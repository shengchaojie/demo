package com.scj.mq;

import javax.jms.*;

/**
 * Created by Shengchaojie on 2016/5/30.
 */

public class Publisher {
    private Session session;
    private Destination destination;

    private MessageProducer producer;

    public Publisher()
    {
        try {
            session =JMSContext.getInstance().getConnection().createSession(true,Session.CLIENT_ACKNOWLEDGE);
            destination =session.createQueue("scjtest");

            producer =session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        }catch (JMSException e)
        {
            e.printStackTrace();
        }

    }

    public void close()
    {
        try {
            producer.close();
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Publisher publisher =new Publisher();
        for (int i=0;i<100;i++)
        {
            String msg ="第"+i+"次发送消息";
            try {
                TextMessage textMessage = publisher.getSession().createTextMessage(msg);
                publisher.getProducer().send(textMessage);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        publisher.close();

        System.exit(0);

    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public MessageProducer getProducer() {
        return producer;
    }

    public void setProducer(MessageProducer producer) {
        this.producer = producer;
    }
}
