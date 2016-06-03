package com.scj.spring.mq;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.MapMessage;

/**
 * Created by Shengchaojie on 2016/6/2.
 */

public class Receiver {
    private JmsTemplate jmsTemplate;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Receiver receiver = (Receiver) context.getBean("receiver");
        receiver.receiveMessage();
    }

    public void receiveMessage() {
        String name;
        Integer age;

        while (true) {
            MapMessage message = (MapMessage) jmsTemplate.receive();

            try {
                if (null != message) {

                    name = message.getString("name");
                    age = message.getInt("age");

                    System.out.println(name + " " + age);

                    Thread.sleep(1000);
                } else {
                    break;
                }
            } catch (JMSException ex) {
            } catch (InterruptedException ex) {
            }
        }

    }


}
