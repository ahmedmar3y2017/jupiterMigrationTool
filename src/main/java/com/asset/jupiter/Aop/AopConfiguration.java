package com.asset.jupiter.Aop;

import com.asset.jupiter.JUPITER.Model.Entities.Indexclass;
import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.JUPITER.Model.Entities.Item;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


@Configuration
@EnableAspectJAutoProxy
@Aspect
public class AopConfiguration {

    @Pointcut("execution(@com.asset.jupiter.Aop.TimeTrack * com.asset.jupiter.JUPITER.jupiterService.dynamicService.insertDynamicTableInfo(..))")
    public void insertdynamicTablePointCut() {
    }

    @Pointcut("execution(@com.asset.jupiter.Aop.TimeTrack * com.asset.jupiter.JUPITER.jupiterService.dynamicService.getDynamicTableInfo(..))")
    public void getdynamicTablePointCut() {
    }

    //        @Before(value = "dynamicTablePointCut()")
//    public void beforeMethodStatistics(JoinPoint jp) {
//        System.out.println("before Interceptor " + jp);
//
//    }
    //    @After("monitor()")
//    public void afterMethodStatistics(JoinPoint jp) {
//        System.out.println("after Interceptor " + jp);
//
//    }
//*******************************************************
//    @Around(value = "insertdynamicTablePointCut()")
//    public boolean aroundinsertdynamicTablePointCut(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        Object proceed = calculateMethodTime(joinPoint);
//
//        return (boolean) proceed;
//    }
//
//    @Around(value = "getdynamicTablePointCut()")
//    public ArrayList<String> aroundgetdynamicTablePointCut(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        Object proceed = calculateMethodTime(joinPoint);
//
//        return (ArrayList<String>) proceed;
//    }
//
//    // method to calculate Method Time execution
//    Object calculateMethodTime(ProceedingJoinPoint joinPoint) throws Throwable {
//        long startTime = System.currentTimeMillis();
//
//        // execute Method
//        Object proceed = joinPoint.proceed();
//        long timeTaken = System.currentTimeMillis() - startTime;
////        logger.info("Time Taken by {} is {}", joinPoint, timeTaken);
//        System.out.println("Method Name : " + joinPoint.getSignature().getName() + " \n  Time Taken by " + timeTaken);
//
//        return proceed;
//
//    }
    //******************************************************


//    @Bean
//    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
//        return new PerformanceMonitorInterceptor(true);
//    }
//
//    @Bean
//    public Advisor performanceMonitorAdvisor() {
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression("com.asset.jupiter.Aop.AopConfiguration.monitor()");
//        return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
//    }

//    @Bean
//    public Person person(){
//        return new Person("John","Smith", LocalDate.of(1980, Month.JANUARY, 12));
//    }
//
//    @Bean
//    public PersonService personService(){
//        return new PersonService();
//    }
}
