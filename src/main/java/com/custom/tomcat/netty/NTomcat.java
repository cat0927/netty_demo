package com.custom.tomcat.netty;

import com.custom.tomcat.servlet.GRequest;
import com.custom.tomcat.servlet.GResponse;
import com.custom.tomcat.servlet.GServlet;
import com.google.common.collect.Maps;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Properties;

/**
 * DATE 2020-05-07
 * Netty 版Tomcat
 */
@Slf4j
public class NTomcat {

    private int prot = 8080;
    private ServerSocket server;
    private Map<String, GServlet> servletMap = Maps.newHashMap();

    private Properties webXml = new Properties();

    /**
     * 加载配置
     */
    private void init() {
        try {
            String WEB_INF = this.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(WEB_INF + "web.properties");
            webXml.load(fis);
            for (Object k: webXml.keySet()) {
                String key = k.toString();
                if (key.endsWith(".url")) {
                    String servletName = key.replaceAll("\\.url$","");
                    String url = webXml.getProperty(key);
                    String className = webXml.getProperty(servletName + ".className");

                    GServlet obj =  (GServlet)Class.forName(className).newInstance();
                    servletMap.put(url, obj);
                }
            }
        } catch (Exception e) {
            log.error("init, error", e);
        }
    }

    public void start() {
        init();

        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();

            // 配置参数
            server.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());
                            socketChannel.pipeline().addLast(new HttpRequestDecoder());
                            // 业务逻辑
                            socketChannel.pipeline().addLast(new GTomcatHandler());
                        }
                    })
                    // 对主线程的分配，最大线程数 128
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture sync = server.bind(prot).sync();
            System.out.println("NTomcat 启动，监听端口：" + prot);
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("start error", e);
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public class GTomcatHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpRequest) {
                System.out.println("channelRead.....");

                HttpRequest req = (HttpRequest)msg;

                GRequest request = new GRequest(ctx, req);
                GResponse response = new GResponse(ctx, req);

                String url = request.getNettyUrl();
                if (servletMap.containsKey(url)) {
                    servletMap.get(url).service(request, response);
                } else {
                    response.writeNetty("404- Not Found");
                }
            }
        }
    }
}
