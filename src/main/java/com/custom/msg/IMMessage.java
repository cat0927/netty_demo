package com.custom.msg;

import lombok.ToString;
import org.msgpack.annotation.Message;

import lombok.Data;

/**
 * DATE 2020-05-16 自定义消息实体
 */
@Data
@ToString
@Message
public class IMMessage {

	private String addr; // IP 地址
	private String cmd; // 命令类型
	private long time; // 发送时间
	private int online; // 当前在线人数
	private String sender; // 发送人
	private String receiver; // 接收人
	private String content; // 消息内容
	private String terminal; // 终端

	public IMMessage() {
	}

	public IMMessage(String cmd, long time, int online, String content) {
		this.cmd = cmd;
		this.time = time;
		this.online = online;
		this.content = content;
	}

	public IMMessage(String cmd, String terminal, long time, String sender) {
		this.cmd = cmd;
		this.terminal = terminal;
		this.time = time;
		this.sender = sender;
	}

	public IMMessage(String cmd, long time, String sender, String content) {
		this.cmd = cmd;
		this.time = time;
		this.sender = sender;
		this.content = content;
	}
}
