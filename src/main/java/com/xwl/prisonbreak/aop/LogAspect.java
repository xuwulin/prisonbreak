package com.xwl.prisonbreak.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @Auther: xwl
 * @Date: 2019/6/19 17:44
 * @Description: 创建切面，方式一：直接使用切面
 *  新建一个日志切面类，假设我们需要一个类来打印进入方法或方法执行后需要打印的日志。
 */
// @Aspect注解：表明这是是一个切面类
//@Aspect
// @Component注解：表示将当前类注入到Spring容器内
//@Component
public class LogAspect {
    /**
     * @Pointcut 切入点，其中execution用于使用切面的连接点。
     * 使用方法：execution(方法修饰符(可选) 返回类型 方法名 参数 异常模式(可选)) ，可以使用通配符匹配字符，*可以匹配任意字符
     * @Pointcut("execution(public * com.xwl.prisonbreak.michael.controller.*.*(..))")：
     * 第一个 * 表示任意类型的返回值
     * 第二个 * 表示controller包下的任意类
     * 第三个 * 表示该类中的任意方法
     * (..)表示任意参数
     */
    @Pointcut("execution(public * com.xwl.prisonbreak.michael.controller.*.*(..))")
    public void LogAspect(){}

    /**
     * @Before 在方法前执行
     * @param joinPoint JoinPoint包含了类名，被切面的方法名，参数等信息。
     */
    @Before("LogAspect()")
    public void doBefore(JoinPoint joinPoint){
        System.out.println("doBefore");
    }

    /**
     * @After 在方法后执行
     * @param joinPoint JoinPoint包含了类名，被切面的方法名，参数等信息。
     */
    @After("LogAspect()")
    public void doAfter(JoinPoint joinPoint){
        System.out.println("doAfter");
    }

    /**
     * @AfterReturning 在方法执行后返回一个结果后执行
     * @param joinPoint JoinPoint包含了类名，被切面的方法名，参数等信息。
     */
    @AfterReturning("LogAspect()")
    public void doAfterReturning(JoinPoint joinPoint){
        System.out.println("doAfterReturning");
    }

    /**
     * @AfterThrowing 在方法执行过程中抛出异常的时候执行
     * @param joinPoint JoinPoint包含了类名，被切面的方法名，参数等信息。
     */
    @AfterThrowing("LogAspect()")
    public void deAfterThrowing(JoinPoint joinPoint){
        System.out.println("deAfterThrowing");
    }

    /**
     * @Around 环绕通知，就是可以在执行前后都使用，
     * 这个方法参数必须为ProceedingJoinPoint，
     * proceed()方法就是被切面的方法，
     * 上面四个方法可以使用JoinPoint，JoinPoint包含了类名，被切面的方法名，参数等信息。
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("LogAspect()")
    public Object deAround(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("doAround");
        return joinPoint.proceed();
    }
}
