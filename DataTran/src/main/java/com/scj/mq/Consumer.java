package com.scj.mq;

import javax.jms.*;

/**
 * Created by Shengchaojie on 2016/5/30.
 */

public class Consumer {
    private Session session;
    private Destination destination;

    private MessageConsumer consumer;

    public Consumer()
    {
        try {
            session =JMSContext.getInstance().getConnection().createSession(true,Session.AUTO_ACKNOWLEDGE);
            destination =session.createQueue("scjtest");
            consumer =session.createConsumer(destination);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Consumer consumer =new Consumer();
        while (true)
        {
            try {
                TextMessage textMessage =(TextMessage) consumer.getConsumer().receive();
                if(textMessage!=null&&!textMessage.getJMSRedelivered())
                {
                    System.out.println(textMessage.getText());
                    //textMessage.acknowledge();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public MessageConsumer getConsumer() {
        return consumer;
    }
}
