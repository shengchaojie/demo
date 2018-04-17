package com.scj.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;

import java.util.concurrent.Future;

public class CommandHelloWorld extends HystrixCommand<String>{

    private final String name;

    public CommandHelloWorld(String name){
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name =name;
    }

    @Override
    protected String run() throws Exception {
        return "Hello"+name+"!";
    }

    public static void main(String[] args) {
        String s = new CommandHelloWorld("scj").execute();
        Future<String> s2 = new CommandHelloWorld("Bob").queue();
        Observable<String> s3 = new CommandHelloWorld("Bob").observe();
    }
}
