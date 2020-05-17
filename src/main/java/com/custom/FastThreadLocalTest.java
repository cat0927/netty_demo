package com.custom;

/**
 * DATE 2020-05-16
 *
 */
public class FastThreadLocalTest {

    private final FastThreadLocalDemo fastThreadLocalText;

    public FastThreadLocalTest( ) {
        fastThreadLocalText = new FastThreadLocalDemo();
    }

    public static void main(String[] args) {
        FastThreadLocalDemo fastThreadLocalDemo = new FastThreadLocalDemo();

        new Thread(() -> {
            Object o = fastThreadLocalDemo.get();
            for (int i=0; i< 10; i++) {
                fastThreadLocalDemo.set(new Object());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Object obj = fastThreadLocalDemo.get();
                for (int i=0; i< 10; i++) {

                    try {
                        System.out.println(obj == fastThreadLocalDemo.get());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();;
    }
}
