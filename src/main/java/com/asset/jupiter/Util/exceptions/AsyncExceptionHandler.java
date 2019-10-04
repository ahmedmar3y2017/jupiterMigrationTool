//package com.asset.jupiter.Util.exceptions;
//
//import com.asset.jupiter.Util.logging.Log;
//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
//
//import java.lang.reflect.Method;
//
//public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
//
//    @Override
//    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
//
//        Log.error("Exception Cause - " + throwable.getMessage() ,throwable);
//        Log.error("Method name - " + method.getName());
//        for (Object param : obj) {
//            Log.error("Parameter value - " + param);
//        }
//
//        try {
//            throw new JupiterException(throwable.getMessage() + "  At Function : " +method.getName() ,700);
//        } catch (JupiterException e) {
//            e.printStackTrace();
//        }
//    }
//}