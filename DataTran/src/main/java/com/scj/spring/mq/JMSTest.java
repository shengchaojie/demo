package com.scj.spring.mq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Shengchaojie on 2016/6/2.
 */

public class JMSTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext =new ClassPathXmlApplicationContext("applicationContext.xml");

        ProxyJMSConsumer consumer =(ProxyJMSConsumer)applicationContext.getBean("messageReceiver");
        consumer.recieve();
    }
}
