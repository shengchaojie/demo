package com.scj.guava;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

/**
 * Created by shengchaojie on 2017/5/10.
 */
public class StringTest {
    public static void main(String[] args) {
        //trim 只会去除最前面和最后面的连续的字符
        System.out.println(Splitter.on(',').trimResults(CharMatcher.is('_')).split(" _a, _b_ ,c__"));
        System.out.println(CharMatcher.digit().replaceFrom("777gfgg88","*"));
    }
}
