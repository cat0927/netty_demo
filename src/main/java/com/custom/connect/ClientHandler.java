package com.custom.connect;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * DATE 2020-05-17
 *  记录 QPS、平响
 */
@Slf4j
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	public static final ChannelHandler INSTANCE = new ClientHandler();

	private static AtomicLong beginTime = new AtomicLong(0);
	// 总响应时间
	private static AtomicLong totalResponseTime = new AtomicLong(0);
	// 总请求量
	private static AtomicInteger totalRequest = new AtomicInteger(0);

	private static final Thread THREAD = new Thread(() -> {
        try {
            while (true) {
                long duration = System.currentTimeMillis() - beginTime.get();
                if (duration != 0) {

                    //System.out.println("QPS: [" + 1000 * totalRequest.get() / duration + "],平均响应时间: "
//        + "[" + (float) totalResponseTime.get() / totalRequest.get() + "] ms");

                    log.info("QPS:[{}], 平均响应时间:[{}] ms", 1000 * totalRequest.get() / duration,
(float) totalResponseTime.get() / totalRequest.get() );

                    Thread.sleep(2000);
                }
            }
        } catch (Exception e) {
log.error("thread error", e);
        }
    });


	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		ctx.executor().scheduleAtFixedRate(() -> {
            ByteBuf byteBuf = ctx.alloc().ioBuffer();
            // 发送系统时间
            byteBuf.writeLong(System.currentTimeMillis());
            ctx.channel().writeAndFlush(byteBuf);
        }, 0, 1, TimeUnit.SECONDS);
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// 获取本次响应时间
	    totalResponseTime.addAndGet(System.currentTimeMillis() - msg.readLong());
        // 请求量
		totalRequest.incrementAndGet();
		if (beginTime.compareAndSet(0, System.currentTimeMillis())) {
			THREAD.start();
		}
	}
}
