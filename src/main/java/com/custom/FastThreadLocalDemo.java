package com.custom;


import io.netty.util.concurrent.FastThreadLocal;

/**
 * DATE 2020-05-16
 *
 */
public class FastThreadLocalDemo extends FastThreadLocal<Object> {

    @Override
    protected Object initialValue() throws Exception {
        return new Object();
    }

}
