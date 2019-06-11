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
    public static List<Map<String, Object>> getFieldAnnotationNameAndValue(Class<?> clazz)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 定义要返回的结构
        List<Map<String, Object>> resList = new ArrayList<>();

        // 获取该类的所有属性（字段）
        Field[] fields = clazz.getDeclaredFields();

        // 遍历每一个属性（字段）
        for (Field field : fields) {
            Map<String, Object> map = new HashMap<>();
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
                // 判断是否是List类型
                if (typeName.startsWith("java.util.List")) {
                    // 截取类名（一定要是类的全类名）
                    String typeOfgeneric = typeName.substring(typeName.indexOf("<") + 1, typeName.lastIndexOf(">"));
                    // 创建该类的实例
                    Object instance = Class.forName(typeOfgeneric).newInstance();
                    // 返回有层次的结构，不能使用递归
//                    getFieldAnnotationNameAndValue(instance.getClass());

                    // 二级结构
                    List<Map<String, Object>> subList = new ArrayList<>();
                    Field[] subFields = instance.getClass().getDeclaredFields();
                    for (Field subField : subFields) {
                        if (subField.isAnnotationPresent(ApiModelProperty.class)) {
                            Map<String, Object> subMap = new HashMap<>();

                            ApiModelProperty subFieldAnnotation = subField.getAnnotation(ApiModelProperty.class);

                            subMap.put(subFieldAnnotation.name(), subFieldAnnotation.value());
                            subList.add(subMap);
                        }
                    }
                    map.put(annotation.name(), subList);
                }
            }
            resList.add(map);
        }
        return resList;
    }
}
