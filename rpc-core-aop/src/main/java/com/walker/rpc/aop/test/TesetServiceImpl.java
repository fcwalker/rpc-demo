package com.walker.rpc.aop.test;

import com.walker.rpc.aop.annotation.RpcReference;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @author fcwalker
 * @date 2020/12/17 18:03
 **/
@Service
@Data
public class TesetServiceImpl implements TestService {
    @RpcReference(url = "http://127.0.0.1:8989")
    UserService userService;

    @Override
    public void hello(String name) {
        userService.findOne(name);
    }
}
