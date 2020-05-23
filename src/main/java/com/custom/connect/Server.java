package com.custom.connect;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * DATE 2020-05-17
 *
 */
@Slf4j
public class Server {
    public static final int BEGIN_PORT = 8080;
    public static final int N_PORT = 8081;

    public static void main(String[] args) {
        new Server().start(BEGIN_PORT, N_PORT);
    }

    public void start(int beginPort, int nPort) {
        log.info("服务端启动中。。。。");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        final EventLoopGroup businessGroup = new NioEventLoopGroup(1000);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);

        //bootstrap.childHandler(new ConnectionCountHandler());

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                // 自定义长度的解码器，每次发送一个long 类型的长度数据
                ch.pipeline().addLast(new FixedLengthFrameDecoder(Long.BYTES));

                // 使用 EventLoopGroup 专门处理耗时的 handl  r。
                //ch.pipeline().addLast(businessGroup, ServerHandler.INSTANCE);

                // 单线程处理
                //ch.pipeline().addLast(ServerHandler.INSTANCE);
                // 多线程处理
                ch.pipeline().addLast(ServerThreadPoolHander.INSTANCE);
            }
        });

        for (int i =0; i <(nPort - beginPort); i++) {
            final int port = beginPort + i;
            bootstrap.bind(port)
                    .addListener((ChannelFutureListener) future -> log.info("成功绑定监听端口: "+ port));
        }
        log.info("服务端已启动。。。");
    }
}
