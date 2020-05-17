package com.custom.connect;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

/**
 * DATE 2020-05-17
 * 根据请求量，模拟业务处理时间。
 * 单线程版本
 */
@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public static final ChannelHandler INSTANCE = new ServerHandler();

    /**
     * 主线程处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBuf data = Unpooled.directBuffer();
        data.writeBytes(msg);
        // 模拟业务处理
        Object result = getResult(data);
        // 写回客户端
        ctx.channel().writeAndFlush(result);
    }

    protected Object getResult(ByteBuf data) {
        int leval = ThreadLocalRandom.current().nextInt(1, 1000);

        // 计算每次响应需要的时间
        int time;
        if (leval <= 900) {
            time = 1;
        } else if (leval <= 950) {
            time = 10;
        } else if (leval <= 990) {
            time = 100;
        } else {
            time = 1000;
        }
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            log.error("getResult, error", e);
        }
        return data;
    }
}
