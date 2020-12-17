package com.walker.rpc.aop;

import com.walker.rpc.aop.annotation.RpcScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@RpcScan("com.walker.rpc.aop.test")
public class RpcAopApp {
    public static void main(String[] args) {
        SpringApplication.run(RpcAopApp.class);
    }
}
