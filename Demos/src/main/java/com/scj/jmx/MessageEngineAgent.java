package com.scj.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * how to run
 * in classpath
 * java -Dcom.sun.management.jmxremote.port=9999  -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false com.scj.jmx.Echo
 *  https://my.oschina.net/xpbug/blog/221547
 */
public class MessageEngineAgent {
    public void start() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName mxbeanName = new ObjectName("com.example:type=MessageEngine");
            MessageEngineMXBean mxbean = new MessageEngine();
            mbs.registerMBean(mxbean, mxbeanName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
