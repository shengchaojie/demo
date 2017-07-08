package com.scj.mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * Created by Shengchaojie on 2016/5/30.
 */

public class JMSContext {
    private static volatile JMSContext instance =null;

    private Connection connection;

    private final String url="tcp://192.168.170.131:61616";

    private JMSContext()
    {
        ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, url);
        try {
            connection = factory.createConnection();
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static JMSContext getInstance()
    {
        if(instance==null)
        {
            synchronized (JMSContext.class)
            {
                if(instance==null)
                {
                    instance =new JMSContext();
                }
            }
        }
        return instance;
    }
}
