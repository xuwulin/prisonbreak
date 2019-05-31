package com.xwl.prsonbreak.prisonbreak;

import com.xwl.prisonbreak.PrisonbreakApplication;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrisonbreakApplication.class)
public class PrisonbreakApplicationTests {

    @Test
    public void contextLoads() {
    }

    // ========================================Lambda表达式=============================================================

    /**
     * 测试使用Comparator，传统方式->匿名类->lambda->方法引用
     */
    public static class Apple {
        private int weight = 0;
        private String color = "";

        public Apple(int weight, String color) {
            this.weight = weight;
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String toString() {
            return "Apple{" +
                    "color='" + color + '\'' +
                    ", weight=" + weight +
                    '}';
        }
    }

    List<Apple> inventory = Arrays.asList(
            new Apple(80, "green"),
            new Apple(155, "green"),
            new Apple(120, "red"));

    /**
     * 第一种方式，传统方法！
     */
    public class AppleComparator implements Comparator<Apple> {
        public int compare(Apple a1, Apple a2) {
            return a1.getWeight().compareTo(a2.getWeight());
        }
    }

    @Test
    public void test1() {
        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}, Apple{color='red', weight=120}]
        System.out.println(inventory);
        inventory.sort(new AppleComparator());
        // [Apple{color='green', weight=80}, Apple{color='red', weight=120}, Apple{color='green', weight=155}]
        System.out.println(inventory);
    }

    @Test
    public void test2() {
        /**
         * 第二种方式，使用匿名类
         */
        System.out.println(inventory);
        inventory.sort(new Comparator<Apple>() {
            public int compare(Apple a1, Apple a2) {
                return a1.getWeight().compareTo(a2.getWeight());
            }
        });
        System.out.println(inventory);
    }

    @Test
    public void test3() {
        /**
         * 第三种方式：使用lambda表达式
         */
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

        /**
         * 简化1
         */
        inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));

        /**
         * 简化2： Comparator 具有一个叫作 comparing 的静态辅助方法，
         * 它可以接受一个 Function 来提取 Comparable 键值，并生成一个 Comparator 对象
         */
        Comparator<Apple> c = Comparator.comparing((Apple a) -> a.getWeight());

        /**
         * 简化3：需要导入：import static java.util.Comparator.comparing;包
         */
        inventory.sort(comparing((a) -> a.getWeight()));
    }

    @Test
    public void test4() {
        /**
         * 第四种方式：方式引用：需要导入：import static java.util.Comparator.comparing;包
         */
        inventory.sort(comparing(Apple::getWeight));
    }

    @Test
    public void test5() {
        /**
         * 逆序：接口有一个默认方法 reversed 可以使给定的比较器逆序。因此仍然用开始的那个比较器，只要修改
         * 一下前一个例子就可以对苹果按重量递减排序：
         */
        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}, Apple{color='red', weight=120}]
        System.out.println(inventory);
        inventory.sort(comparing(Apple::getWeight).reversed()); // reversed()按重量递减排序
        // [Apple{color='green', weight=155}, Apple{color='red', weight=120}, Apple{color='green', weight=80}]
        System.out.println(inventory);
    }

    @Test
    public void test6() {
        /**
         * 比较器链: 按重量递减排序, 两个苹果一样重时根据颜色排序
         */
        inventory.sort(comparing(Apple::getWeight)
                .reversed()
                .thenComparing(Apple::getColor));
    }

    @Test
    public void test7() {
        /**
         * 谓词接口包括三个方法： negate 、 and 和 or ，让你可以重用已有的 Predicate 来创建更复
         * 杂的谓词。比如，你可以使用 negate 方法来返回一个 Predicate 的非，比如苹果不是红的
         */
        List<Apple> red = inventory.stream().filter(a -> StringUtils.equals(a.getColor(), "red")).collect(toList());
        System.out.println(red);

        Predicate<Apple> redApple = a -> StringUtils.equals(a.getColor(), "red");

        // 产生现有 Predicate对象 redApple 的非
        Predicate<Apple> notRedApple = redApple.negate();

        // 链接两个谓词来生成另一个 Predicate 对象
        Predicate<Apple> redAndHeavyApple = redApple.and(a -> a.getWeight() > 150);

        // 链接 Predicate 的方法来构造更复杂Predicate 对象
        Predicate<Apple> redAndHeavyAppleOrGreen = redApple.and(a -> a.getWeight() > 150)
                .or(a -> "green".equals(a.getColor()));
    }

    @Test
    public void test8() {
        /**
         *  函数复合
         *  还可以把 Function 接口所代表的Lambda表达式复合起来。 Function 接口为此配
         * 了 andThen 和 compose 两个默认方法，它们都会返回 Function 的一个实例
         * andThen 方法会返回一个函数，它先对输入应用一个给定函数，再对输出应用另一个函数。
         * 比如，假设有一个函数 f 给数字加1 (x -> x + 1) ，另一个函数 g 给数字乘2，你可以将它们组
         * 合成一个函数 h ，先给数字加1，再给结果乘2：
         */
        Function<Integer, Integer> f = x -> x + 1; // 2
        Function<Integer, Integer> g = x -> x * 2; // 4
        Function<Integer, Integer> h = f.andThen(g);
        int result = h.apply(1); // 结果为4
        System.out.println(result);

        /**
         * 你也可以类似地使用 compose 方法，先把给定的函数用作 compose 的参数里面给的那个函
         * 数，然后再把函数本身用于结果。比如在上一个例子里用 compose 的话，它将意味着 f(g(x)) ，
         * 而 andThen 则意味着 g(f(x)) ：
         */
        Function<Integer, Integer> m = x -> x + 1;
        Function<Integer, Integer> n = x -> x * 2;
        Function<Integer, Integer> o = m.compose(n); // 先计算n = 1 * 2,再计算m m = 2 + 1
        int result2 = o.apply(1); // 结果为3
        System.out.println(result2);
    }


    // ===========================================Stream流处理================================================
    public static class Dish {

        private final String name;
        private final boolean vegetarian;
        private final int calories;
        private final Type type;

        public Dish(String name, boolean vegetarian, int calories, Type type) {
            this.name = name;
            this.vegetarian = vegetarian;
            this.calories = calories;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public boolean isVegetarian() {
            return vegetarian;
        }

        public int getCalories() {
            return calories;
        }

        public Type getType() {
            return type;
        }

        public enum Type {MEAT, FISH, OTHER}

        @Override
        public String toString() {
            return name;
        }
    }

    public static final List<Dish> menu =
            Arrays.asList(new Dish("pork", false, 800, Dish.Type.MEAT),
                    new Dish("beef", false, 700, Dish.Type.MEAT),
                    new Dish("chicken", false, 400, Dish.Type.MEAT),
                    new Dish("french fries", true, 530, Dish.Type.OTHER),
                    new Dish("rice", true, 350, Dish.Type.OTHER),
                    new Dish("season fruit", true, 120, Dish.Type.OTHER),
                    new Dish("pizza", true, 550, Dish.Type.OTHER),
                    new Dish("prawns", false, 400, Dish.Type.FISH),
                    new Dish("salmon", false, 450, Dish.Type.FISH));

    @Test
    public void testStream1() {
        List<String> lowCaloricDishesName = menu.stream()
                .filter(d -> d.getCalories() < 400) // 过滤卡路里小于400的失误
                .sorted(comparing(Dish::getCalories)) // 按照热量排序，升序 comparing(Dish::getCalories).reversed()降序
                .map(Dish::getName) // 提取菜名
                .collect(toList()); // 转为集合
        System.out.println(lowCaloricDishesName); // [season fruit, rice]

        // 使用并行流
        List<String> lowCaloricDishesName2 = menu.parallelStream()
                .filter(d -> d.getCalories() < 400) // 过滤卡路里小于400的失误
                .sorted(comparing(Dish::getCalories)) // 按照热量排序，升序 comparing(Dish::getCalories).reversed()降序
                .map(Dish::getName) // 提取菜名
                .collect(toList()); // 转为集合

        // 按照类型分组
        Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));
        System.out.println(dishesByType);

        // limit
        // 接受一个Lambda，将元素转换成其他形式或提取信息。在本例中，通过传递方法引用 Dish::getName ，
        // 相当于Lambda d -> d.getName() ，提取了每道菜的菜名
        List<String> threeHighCaloricDishNames = menu.stream()
                .filter(d -> d.getCalories() > 300) // 过滤卡路里大于300的失误
                .map(Dish::getName) // 提取菜名
                .limit(3) // 只取前3条记录
                .collect(toList()); // 转为集合
        System.out.println(threeHighCaloricDishNames);

        List<String> names = menu.stream()
                .filter(d -> {
                    System.out.println("filtering：" + d.getName());
                    return d.getCalories() > 300;
                })
                .map(d -> {
                    System.out.println("mapping：" + d.getName());
                    return d.getName();
                })
                .limit(3)
                .collect(toList()); // 执行到此才会打印filter和map中的信息
        System.out.println(names); // [pork, beef, chicken]

        // 跳过元素
        // 请注意， limit(n) 和 skip(n) 是互补的！例如，下面的代码将跳过超过300卡路里的头两道菜，并返回剩下的。
        List<Dish> dishes = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .skip(2)
                .collect(toList());

        // 找出每道菜名的长度
        List<Integer> dishNameLengths = menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(toList());
        System.out.println(dishNameLengths);
    }

    @Test
    public void testStream2() {
        /**
         * 流的扁平化: flatMap
         *  给 定 单 词 列 表 ["Hello","World"] ，
         *  你想要返回列表 ["H","e","l", "o","W","r","d"] 。
         */
        List<String> words = Arrays.asList("Hello", "World");
        List<String[]> collect = words.stream()
                .map(word -> word.split("")) // 此操作返回的是：Stream<String[]>
                .distinct() // 此操作返回的也是：Stream<String[]>
                .collect(toList()); // 此操作返回的也是：Stream<String[]>
        System.out.println(collect);

        // 1、尝试使用 map 和 Arrays.stream()
        // 首先，你需要一个字符流，而不是数组流。有一个叫作 Arrays.stream() 的方法可以接受一个数组并产生一个流，例如：
        String[] arrayOfWords = {"Goodbye", "World"};
        Stream<String> streamOfwords = Arrays.stream(arrayOfWords);
        List<Stream<String>> collect1 = streamOfwords
                .map(word -> word.split(""))
                .map(Arrays::stream)
                .distinct()
                .collect(toList()); // 仍然解决不了问题

        // 你可以像下面这样使用 flatMap 来解决这个问题
        // 使用 flatMap 方法的效果是，各个数组并不是分别映射成一个流，而是映射成流的内容。
        // 所有使用 map(Arrays::stream) 时生成的单个流都被合并起来，即扁平化为一个流
        // 一言以蔽之， flatmap 方法让你把一个流中的每个值都换成另一个流，然后把所有的流连接起来成为一个流。
        List<String> uniqueCharacters = words.stream()
                .map(w -> w.split("")) // 返回Stream<String[]>
                .flatMap(Arrays::stream) // 返回Stream<String>
                .distinct() // 返回Stream<String>
                .collect(Collectors.toList()); // 返回List<String>
        System.out.println(uniqueCharacters); // [H, e, l, o, W, r, d]
    }

    @Test
    public void testStream3() {
        /**
         * 查找:findAny 方法将返回当前流中的任意元素。它可以与其他流操作结合使用。比如，你可能想
         * 找到一道素食菜肴。你可以结合使用 filter 和 findAny 方法来实现这个查询：
         */
        Optional<Dish> dish = menu.stream()
                .filter(Dish::isVegetarian)
                .findAny();

        /**
         * isPresent() 将在 Optional 包含值的时候返回 true , 否则返回 false 。
         * ifPresent(Consumer<T> block) 会在值存在的时候执行给定的代码块。我们在第3章
         * 介绍了 Consumer 函数式接口；它让你传递一个接收 T 类型参数，并返回 void 的Lambda表达式。
         * T get() 会在值存在时返回值，否则抛出一个 NoSuchElement 异常。
         * T orElse(T other) 会在值存在时返回值，否则返回一个默认值。
         */
        menu.stream()
                .filter(Dish::isVegetarian)
                .findAny()
                .ifPresent(d -> System.out.println(d.getName()));

        /**
         * 查找第一个元素
         * 有些流有一个出现顺序（encounter order）来指定流中项目出现的逻辑顺序（比如由 List 或
         * 排序好的数据列生成的流）。对于这种流，你可能想要找到第一个元素。为此有一个 findFirst
         * 方法，它的工作方式类似于 findany 。例如，给定一个数字列表，下面的代码能找出第一个平方
         * 能被3整除的数：
         */
        List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> firstSquareDivisibleByThree = someNumbers.stream()
                .map(x -> x * x)
                .filter(x -> x % 3 == 0)
                .findFirst(); // 9

        // 何时使用 findFirst 和 findAny???
        // 找到第一个元素在并行上限制更多。如果你不关心返回的元素是哪个，请使用 findAny ，因为它在使用并行流时限制较少。
    }

    @Test
    public void testStream4() {
        /**
         * 归约
         * 使用 reduce 操作来表达更复杂的查询，比如“计算菜单中的总卡路里”或“菜单中卡路里最高的菜是哪一个”。此类查询需要将流
         * 中所有元素反复结合起来，得到一个值，比如一个 Integer 。这样的查询可以被归类为归约操作
         * （将流归约成一个值）。用函数式编程语言的术语来说，这称为折叠（fold），因为你可以将这个操
         * 作看成把一张长长的纸（你的流）反复折叠成一个小方块，而这就是折叠操作的结果。
         *
         * reduce 接受两个参数：
         *  一个初始值，这里是0；
         *  一个 BinaryOperator<T> 来将两个元素结合起来产生一个新值，这里我们用的是lambda (a, b) -> a + b 。
         *
         * 首先， 0 作为Lambda（ a ）的
         * 第一个参数，从流中获得 4 作为第二个参数（ b ）。 0 + 4 得到 4 ，它成了新的累积值。然后再用累
         * 积值和流中下一个元素 5 调用Lambda，产生新的累积值 9 。接下来，再用累积值和下一个元素 3
         * 调用Lambda，得到 12 。最后，用 12 和流中最后一个元素 9 调用Lambda，得到最终结果 21 。
         */
        List<Integer> numbers = Arrays.asList(4, 5, 3, 9);
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        System.out.println(sum); // 21

        // 你可以使用方法引用让这段代码更简洁。
        // 在Java 8中， Integer 类现在有了一个静态的 sum方法来对两个数求和，这恰好是我们想要的，用不着反复用Lambda写同一段代码了：
        int sum1 = numbers.stream().reduce(0, Integer::sum);

        /**
         * 无初始值
         * reduce 还有一个重载的变体，它不接受初始值，但是会返回一个 Optional 对象：
         *
         * 为什么它返回一个 Optional<Integer> 呢？
         * 考虑流中没有任何元素的情况。 reduce 操作无法返回其和，因为它没有初始值。
         * 这就是为什么结果被包裹在一个 Optional 对象里，以表明和可能不存在。
         */
        Optional<Integer> sum2 = numbers.stream().reduce((a, b) -> (a + b));
        Optional<Integer> sum3 = numbers.stream().reduce(Integer::sum);

        /**
         * 最大值和最小值
         */
        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        Optional<Integer> min = numbers.stream().reduce(Integer::min);
        // 使用lambda更容易明了
        Optional<Integer> min2 = numbers.stream().reduce((x, y) -> x < y ? x : y);

        /**
         * 怎样用 map 和 reduce 方法数一数流中有多少个菜呢？
         *
         * 要解决这个问题，你可以把流中每个元素都映射成数字 1 ，然后用 reduce 求和。这相当于按顺序数流中的元素个数。
         */
        int count = menu.stream()
                .map(d -> 1)
                .reduce(0, (a, b) -> a + b);

        /**
         * map 和 reduce 的连接通常称为 map-reduce 模式，因Google用它来进行网络搜索而出名，
         * 因为它很容易并行化。请注意，在第4章中我们也看到了内置 count 方法可用来计算流中元素
         * 的个数：
         */
        long count2 = menu.stream().count();
    }

    // =======================================练习使用流===========================================================
    // 交易员
    public static class Trader {

        private String name;
        private String city;

        public Trader(String n, String c) {
            this.name = n;
            this.city = c;
        }

        public String getName() {
            return this.name;
        }

        public String getCity() {
            return this.city;
        }

        public void setCity(String newCity) {
            this.city = newCity;
        }

        public String toString() {
            return "Trader:" + this.name + " in " + this.city;
        }
    }

    // 交易
    public static class Transaction {
        private Trader trader;
        private int year;
        private int value;

        public Transaction(Trader trader, int year, int value) {
            this.trader = trader;
            this.year = year;
            this.value = value;
        }

        public Trader getTrader() {
            return this.trader;
        }

        public int getYear() {
            return this.year;
        }

        public int getValue() {
            return this.value;
        }

        public String toString() {
            return "{" + this.trader + ", " +
                    "year: " + this.year + ", " +
                    "value:" + this.value + "}";
        }
    }

    Trader raoul = new Trader("Raoul", "Cambridge");
    Trader mario = new Trader("Mario", "Milan");
    Trader alan = new Trader("Alan", "Cambridge");
    Trader brian = new Trader("Brian", "Cambridge");
    List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
    );

    @Test
    public void testExerStream() {
        // (1) 找出2011年发生的所有交易，并按交易额排序（从低到高）。
        List<Transaction> collect1 = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(comparing(Transaction::getValue))
                .collect(Collectors.toList());
        System.out.println(collect1); // [{Trader:Brian in Cambridge, year: 2011, value:300}, {Trader:Raoul in Cambridge, year: 2011, value:400}]

        // (2) 交易员都在哪些不同的城市工作过？
        // 方式一：使用distinct()去重
        List<String> collect2 = transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .collect(toList());
        // 方式二：使用set
        Set<String> cities = transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .collect(toSet());

        // (3) 查找所有来自于剑桥的交易员，并按姓名排序。
        List<Transaction> cambridge = transactions.stream()
                .filter(t -> StringUtils.equals(t.getTrader().getCity(), "Cambridge"))
                .distinct()
                .sorted(comparing(t -> t.getTrader().getName()))
                .collect(toList());

        // (4) 返回所有交易员的姓名字符串，按字母顺序排序。
        // 方式一：效率不高
        String traderStr = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted() // 对姓名按字母顺序排序
                .reduce("", (n1, n2) -> n1 + n2); // 逐个拼接每个名字，得到一个将所有名字连接起来的 String
        // 方式二：效率高
        String traderStr2 = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .collect(joining());

        // (5) 有没有交易员是在米兰工作的？
        boolean milan = transactions.stream()
                .anyMatch(t -> StringUtils.equals(t.getTrader().getCity(), "Milan"));

        // (6) 打印生活在剑桥的交易员的所有交易额。
        transactions.stream()
                .filter(t -> StringUtils.equals(t.getTrader().getCity(), "Cambridge"))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        // (7) 所有交易中，最高的交易额是多少？
        // 方式一
        Optional<Integer> max = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);
        // 方式二
        Optional<Transaction> max1max2 = transactions.stream()
                .max(comparing(Transaction::getValue));

        // (8) 找到交易额最小的交易。
        // 方式一
        Optional<Transaction> smallestTransaction = transactions.stream()
                .reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2);
        // 方式二：
        Optional<Transaction> smallestTransaction2 = transactions.stream()
                .min(comparing(Transaction::getValue));
    }

    @Test
    public void testStream5() {
        /**
         * 数值流
         */
        int calories = menu.stream() // 返回Stream<Dish>
                .mapToInt(Dish::getCalories) // 返回一个IntStream
                .sum();

        /**
         * 转换回对象流
         * 一旦有了数值流，你可能会想把它转换回非特化流。例如， IntStream 上的操作只能产生原始整数：
         * IntStream 的 map 操作接受的Lambda必须接受 int 并返回 int （一个IntUnaryOperator ）。
         * 但是你可能想要生成另一类值，比如 Dish 。为此，你需要访问 Stream接口中定义的那些更广义的操作。
         * 要把原始流转换成一般流（每个 int 都会装箱成一个Integer ），可以使用 boxed 方法
         */
        IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
        Stream<Integer> stream = intStream.boxed(); // 将数值流转为Stream

        /**
         * 默认值 OptionalInt
         * 如果没有最大值的话，你就可以显式处理 OptionalInt 去定义一个默认值了：
         */
        OptionalInt maxCalories = menu.stream()
                .mapToInt(Dish::getCalories)
                .max();
        int max = maxCalories.orElse(1); // 如果没有最大值的话，显式提供一个默认最大值

        /**
         * 数值范围
         * Java 8引入了两个可以用于 IntStream 和 LongStream 的静态方法，帮助生成这种范围：
         * range 和 rangeClosed 。这两个方法都是第一个参数接受起始值，第二个参数接受结束值。但
         * range 是不包含结束值的，而 rangeClosed 则包含结束值
         */
        IntStream evenNumbers = IntStream.rangeClosed(1, 100) // 表示范围[1, 100]
                .filter(n -> n % 2 == 0); // 一个从1到100的偶数流
        System.out.println(evenNumbers.count()); // 从1到100有50个偶数

        IntStream evenNumbers1 = IntStream.range(1, 100) // 表示范围[1, 100),不包含100
                .filter(n -> n % 2 == 0); // 一个从1到99的偶数流
        System.out.println(evenNumbers1.count()); // 从1到99有49个偶数
    }

    @Test
    public void testStream6() {
        /**
         * 无限流
         * 1.迭代 iterate
         *
         * 斐波纳契元组序列与此类似，是数列中数字和其后续数字组成的元组构成的序列：(0, 1),
         * (1, 1), (1, 2), (2, 3), (3, 5), (5, 8), (8, 13), (13, 21) …
         * 你的任务是用 iterate 方法生成斐波纳契元组序列中的前20个元素
         *
         * iterate的第一个参数是初始值
         */
        // (0,1), (1,1), (1, 2), (2, 3), (3, 5), (5, 8), (8, 13), (13, 21)
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(20)
                .forEach(t -> System.out.println("(" + t[0] + "," + t[1] + ")"));

        // 0, 1, 1, 2, 3, 5, 8, 13, 21, 34…
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .map(t -> t[0])
                .forEach(System.out::println);

        /**
         * 生成
         */
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);
    }

    public static class Transaction2 {
        private final Currency currency;
        private final double value;

        public Transaction2(Currency currency, double value) {
            this.currency = currency;
            this.value = value;
        }

        public Currency getCurrency() {
            return currency;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return currency + " " + value;
        }
    }

    public enum Currency {
        EUR, USD, JPY, GBP, CHF
    }

    public static List<Transaction2> transactions2 = Arrays.asList(new Transaction2(Currency.EUR, 1500.0),
            new Transaction2(Currency.USD, 2300.0),
            new Transaction2(Currency.GBP, 9900.0),
            new Transaction2(Currency.EUR, 1100.0),
            new Transaction2(Currency.JPY, 7800.0),
            new Transaction2(Currency.CHF, 6700.0),
            new Transaction2(Currency.EUR, 5600.0),
            new Transaction2(Currency.USD, 4500.0),
            new Transaction2(Currency.CHF, 3400.0),
            new Transaction2(Currency.GBP, 3200.0),
            new Transaction2(Currency.USD, 4600.0),
            new Transaction2(Currency.JPY, 5700.0),
            new Transaction2(Currency.EUR, 6800.0));

    @Test
    public void testCollect1() {
        /**
         * 用流收集数据
         */
        /**
         * 用指令式风格对交易按照货币分组
         */
        Map<Currency, List<Transaction2>> transactionsByCurrencies = new HashMap<>();
        for (Transaction2 transaction2 : transactions2) {
            Currency currency = transaction2.getCurrency();
            List<Transaction2> transactionsForCurrency = transactionsByCurrencies.get(currency);
            if (transactionsForCurrency == null) {
                transactionsForCurrency = new ArrayList<>();
                transactionsByCurrencies.put(currency, transactionsForCurrency);
            }
            transactionsForCurrency.add(transaction2);
        }
        System.out.println(transactionsByCurrencies);

        /**
         * 使用java8
         */
        Map<Currency, List<Transaction2>> transactionsByCurrencies2 = transactions2.stream()
                .collect(groupingBy(Transaction2::getCurrency));
        System.out.println(transactionsByCurrencies2);

        /**
         * 归约和汇总
         */
        long howManyDishes = menu.stream().collect(Collectors.counting());
        // 简化写法
        long howManyDishes2 = menu.stream().count();

        /**
         * 查找流中的最大值和最小值
         *  Collectors.maxBy 和Collectors.minBy ，来计算流中的最大或最小值
         */
        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));

        /**
         * 汇总
         * Collectors 类专门为汇总提供了一个工厂方法： Collectors.summingInt 。它可接受一
         * 个把对象映射为求和所需 int 的函数，并返回一个收集器
         */
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));

        /**
         * 求平均
         */
        double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));

        /**
         * 到目前为止，你已经看到了如何使用收集器来给流中的元素计数，找到这些元素数值属性的
         * 最大值和最小值，以及计算其总和和平均值。不过很多时候，你可能想要得到两个或更多这样的
         * 结果，而且你希望只需一次操作就可以完成。在这种情况下，你可以使用 summarizingInt 工厂
         * 方法返回的收集器。例如，通过一次 summarizing 操作你可以就数出菜单中元素的个数，并得
         * 到菜肴热量总和、平均值、最大值和最小值：
         *
         * 这个收集器会把所有这些信息收集到一个叫作 IntSummaryStatistics 的类里，它提供了
         * 方便的取值（getter）方法来访问结果。打印 menuStatisticobject 会得到以下输出：
         */
        IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
        // IntSummaryStatistics{count=9, sum=4300, min=120, average=477.777778, max=800}
        System.out.println(menuStatistics);

        /**
         * 连接字符串
         * joining 工厂方法返回的收集器会把对流中每一个对象应用 toString 方法得到的所有字符
         * 串连接成一个字符串。这意味着你把菜单中所有菜肴的名称连接起来，如下所示：
         */
        String shortMenu = menu.stream().map(Dish::getName).collect(joining());
        // porkbeefchickenfrench friesriceseason fruitpizzaprawnssalmon
        System.out.println(shortMenu);

        /**
         * 但该字符串的可读性并不好。幸好， joining 工厂方法有一个重载版本可以接受元素之间的
         * 分界符，这样你就可以得到一个逗号分隔的菜肴名称列表
         */
        String shortMenu1 = menu.stream().map(Dish::getName).collect(joining(", "));
        // pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon
        System.out.println(shortMenu1);
    }

    @Test
    public void testCollection2() {
        /**
         * 广义的归约汇总
         * 它需要三个参数。
         *  第一个参数是归约操作的起始值，也是流中没有元素时的返回值，所以很显然对于数值和而言 0 是一个合适的值。
         *  第二个参数就是你在6.2.2节中使用的函数，将菜肴转换成一个表示其所含热量的 int 。
         *  第三个参数是一个 BinaryOperator ，将两个项目累积成一个同类型的值。这里它就是
         * 对两个 int 求和
         */
        int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
        System.out.println(totalCalories); // 4300

        Optional<Dish> mostCalorieDish = menu.stream()
                .collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));

        /**
         * 收集与归约
         * 在上一章和本章中讨论了很多有关归约的内容。你可能想知道， Stream 接口的 collect
         * 和 reduce 方法有何不同，因为两种方法通常会获得相同的结果。例如，你可以像下面这样使
         * 用 reduce 方法来实现 toListCollector 所做的工作：
         */
        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();
        List<Integer> numbers = stream.
                reduce(new ArrayList<>(), (List<Integer> l, Integer e) -> {
                            l.add(e);
                            return l;
                        },
                        (List<Integer> l1, List<Integer> l2) -> {
                            l1.addAll(l2);
                            return l1;
                        });
        System.out.println(numbers); // [1, 2, 3, 4, 5, 6]

        /**
         * 用 reducing 连接字符串
         *  使用reducing 收集器合法地替代 joining 收集器
         */
        String shortMenu = menu.stream().map(Dish::getName).collect(joining());
        Optional<String> collect = menu.stream().map(Dish::getName).collect(reducing((s1, s2) -> s1 + s2)); // 注意，这里返回的是Optional<String>
        // 方式一
        String shortMenu2 = menu.stream().map(Dish::getName).collect(reducing((s1, s2) -> s1 + s2)).get();
        // 方式二
        String shortMenu3 = menu.stream().collect(reducing("", Dish::getName, (s1, s2) -> s1 + s2));


    }

    public enum CaloricLevel {DIET, NORMAL, FAT}

    @Test
    public void testGroupingBy() {
        /**
         * 分组
         */
        Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));
        // {OTHER=[french fries, rice, season fruit, pizza], FISH=[prawns, salmon], MEAT=[pork, beef, chicken]}
        System.out.println(dishesByType);

        /**
         * 你可能想把热量不到400卡路里的菜划分为“低热量”（diet），热量400到700
         * 卡路里的菜划为“普通”（normal），高于700卡路里的划为“高热量”（fat）
         */
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(
                groupingBy(dish -> {
                    if (dish.getCalories() <= 400) {
                        return CaloricLevel.DIET;
                    } else if (dish.getCalories() <= 700) {
                        return CaloricLevel.NORMAL;
                    } else {
                        return CaloricLevel.FAT;
                    }
                }));
        // {FAT=[pork], NORMAL=[beef, french fries, pizza, salmon], DIET=[chicken, rice, season fruit, prawns]}
        System.out.println(dishesByCaloricLevel);

        /**
         * 多级分组
         * 要实现多级分组，我们可以使用一个由双参数版本的 Collectors.groupingBy 工厂方法创
         * 建的收集器，它除了普通的分类函数之外，还可以接受 collector 类型的第二个参数。那么要进
         * 行二级分组的话，我们可以把一个内层 groupingBy 传递给外层 groupingBy ，并定义一个为流
         * 中项目分类的二级标准，
         */
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel =
                menu.stream().collect(
                        groupingBy(Dish::getType, // 一级分类函数
                                groupingBy(dish -> { // 二级分类函数
                                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                })
                        )
                );
        // {MEAT={FAT=[pork], DIET=[chicken], NORMAL=[beef]}, OTHER={DIET=[rice, season fruit], NORMAL=[french fries, pizza]}, FISH={DIET=[prawns], NORMAL=[salmon]}}
        System.out.println(dishesByTypeCaloricLevel);

    }
}
