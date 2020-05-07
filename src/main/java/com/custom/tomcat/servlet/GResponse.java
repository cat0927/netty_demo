package com.custom.tomcat.servlet;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;

/**
 * DATE 2020-05-07
 *
 */
@Slf4j
public class GResponse {

    private OutputStream out;

    public GResponse(OutputStream out) {
        this.out = out;
    }

    public void write(String s) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html; \n")
                .append("\r\n").append(s);

        out.write(sb.toString().getBytes());
    }


    /********************** Netty ************************/

    private ChannelHandlerContext ctx;
    private HttpRequest req;

    public GResponse(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }

    public void writeNetty(String out) throws Exception {
        try {
            if (out == null || out.length() == 0) {
                return;
            }

            DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(out.getBytes("UTF-8"))
            );

            response.headers().set("Content-Type", "text/html");
            ctx.write(response);
        } catch (Exception e) {
            log.error("write error", e);
        } finally {
            ctx.flush();
            ctx.close();
        }
    }
}
