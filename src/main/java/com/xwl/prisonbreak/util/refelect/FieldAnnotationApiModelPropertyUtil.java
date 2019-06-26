package com.xwl.prisonbreak.util.refelect;

import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xwl
 * @Date: 2019/6/11 17:36
 * @Description: 获取某个实体类中每个属性上的 @ApiModelProperty 注解中的值
 */
public class FieldAnnotationApiModelPropertyUtil {
    /**
     * 解析类属性上的注解 @ApiModelProperty 返回 List<Map<String, Object>>
     *     注：最多只能解析两层
     * @param clazz
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static List<Map<String, Object>> getFieldAnnotationNameAndValue2List(Class<?> clazz)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        // 定义要返回的结构
        List<Map<String, Object>> resList = new ArrayList<>();

        // 向上循环，遍历父类(如果有继承父类，没有继承也不会有问题)
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            // 获取该类的所有属性（字段）
            Field[] fields = clazz.getDeclaredFields();

            // 遍历每一个属性（字段）
            for (Field field : fields) {
                Map<String, Object> map = new HashMap<>(16);
                // 判断该属性（字段）上是否存在 @ApiModelProperty 注解
                if (field.isAnnotationPresent(ApiModelProperty.class)) {
                    // 获取该注解的信息：如@ApiModelProperty(name = "name", value = "姓名")中的 name和vaule的值
                    ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                    String name = annotation.name();
                    String value = annotation.value();
                    map.put(name, value);

                    // 获取属性（字段）的类型
                    Type type = field.getGenericType();
                    // 类型名（字符串）
                    String typeName = type.getTypeName();
                    // 判断该属性是否是List类型
                    if (typeName.startsWith("java.util.List")) {
                        // 截取类名（一定要是类的全类名）
                        String typeOfgeneric = typeName.substring(typeName.indexOf("<") + 1, typeName.lastIndexOf(">"));
                        // 创建该类的实例
                        Object instance = Class.forName(typeOfgeneric).newInstance();
                        Class<?> subClazz = instance.getClass();
                        // 返回有层次的结构，不能使用递归
//                    getFieldAnnotationNameAndValue(instance.getClass());

                        // 二级结构
                        List<Map<String, Object>> subList = new ArrayList<>();
                        for (; subClazz != Object.class; subClazz = subClazz.getSuperclass()) {
                            Field[] subFields = subClazz.getDeclaredFields();
                            for (Field subField : subFields) {
                                if (subField.isAnnotationPresent(ApiModelProperty.class)) {
                                    Map<String, Object> subMap = new HashMap<>(16);
                                    ApiModelProperty subFieldAnnotation = subField.getAnnotation(ApiModelProperty.class);
                                    String subName = subFieldAnnotation.name();
                                    String subValue = subFieldAnnotation.value();
                                    subMap.put(subName, subValue);
                                    subList.add(subMap);
                                }
                            }
                            map.put(annotation.name(), subList);
                        }
                    } else {
                        // 判断该属性是否是其他类型
                    }
                }
                resList.add(map);
            }
        }
        return resList;
    }

    /**
     * 解析类属性上的注解 @ApiModelProperty 返回 Map<String, Object>
     *     注：最多只能解析两层
     * @param clazz
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Map<String, Object> getFieldAnnotationNameAndValue2Map(Class<?> clazz)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        // 定义要返回的结构
        Map<String, Object> resMap = new HashMap<>();

        // 向上循环，遍历父类(如果有继承父类，没有继承也不会有问题)
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            // 获取该类的所有属性（字段）
            Field[] fields = clazz.getDeclaredFields();

            // 遍历每一个属性（字段）
            for (Field field : fields) {
                // 判断该属性（字段）上是否存在 @ApiModelProperty 注解
                if (field.isAnnotationPresent(ApiModelProperty.class)) {
                    // 获取该注解的信息：如@ApiModelProperty(name = "name", value = "姓名")中的 name和vaule的值
                    ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                    String name = annotation.name();
                    String value = annotation.value();

                    // 添加到map中
                    resMap.put(name, value);

                    // 获取属性（字段）的类型
                    Type type = field.getGenericType();
                    // 类型名（字符串）
                    String typeName = type.getTypeName();
                    // 判断该属性是否是List类型
                    if (typeName.startsWith("java.util.List")) {
                        // 截取类名（一定要是类的全类名）
                        String typeOfgeneric = typeName.substring(typeName.indexOf("<") + 1, typeName.lastIndexOf(">"));
                        // 创建该类的实例
                        Object instance = Class.forName(typeOfgeneric).newInstance();
                        Class<?> subClazz = instance.getClass();

                        // 二级结构
                        Map<String, Object> subMap = new HashMap<>();
                        for (; subClazz != Object.class; subClazz = subClazz.getSuperclass()) {
                            Field[] subFields = subClazz.getDeclaredFields();
                            for (Field subField : subFields) {
                                if (subField.isAnnotationPresent(ApiModelProperty.class)) {
                                    ApiModelProperty subFieldAnnotation = subField.getAnnotation(ApiModelProperty.class);
                                    String subName = subFieldAnnotation.name();
                                    String subValue = subFieldAnnotation.value();
                                    subMap.put(subName, subValue);
                                }
                            }
                        }
                        resMap.put(name, subMap);
                    } else {
                        // 判断该属性是否是其他类型
                    }
                }
            }
        }
        return resMap;
    }
}
