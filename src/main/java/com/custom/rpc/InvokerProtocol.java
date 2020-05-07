package com.custom.rpc;

import lombok.Data;

import java.io.Serializable;

/**
 * DATE 2020-05-07
 * 自定义传输协议
 */
@Data
public class InvokerProtocol implements Serializable {

    private String className;

    /**
     * 函数名称
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] params;

    /**
     * 参数列表
     */
    private Object[] values;

}
