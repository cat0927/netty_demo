package com.custom.connect;

/**
 * 模拟百万连接
 *
 * 1、Server 启动类，可设置多个端口。
 *  Client 同时连接，Server 多个端口。
 *  ConnectionCountHandler 统计连接请求数
 *
 * 弊端：
 *  单个文件句柄限制。
 *
 * 2、
 *  ServerHandler 单线程版本 【 模拟业务请求 】
 *  ServerThreadPoolHander 多线程版本 【 模拟业务请求 】
 *
 *
 *
 */