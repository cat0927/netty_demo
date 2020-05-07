/**
 * DATE 2020-05-08
 *
 */
package com.custom.rpc;

/*
 * 先启动 RpcRegistry (Service)
 * 扫描 provider 包,实现类。
 *
 * RpcConsumer (作为Client )
 * 调用 Service -》 Service 利用反射原理，通过类名、方法名.调用方法
 *
 *
 * 注意：
 * unsupported message type: InvokerProtocol (expected: ByteBuf, FileRegion)
 *
 *  自定义协议，要实现序列化
 *  InvokerProtocol implements Serializable
 */


