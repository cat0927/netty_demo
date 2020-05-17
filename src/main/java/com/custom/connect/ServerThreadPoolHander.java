package com.custom.connect;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * DATE 2020-05-17
 * 多线程版本。
 */
@ChannelHandler.Sharable
public class ServerThreadPoolHander extends ServerHandler{
    public static final ChannelHandler INSTANCE = new ServerThreadPoolHander();

    private static ExecutorService threadPool = Executors.newFixedThreadPool(1000);

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, ByteBuf msg) {
        final ByteBuf data = Unpooled.buffer();
        data.writeBytes(msg);

        threadPool.submit(() -> {
            Object result =  getResult(data);
            ctx.channel().writeAndFlush(result);
        });
    }
}
