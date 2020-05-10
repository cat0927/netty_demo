package com.custom.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * DATE 2020-05-08
 *
 */
public class ByteBufPooled {

    public static void main(String[] args) {
        final byte[] bytes = new byte[1024];
        int loop = 1800000;
        long startTime = System.currentTimeMillis();
        ByteBuf byteBuf = null;
        for (int i = 0; i< loop; i++) {
            byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
            byteBuf.writeBytes(bytes);
            byteBuf.release();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("内存分配耗时" + (endTime - startTime) + "ms");

    }
}
