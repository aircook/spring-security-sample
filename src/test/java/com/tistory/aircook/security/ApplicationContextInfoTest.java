package com.tistory.aircook.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = Application.class)
public class ApplicationContextInfoTest {

    @Autowired
    ApplicationContext context;

    @Test
    public void contextCheck() throws Exception {

        if(context != null) {
            String[] beans = context.getBeanDefinitionNames();

            for (String bean: beans) {
                System.out.println("bean: " + bean);
            }
        }

    }

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean(){
        String[] beanDefinitionNames= context.getBeanDefinitionNames();

        int i = 1;
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = context.getBean(beanDefinitionName);
            System.out.println("[" + (i++) + "] beanDefinitionName is [" + beanDefinitionName + "], object is ["+ bean + "]");

        }
    }


    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean(){

        // ApplicationContext를 ConfigurableApplicationContext로 캐스팅
        ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) context;

        // BeanDefinitionRegistry 얻기
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) configurableContext.getBeanFactory();

        // 모든 Bean 이름 가져오기
        String[] beanNames = registry.getBeanDefinitionNames();

        String[] beanDefinitionNames= registry.getBeanDefinitionNames();

        int i = 1;
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);//빈에 대한 메타데이터

            //Role ROLE_APPLICATION: 직접 등록한 애플리케이션 빈
            // Role ROLE_INFRASTRUCTURE: 스프링이 내부에서 사용하는 빈
            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) { //애플리케이션에서 등록한 빈
                Object bean = context.getBean(beanDefinitionName);
                System.out.println("[" + (i++) + "] beanDefinitionName is [" + beanDefinitionName + "], object is ["+ bean + "]");
            }
        }
    }

}
