package com.scj.spring.mq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;

/**
 * Created by Shengchaojie on 2016/5/31.
 */

public class ProxyJMSConsumer {

    public ProxyJMSConsumer() {
    }

    private JmsTemplate jmsTemplate;

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void recieve() {
        ApplicationContext context =new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        Destination destination =(Destination)context.getBean("destination");

        while (true)
        {
            try {
                TextMessage textMessage =(TextMessage) jmsTemplate.receive(destination);
                if(null!=textMessage)
                {
                    System.out.println("消费消息:"+textMessage.getText()+ " 时间:"+System.currentTimeMillis());
                }else
                {
                    break;
                }
                Thread.sleep(1000);
            }catch (JMSException e)
            {

            }catch (InterruptedException e)
            {

            }
        }
    }
}
