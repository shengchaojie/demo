package com.scj.cruator;

import com.scj.concurrencyutil.WaitTest;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.proto.GetChildren2Request;

/**
 * Created by shengchaojie on 2017/5/12.
 */
public class BasicTest {
    public static void main(String[] args) throws Exception {
        String connectString ="59.110.172.206:2181";
        RetryPolicy retryPolicy =new ExponentialBackoffRetry(1000,3);
        CuratorFramework client =CuratorFrameworkFactory.newClient(connectString,retryPolicy);
        client.start();

        System.out.println(new String(client.getData().forPath("/scj")));
        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/scj234","3333".getBytes());
        //client.setData().forPath("/scj234","7758".getBytes());

        synchronized (BasicTest.class){
            BasicTest.class.wait();
        }
    }

}
