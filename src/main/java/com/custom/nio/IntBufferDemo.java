package com.custom.nio;

import java.nio.IntBuffer;

/**
 * DATE 2020-05-06
 *
 */
public class IntBufferDemo {

    public static void main(String[] args) {


        IntBuffer allocate = IntBuffer.allocate(8);
        for (int i = 0; i< allocate.capacity(); i++) {
            int j = 2 * (i + 1);
            allocate.put(j);
        }
        allocate.flip();

        while (allocate.hasRemaining()) {
            int j = allocate.get();
            System.out.print(j + " ");
        }
    }
}
