//package com.asset.jupiter;
//
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//
//import java.util.logging.Logger;
//
//@Aspect
//@EnableAspectJAutoProxy
//@Configuration
//public class MethodExecutionCalculationAspect {
//    @Around("@annotation(com.asset.jupiter.TimeTrack)")
//    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
//        long startTime = System.currentTimeMillis();
//        // execute Method
//        joinPoint.proceed();
//        long timeTaken = System.currentTimeMillis() - startTime;
////        logger.info("Time Taken by {} is {}", joinPoint, timeTaken);
//        System.out.println("Time Taken by {} is {}" + "    " + joinPoint + "    " + timeTaken);
//    }
//}