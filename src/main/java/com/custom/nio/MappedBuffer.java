package com.custom.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * DATE 2020-05-06
 * 内存映射
 */
public class MappedBuffer {

    static private final int start = 0;
    static private final int size = 1024;

    public static void main(String[] args) throws Exception {
        RandomAccessFile rw = new RandomAccessFile("./test.txt", "rw");
        FileChannel channel = rw.getChannel();

        /*
         * 把缓存区跟文件系统进行一个映射关联
         * 只要操作缓冲区里面的内容，文件内容会跟着变化。
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, start, size);

        map.put(0, (byte) 97);
        map.put(1023, (byte) 122);

        rw.close();

    }
}
