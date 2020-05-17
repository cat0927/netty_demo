package com.custom.connect;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * DATE 2020-05-17
 * 客户端
 */
@Slf4j
public class Client {

    private static final String SERVER_HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        //new Client().start(Server.BEGIN_PORT, Server.N_PORT);

        new Client().start(8080);
    }

    /**
     * 监听单个端口。循环发起请求。
     * @param port
     * @throws Exception
     */
    public void start(int port) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FixedLengthFrameDecoder(Long.BYTES));
                        ch.pipeline().addLast(ClientHandler.INSTANCE);

                    }
                });
        // 模拟客户端向服务器发起 1000个请求。
        for (int i = 0; i< 1000; i++) {
            bootstrap.connect(SERVER_HOST, port).get();
        }
    }


    /**
     * 模拟，监听服务端多个端口
     * @param beginPort
     * @param nPort
     */
    public void start(final int beginPort, int nPort) {
        log.info("客户端已启动。。。");

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

            }
        });

        int index = 0;
        int port;
        while (!Thread.interrupted()) {
            port = beginPort + index;
            try {
                log.info("port:[{}]", port);
                ChannelFuture channelFuture = bootstrap.connect(SERVER_HOST, port);
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            log.error("连接失败，程序关闭");
                            System.exit(0);
                        }
                    }
                });
                channelFuture.get();
            } catch (Exception e) {
                log.error("connect error", e);
            }
            if (port == nPort) {
                index = 0;
            } else {
                index ++;
            }
        }
    }

}
