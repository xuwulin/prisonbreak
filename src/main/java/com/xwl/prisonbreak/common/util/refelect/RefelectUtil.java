package com.xwl.prisonbreak.common.util.refelect;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/23 16:11
 * @Description: 反射工具类
 */
public class RefelectUtil {

    /**
     * 传入一个对象，获取该对象的信息，如属性类型、属性名、属性对应的值、方法名等
     * @param object
     * @throws Exception
     */
    public static void getObjectValue(Object object) throws Exception {
        if (object != null) {
            // 拿到该类
            Class<?> clz = object.getClass();
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = clz.getDeclaredFields();

            for (Field field : fields) {
                // 属性类型
                Type genericType = field.getGenericType();
                System.out.println(genericType);
                // 属性名称
                String name = field.getName();
                System.out.println(name);

                // 类型名（字符串）
                String type = field.getGenericType().toString();

                // 如果类型是String
                // 如果type是类类型，则前面包含"class "，后面跟类名
                if (StringUtils.equals(type, "class java.lang.String")) {
                    // 拿到该属性的gettet方法
                    // 这里需要说明一下：他是根据拼凑的字符来找你写的getter方法的
                    // 在Boolean值的时候是isXXX（默认使用ide生成getter的都是isXXX）
                    // 如果出现NoSuchMethod异常 就说明它找不到那个gettet方法 需要做个规范
                    Method method = object.getClass().getMethod("get" + getMethodName(field.getName()));
                    System.out.println(method);

                    // 调用getter方法获取属性值
                    String val = (String) method.invoke(object);
                    if (val != null) {
                        System.out.println("String type:" + val);
                    }
                }

                // 如果类型是Integer
                if (StringUtils.equals(type, "class java.lang.Integer")) {
                    Method method = object.getClass().getMethod("get" + getMethodName(field.getName()));
                    Integer val = (Integer) method.invoke(object);
                    if (val != null) {
                        System.out.println("Integer type:" + val);
                    }
                }

                // 如果类型是Double
                if (StringUtils.equals(type, "class java.lang.Double")) {
                    Method method = object.getClass().getMethod("get" + getMethodName(field.getName()));
                    Double val = (Double) method.invoke(object);
                    if (val != null) {
                        System.out.println("Double type:" + val);
                    }
                }

                // 如果类型是Boolean 是封装类
                if (StringUtils.equals(type, "class java.lang.Boolean")) {
                    Method method = object.getClass().getMethod(field.getName());
                    Boolean val = (Boolean) method.invoke(object);
                    if (val != null) {
                        System.out.println("Boolean type:" + val);
                    }
                }

                // 如果类型是boolean 基本数据类型不一样 这里有点说名如果定义名是 isXXX的 那就全都是isXXX的
                // 反射找不到getter的具体名
                if (StringUtils.equals(type, "boolean")) {
                    Method method = object.getClass().getMethod(field.getName());
                    Boolean val = (Boolean) method.invoke(object);
                    if (val != null) {
                        System.out.println("boolean type:" + val);
                    }
                }

                // 如果类型是Date
                if (StringUtils.equals(type, "class java.util.Date")) {
                    Method method = object.getClass().getMethod("get" + getMethodName(field.getName()));
                    Date val = (Date) method.invoke(object);
                    if (val != null) {
                        System.out.println("Date type:" + val);
                    }
                }

                // 如果类型是Short
                if (StringUtils.equals(type, "class java.lang.Short")) {
                    Method method = object.getClass().getMethod("get" + getMethodName(field.getName()));
                    Short val = (Short) method.invoke(object);
                    if (val != null) {
                        System.out.println("Short type:" + val);
                    }
                }

                // 类型是List
                if (type.startsWith("java.util.List")) {
                    // List<>泛型中的类型
                    String typeOfgeneric = type.substring(type.indexOf("<") + 1, type.lastIndexOf(">"));
                    // 将字符串转为对应的类
                    Class<?> aClass = Class.forName(typeOfgeneric);
                    // 将字符串转为对应的类后再new一个实例
                    Object instance = Class.forName(typeOfgeneric).newInstance();
                    // 获取方法
                    Method method = object.getClass().getMethod("get" + getMethodName(field.getName()));
                    // 获取值 得到的invoke为List
                    Object invoke = method.invoke(object);
                    // 强转为list
                    List<?> list = (List) invoke;
                    for (int i = 0; i < list.size(); i++) {
                        // 对象转map
//                        Map<?, ?> map = objectToMap(list.get(i));
//                        System.out.println(map);
//                        for (Map.Entry<?, ?> entry : map.entrySet()) {
//                            if (!StringUtils.equals(entry.getKey().toString(), "class")) {
//                                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//                            }
//                        }
                    }

                    if (invoke != null) {
                        System.out.println(invoke);
                    }
                }
                // 如果还需要其他的类型请自己做扩展
            }
        }
    }

    /**
     * 把一个字符串的第一个字母大写、效率是最高的
     * @param fildeName
     * @return
     */
    private static String getMethodName(String fildeName) {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
}
