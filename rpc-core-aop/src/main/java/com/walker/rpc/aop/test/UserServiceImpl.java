package com.walker.rpc.aop.test;

import org.springframework.stereotype.Service;

/**
 * @author fcwalker
 * @date 2020/12/17 18:04
 **/
@Service
public class UserServiceImpl implements UserService {
    @Override
    public void findOne(String key) {
        System.out.println(key);
    }
}
