package com.custom.msg;

import lombok.Getter;

/**
 * DATE 2020-05-16
 *  定义消息枚举
 */
@Getter
public enum IMP {
    SYSTEM("SYSTEM"),
    LOGIN("LOGIN"),
    LOGOUT("LOGOUT"),
    CHAT("CHAT"),
    FLOWER("FLOWER");


    private String name;

    IMP(String name) {
        this.name = name;
    }

    public static boolean isIMP(String content) {
        return content.matches("^\\[(SYSTEM|LOGIN|LOGOUT|CHAT)\\]");
    }
}
