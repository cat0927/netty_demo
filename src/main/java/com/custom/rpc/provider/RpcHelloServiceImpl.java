package com.custom.rpc.provider;

import com.custom.rpc.IRpcHelloService;

/**
 * DATE 2020-05-07
 *
 */
public class RpcHelloServiceImpl implements IRpcHelloService {
    @Override
    public String hello(String name) {
        return "Hello" + name + "!";
    }
}
