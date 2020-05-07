package com.custom.nio;

import java.nio.ByteBuffer;

/**
 * DATE 2020-05-06
 * 缓冲区分片
 */
public class BufferSlice {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i=0; i< buffer.capacity(); i++) {
            buffer.put((byte)i);
        }

        // 创建子缓冲区
        buffer.position(3);
        buffer.limit(7);

        ByteBuffer slice = buffer.slice();

        for (int i=0; i< slice.capacity(); ++i) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i ,b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());
        while (buffer.remaining() > 0) {
            System.out.println(buffer.get());
        }
    }

}
