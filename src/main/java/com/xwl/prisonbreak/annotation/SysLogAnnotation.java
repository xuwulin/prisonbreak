package com.xwl.prisonbreak.annotation;

import java.lang.annotation.*;


/**
 * @Auther: xwl
 * @Date: 2019/6/19 17:56
 * @Description: 创建切面，方式二：创建切面注解
 * 此切面的作用：用于记录日志，可以加在类上和方法上，如果加在类上表示该类中的每个方法都会被切入
 * 用法：在类或者方法上加：@CustomAopAnnotation(param = "id", detail = "加在方法上")
 *
 * @Documented注解 功能：指明修饰的注解，可以被例如javadoc此类的工具文档化，只负责标记，没有成员取值。
 *
 * @Target 注解
 *  功能：指明了修饰的这个注解的使用范围，即被描述的注解可以用在哪里。
 *  ElementType的取值包含以下几种：
 *       TYPE:类，接口或者枚举
 *       FIELD:域，包含枚举常量
 *       METHOD:方法
 *       PARAMETER:参数
 *       CONSTRUCTOR:构造方法
 *       LOCAL_VARIABLE:局部变量
 *       ANNOTATION_TYPE:注解类型
 *       PACKAGE:包
 *
 *  @Retention注解 功能：指明修饰的注解的生存周期，即会保留到哪个阶段。
 *  RetentionPolicy的取值包含以下三种：
 *       SOURCE：源码级别保留，编译后即丢弃。
 *       CLASS：编译级别保留，编译后的class文件中存在，在jvm运行时丢弃，这是默认值。
 *       RUNTIME：运行级别保留，编译后的class文件中存在，在jvm运行时保留，可以被反射调用。
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD}) // 此注解可以加在类上，方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLogAnnotation {
    // 属性根据业务需求添加，这里只是举例
    String param() default "";
    String detail() default "";
}
