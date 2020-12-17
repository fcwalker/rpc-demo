package com.walker.rpc.aop;

import com.walker.rpc.aop.test.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RpcAopApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RpcAopAppTest
{

    @Autowired
    TestService service;

    @Test
    public void test()
    {
        service.hello("fc");
    }
}
