package com.scj.act;

import javafx.application.Application;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ActTestMain1 {

    public static void main(String[] args) {
        ApplicationContext context =new ClassPathXmlApplicationContext("act/spring-activiti.xml");
        ProcessEngine processEngine =context.getBean(ProcessEngine.class);
        RepositoryService repositoryService = context.getBean(RepositoryService.class);
        repositoryService.createDeployment().name("test.bar").addClasspathResource("act/onboarding.bpmn20.xml").deploy();

    }
}
