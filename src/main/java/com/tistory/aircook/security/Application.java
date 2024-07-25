package com.tistory.aircook.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);

        printBeanInformation(context);
    }

    private static void printBeanInformation(ApplicationContext context){

        //스프링 빈 정보
        log.info("context bean information --------------------------------------------------------------");
        int i = 1;
        String[] beanNames = context.getBeanDefinitionNames();
        for (String name : beanNames) {
            log.info("[{}] {}: {}", i++ ,name, context.getBean(name).getClass().getCanonicalName());
        }

    }

}
