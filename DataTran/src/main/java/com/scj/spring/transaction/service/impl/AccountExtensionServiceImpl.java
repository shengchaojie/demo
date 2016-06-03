package com.scj.spring.transaction.service.impl;

import com.scj.spring.transaction.dao.AccountEntityMapper;
import com.scj.spring.transaction.entity.AccountEntity;
import com.scj.spring.transaction.service.AccountService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Shengchaojie on 2016/6/2.
 */

@Service("accountService2")
public class AccountExtensionServiceImpl implements AccountService,ApplicationContextAware{

    private ApplicationContext context;

    @Resource
    private AccountEntityMapper accountEntityMapper;


    @Transactional(propagation =Propagation.REQUIRED)
    public void addUser(String username, String password) {
        AccountEntity accountEntity =new AccountEntity();
        accountEntity.setLoginName(username);
        accountEntity.setPassword(password);

        accountEntityMapper.insertSelective(accountEntity);

        addUser1(username,password);


        throw  new RuntimeException("123");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addUser1(String username, String password) {
        AccountEntity accountEntity =new AccountEntity();
        accountEntity.setLoginName(username+"test");
        accountEntity.setPassword(password);

        accountEntityMapper.insertSelective(accountEntity);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=context;
    }
}
