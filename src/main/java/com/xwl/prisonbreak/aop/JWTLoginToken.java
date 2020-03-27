package com.xwl.prisonbreak.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xwl
 * @date 2020-03-27 13:39
 * @description 作用：如果请求是登录操作，在用户登录的方法上增加@LoginToken注解，拦截器就会放行
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
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JWTLoginToken {
    boolean required() default true;
}
