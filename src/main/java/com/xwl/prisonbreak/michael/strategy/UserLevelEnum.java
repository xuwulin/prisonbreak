package com.xwl.prisonbreak.michael.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xwl
 * @date 2020-01-20 14:49
 * @description
 */
public enum UserLevelEnum {
    SSVIP("ssvip", "至尊会员", "SsvipTypeServiceImpl"),
    SVIP("svip", "超级会员", "SvipTypeServiceImpl"),
    VIP("vip", "会员", "VipTypeServiceImpl"),
    COMMON("common", "普通用户", "CommonTypeServiceImpl");

    /**
     * 枚举值码
     */
    private final String type;

    /**
     * 枚举描述
     */
    private final String desc;

    /**
     * 实现类
     */
    private final String clazz;

    /**
     * 构造函数
     *
     * @param type  类型
     * @param desc  描述
     * @param clazz 实现类
     */
    UserLevelEnum(String type, String desc, String clazz) {
        this.type = type;
        this.desc = desc;
        this.clazz = clazz;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getClazz() {
        return clazz;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static Map<String, String> getAllStatusCode() {
        Map<String, String> map = new HashMap<>(16);
        for (UserLevelEnum status : values()) {
            map.put(status.getType(), status.getDesc());
        }
        return map;
    }

    /**
     * 获取全部枚举对应的实现类
     *
     * @return
     */
    public static Map<String, String> getAllClazz() {
        Map<String, String> map = new HashMap<>(16);
        for (UserLevelEnum status : values()) {
            map.put(status.getType().trim(), "com.xwl.prisonbreak.michael.strategy.service.impl." + status.getClazz());
        }
        return map;
    }
}
