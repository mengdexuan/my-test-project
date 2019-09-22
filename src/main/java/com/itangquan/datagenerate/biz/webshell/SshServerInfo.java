package com.itangquan.datagenerate.biz.webshell;

import lombok.Data;

@Data
public class SshServerInfo {
	private String id;
	//主机名称
	private String hostname;
	//用户名
	private String username;
	//密码
	private String password;
	//描述
	private String note;

	public SshServerInfo(){

	}
	public SshServerInfo(String hostname, String username, String password) {
		this.hostname = hostname;
		this.username = username;
		this.password = password;
	}


}
