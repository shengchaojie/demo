package com.scj.guava;

import com.google.common.cache.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by shengchaojie on 2017/5/10.
 */
public class CacheTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(1)
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        return "scjtest";
                    }
                });
        loadingCache.put("scj", "scj2222");
        System.out.println(loadingCache.asMap());
        loadingCache.put("scj2", "scj2222");
        System.out.println(loadingCache.asMap());
        System.out.println(loadingCache.get("scj"));
        System.out.println(loadingCache.asMap());
        System.out.println(loadingCache.get("scj2"));
        System.out.println(loadingCache.asMap());
        System.out.println(loadingCache.size());
        Thread.sleep(5000);
        System.out.println(loadingCache.get("scj"));*/

        RemovalListener<String,String> removalListener =new RemovalListener<String, String>() {
            @Override
            public void onRemoval(RemovalNotification<String, String> removalNotification) {
                System.out.println("remove"+removalNotification.getKey());
            }
        };
        //eviction 
        LoadingCache<String, String> loadingCache2 = CacheBuilder.newBuilder()
                .maximumWeight(4)
                .weigher(new Weigher<String, String>() {
                    @Override
                    public int weigh(String s, String s2) {
                        if("scj".equals(s)){
                            return 2;
                        }else {
                            return 3;
                        }
                    }
                })
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .removalListener(removalListener)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        return "scjtest";
                    }
                });
        loadingCache2.put("scj", "scj2222");
        System.out.println(loadingCache2.asMap());
        System.out.println(loadingCache2.size());
        System.out.println(loadingCache2.get("scj"));
        loadingCache2.put("scj2", "scj2222");
        System.out.println(loadingCache2.asMap());
        System.out.println(loadingCache2.size());
        System.out.println(loadingCache2.get("scj3"));

        System.out.println(loadingCache2.stats().hitRate());

    }
}
