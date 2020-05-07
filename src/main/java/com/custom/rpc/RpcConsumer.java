package com.custom.rpc;

/**
 * DATE 2020-05-08
 * 消费者
 *
 */
public class RpcConsumer {

    /**
     * 先启动，RpcRegistry.main
     * @param args
     */
    public static void main(String[] args) {
        IRpcHelloService rpcHelloService =  RpcProxy.create(IRpcHelloService.class);
        System.out.println(rpcHelloService.hello("老师"));

        IRpcService service =  RpcProxy.create(IRpcService.class);
        System.out.println("8 + 2 = " + service.add(8 ,2));
        System.out.println("8 - 2 = " + service.sub(8 ,2));
        System.out.println("8 * 2 = " + service.mult(8 ,2));

    }
}
