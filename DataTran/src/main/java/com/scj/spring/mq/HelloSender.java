package com.scj.spring.mq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by Shengchaojie on 2016/5/31.
 */

public class HelloSender {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext(new String[]{"classpath:applicationContext.xml"});

        JmsTemplate template =(JmsTemplate)context.getBean("jmsTemplate");
        Destination destination =(Destination)context.getBean("destination");

        template.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("发送消息测试");
            }
        });
        System.out.println("发送了一条信息");
        System.exit(0);
    }
}
