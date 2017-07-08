package com.scj.cruator;

import com.scj.concurrencyutil.WaitTest;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.imps.CuratorFrameworkState;
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
        CuratorListener curatorListener =new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println(curatorEvent.getType());
            }


        };
        CuratorFramework client =CuratorFrameworkFactory.newClient(connectString,retryPolicy);
        client.getCuratorListenable().addListener(curatorListener);
        client.start();

        System.out.println(new String(client.getData().watched().forPath("/scj")));
        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/scj234","3333".getBytes());
        client.setData().inBackground().forPath("/scj","7758".getBytes());

        while (client.getState() == CuratorFrameworkState.STARTED){
            Thread.sleep(100);
        }
    }

}
