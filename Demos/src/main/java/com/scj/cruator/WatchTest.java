package com.scj.cruator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;

import java.util.List;

/**
 * @author 10064749
 * @description ${DESCRIPTION}
 * @create 2018-06-20 18:19
 */
public class WatchTest {

    public static void main(String[] args) throws Exception {
        String connectString ="127.0.0.1:2181";
        RetryPolicy retryPolicy =new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString,retryPolicy);
        client.start();
        List<String> result = client.getChildren().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent watchedEvent) throws Exception {
                System.out.println(watchedEvent.getPath());
                client.getChildren().usingWatcher(this).forPath("/test");
            }
        }).forPath("/test");
        result.stream().forEach(a->{
            System.out.println(a);
        });
        client.getData().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent watchedEvent) throws Exception {
                System.out.println(watchedEvent.getPath());
                client.getChildren().usingWatcher(this).forPath("/test");
            }
        }).forPath("/test");

        while (client.getState() == CuratorFrameworkState.STARTED){
            Thread.sleep(100);
        }
    }
}
