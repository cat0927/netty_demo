package com.custom.msg.coder;

import com.custom.msg.IMMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * DATE 2020-05-16
 * 自定义 IMP 编码器
 */
public class IMEncoder extends MessageToByteEncoder<IMMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, IMMessage msg, ByteBuf out) throws Exception {
        out.writeBytes(new MessagePack().write(msg));
    }
}
