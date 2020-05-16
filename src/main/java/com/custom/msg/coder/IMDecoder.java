package com.custom.msg.coder;

import com.custom.msg.IMMessage;
import com.custom.msg.IMP;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.util.Strings;
import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;

import java.util.List;
import java.util.regex.Pattern;

/**
 * DATE 2020-05-16
 * 自定义解码
 */
public class IMDecoder extends ByteToMessageDecoder {

    private Pattern pattern = Pattern.compile("^\\[(.*)](\\s-\\s(.*))?");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            final int length = in.readableBytes();
            final byte[] array = new byte[length];
            String content = new String(array,in.readerIndex(), length);
            if (Strings.isNotBlank(content)) {
                if (!IMP.isIMP(content)) {
                    ctx.channel().pipeline().remove(this);
                    return;
                }
            }

            in.getBytes(in.readerIndex(), array, 0, length);
            out.add(new MessagePack().read(array, IMMessage.class));
            in.clear();
        } catch (MessageTypeException e) {
            ctx.channel().pipeline().remove(this);
        }
    }
}
