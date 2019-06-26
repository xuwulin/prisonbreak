package com.xwl.prisonbreak.util.obj2map;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/23 16:08
 * @Description: 对象和map互转，需要借助 org.apache.commons.beanutils工具类，commons-beanutils（maven依赖）
 */
public class Object2Map {

    /**
     * org.apache.commons.beanutils工具类，对象转map
     * @param obj 实体对象
     * @return
     */
    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }

    /**
     * org.apache.commons.beanutils工具类，map转对象
     * @param map map数据
     * @param beanClass 实体对象
     * @return
     * @throws
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass)  {
        if (map == null) {
            return null;
        }

        Object obj = null;
        try {
            obj = beanClass.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 将 List<Map<String, Object>> 转换成平级的 Map<String, Object>
     * @param list list结构：
     *  list = {ArrayList@532}  size = 1
     *  0 = {HashMap@531}  size = 4
     *   "address" -> {ArrayList@530}  size = 1
     *    key = "address"
     *    value = {ArrayList@530}  size = 1
     *     0 = {HashMap@529}  size = 2
     *      "province" -> "sichuan"
     *      "city" -> "chengdu"
     *   "gender" -> "male"
     *   "name" -> "Frank"
     *   "age" -> "18"
     *
     * @return 返回结构：
     * splitMap = {HashMap@533}  size = 5
     *  "address.city" -> "chengdu"
     *  "gender" -> "male"
     *  "name" -> "Frank"
     *  "address.province" -> "sichuan"
     *  "age" -> "18"
     *
     */
    public static Map<String, Object> splitMap(List<Map<String, Object>> list) {
        Map<String, Object> resMap = new HashMap<>(16);
        for (Map<String, Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof List) {
                    List<Map<String, Object>> list2 = (List<Map<String, Object>>) value;
                    for (Map<String, Object> map2 : list2) {
                        for (Map.Entry<String, Object> entry2 : map2.entrySet()) {
                            String key2 = entry2.getKey();
                            Object value2 = entry2.getValue();
                            resMap.put(key + "." + key2, value2);
                        }
                    }
                } else {
                    resMap.put(key, value);
                }
            }
        }
        return resMap;
    }

    /**
     * 将 Map<String, Object> 转为全是平级的 Map<String, Object>
     * @param map 结构:
     * empMap = {HashMap@530}  size = 4
     *  "address" -> {HashMap@528}  size = 2
     *   key = "address"
     *   value = {HashMap@528}  size = 2
     *    "province" -> "sichuan"
     *    "city" -> "chengdu"
     *  "gender" -> "male"
     *  "name" -> "Frank"
     *  "age" -> "18"
     *
     * @return 返回结构：
     * splitMap1 = {HashMap@531}  size = 5
     *  "address.city" -> "chengdu"
     *  "gender" -> "male"
     *  "name" -> "Frank"
     *  "address.province" -> "sichuan"
     *  "age" -> "18"
     */
    public static Map<String, Object> splitMap(Map<String, Object> map) {
        Map<String, Object> resMap = new HashMap<>(16);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                Map<String, Object> map2 = (Map<String, Object>) value;
                for (Map.Entry<String, Object> entry2 : map2.entrySet()) {
                    String key2 = entry2.getKey();
                    Object value2 = entry2.getValue();
                    resMap.put(key + "." + key2, value2);
                }
            } else {
                resMap.put(key, value);
            }
        }
        return resMap;
    }
}
