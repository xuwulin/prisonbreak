package com.xwl.prisonbreak.util.obj2map;

import java.lang.reflect.InvocationTargetException;
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
}
