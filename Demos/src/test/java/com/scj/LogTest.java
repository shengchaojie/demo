package com.scj;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shengchaojie on 2016/6/2.
 */

public class LogTest {
    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void test1()
    {
        logger.debug("Test the MessageFormat for {} to {} endTo {}", 1,2,3);
        logger.info("Test the MessageFormat for {} to {} endTo {}", 1,2,3);
        logger.error("Test the MessageFormat for {} to {} endTo {}", 1,2,3);

        try{
            throw new IllegalStateException("try to throw an Exception");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
    }
}
