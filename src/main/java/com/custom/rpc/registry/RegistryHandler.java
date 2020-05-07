package com.custom.rpc.registry;

import com.custom.rpc.InvokerProtocol;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DATE 2020-05-07
 *
 */
@Slf4j
public class RegistryHandler extends ChannelInboundHandlerAdapter {

    // 保存所有可用的服务
    public static ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap();
    // 保存相关的服务类
    public List<String> classNames = Lists.newArrayList();

    public RegistryHandler() {
        scannerClass("com.custom.rpc.provider");
        doRegister();
    }

    /**
     * 利用反射原理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("RegistryHandler registryMap, size + " + registryMap.size());
        Object result = new Object();

        InvokerProtocol request =  (InvokerProtocol)msg;
        System.out.println("RegistryHandler, className: " + request.getClassName());
        if (registryMap.containsKey(request.getClassName())) {
            Object clazz = registryMap.get(request.getClassName());
            Method method =  clazz.getClass().getMethod(request.getMethodName(), request.getParams());
            result = method.invoke(clazz, request.getValues());
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    public void scannerClass(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file: dir.listFiles()) {
            if (file.isDirectory()) {
                scannerClass(packageName + "." + file.getName());
            } else {
                classNames.add(packageName+ "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    private void doRegister() {
        if (classNames.size() == 0) {
            log.warn("doRegister is not config");
            return;
        }
        for (String className: classNames) {
            System.out.println("doRegister className: "+ className);
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> i =  clazz.getInterfaces()[0];
                registryMap.put(i.getName(), clazz.newInstance());
            } catch (Exception e) {
                log.error("doRegister error", e);
            }
        }
    }

}
