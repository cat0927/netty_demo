package com.custom.tomcat;

import com.custom.tomcat.servlet.GTomcat;

/**
 * DATE 2020-05-07
 *
 */
public class ServletMain {

    public static void main(String[] args) {
        new GTomcat().start();
    }
}
