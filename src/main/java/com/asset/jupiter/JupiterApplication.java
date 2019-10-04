package com.asset.jupiter;

import com.asset.jupiter.JUPITER.jupiterService.*;

import com.asset.jupiter.Util.CrunchifyGetIPHostname;
import com.asset.jupiter.Util.configurationService.config;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.excelModel.ExcelModel;
import com.asset.jupiter.Util.excelModel.RowModel;
import com.asset.jupiter.Util.exceptions.JupiterException;
import com.asset.jupiter.Util.handlers.ExcelHandler;
import com.asset.jupiter.Util.handlers.JsonHandler;
import com.asset.jupiter.Util.jsonModel.JsonCreateModel;
import com.asset.jupiter.Util.jsonModel.JsonUpdateModel;
import com.asset.jupiter.Util.logging.Log;
import com.asset.jupiter.Util.operations.DocumentHandler;
import org.modelmapper.ModelMapper;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

//@EnableCaching
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication

public class JupiterApplication {

    @Autowired
    static config config;
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
//        SpringApplication.run(JupiterApplication.class, args);
        SpringApplication sa = new SpringApplication();
//        sa.addListeners(new MyApplicationListener());
        sa.setSources(new HashSet(Arrays.asList(JupiterApplication.class)));
        ConfigurableApplicationContext context = sa.run(args);

//        applicationContext = SpringApplication.run(JupiterApplication.class, args);
//        displayAllBeans();

    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }



}

