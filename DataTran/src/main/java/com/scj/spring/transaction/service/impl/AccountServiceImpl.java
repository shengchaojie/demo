package com.scj.spring.transaction.service.impl;

import com.scj.spring.transaction.dao.AccountEntityMapper;
import com.scj.spring.transaction.entity.AccountEntity;
import com.scj.spring.transaction.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Shengchaojie on 2016/6/2.
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService{

    @Resource
    private AccountEntityMapper accountEntityMapper;

    @Resource(name="accountService2")
    private AccountService accountService;

    @Transactional
    public void addUser(String username, String password) {
        AccountEntity accountEntity =new AccountEntity();
        accountEntity.setLoginName(username);
        accountEntity.setPassword(password);

        accountEntityMapper.insertSelective(accountEntity);

        addUser1(username,password);

        //throw  new RuntimeException("123");
    }

    @Transactional
    public synchronized void  addUser1(String username, String password) {
        AccountEntity accountEntity =new AccountEntity();
        accountEntity.setLoginName(username+"test");
        accountEntity.setPassword(password);

        accountEntityMapper.insertSelective(accountEntity);

        throw new RuntimeException();
    }
}
