package com.scj.spring.transaction;

import com.scj.spring.transaction.service.AccountService;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Shengchaojie on 2016/6/2.
 */

public class TestMain {
    public static void main(String[] args) {
        ApplicationContext context =new ClassPathXmlApplicationContext("classpath:transaction/transactionContext.xml");
        ((AbstractApplicationContext)context).registerShutdownHook();

        AccountService accountService =(AccountService)context.getBean("accountService");
        System.out.println("============================"+AopUtils.isJdkDynamicProxy(accountService));
        System.out.println("============================"+AopUtils.isCglibProxy(accountService));
        accountService.addUser("scj","123");
    }
}
