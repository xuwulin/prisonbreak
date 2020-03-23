package com.xwl.prisonbreak.common.util;

/**
 * snowflake 雪花算法
 */
public class SnowflakeIdWorker {

    /**
     * 工作机器id(10进制：0~31，二进制：00000->11111)，(2^5 - 1 = 31，从0开始，所以要减1)
     * 5bit
     */
    private long workerId;

    /**
     * 数据中心id(10进制：0~31，二进制：00000 -> 11111)，(2^5 - 1 = 31，从0开始，所以要减1)
     * 5bit
     */
    private long datacenterId;

    /**
     * 毫秒内序列(10进制：0~4095，二进制：000000000000 -> 111111111111)，(2^12 - 1 = 4095，从0开始，所以要减1)
     * 12bit
     */
    private long sequence;

    /**
     * 上次生成id的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 开始时间(2019-01-01)
     */
    private final long twepoch = 1546314057426L;

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31（这个移位算法可以很快的计算出二进制所能表示的最大十进制数）
     * 这个是二进制运算，就是 5 bit最多只能有31个数字，也就是说机器id最多只能是32以内
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31（这个移位算法可以很快的计算出二进制所能表示的最大十进制数）
     * 这个是一个意思，就是 5 bit最多只能有31个数字，机房id最多只能是32以内
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 序列在id中所占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器id向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12 + 5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间戳向左移22位(12 + 5 + 5)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里是4095(0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    public SnowflakeIdWorker(long workerId, long datacenterId, long sequence) {
        // sanity check for workerId
        // 这儿不就检查了一下，要求就是你传递进来的机房id和机器id不能超过32，不能小于0
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        System.out.printf(
                "worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workerId);

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获得下一个id(该方法是线程安全的)
     *
     * @return
     */
    public synchronized long nextId() {
        // 这儿就是获取当前时间戳，单位是毫秒
        long timestamp = timeGen();

        // 如果当前时间小于上次id生成的时间戳，说明系统时钟回退过这个时候，应当抛异常
        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            // 这个意思是说一个毫秒内最多只能有4096个数字
            // 无论你传递多少进来，这个位运算保证始终就是在4096这个范围内，避免你自己传递个sequence超过了4096这个范围
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // 阻塞到下一个毫秒，获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        // 这儿记录一下最近一次生成id的时间戳，单位是毫秒
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位id
        // 这儿就是将时间戳左移，放到 41 bit那儿；
        // 将机房 id 左移放到 5 bit那儿；
        // 将机器 id 左移放到 5 bit那儿；将序号放最后 12 bit；
        // 最后拼接起来成一个 64 bit的二进制数字，转换成 10 进制就是个 long 型
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成id的时间戳
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    // ---------------测试---------------
    public static void main(String[] args) {
        SnowflakeIdWorker idworker = new SnowflakeIdWorker(1, 1, 1);
        for (int i = 0; i < 10; i++) {
            long id = idworker.nextId();
            System.out.println(id + "\t 长度：" + String.valueOf(id).length());
        }
    }
}