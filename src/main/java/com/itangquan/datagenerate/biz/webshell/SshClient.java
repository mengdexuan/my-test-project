package com.itangquan.datagenerate.biz.webshell;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

@Slf4j
public class SshClient {

	// 服务器信息
	private SshServerInfo server;
	// 与客户端连接的socket回话
	private WebSocketSession socket;
	// 连接信息
	private Connection conn = null;
	// term的session信息
	private Session session = null;
	// 向服务器外面写数据的线程
	private SshWriteThread writeThread = null;
	
	//写命令到服务器
	private BufferedWriter out =  null;

	public SshClient(SshServerInfo server, WebSocketSession socket) {
		super();
		this.server = server;
		this.socket = socket;
	}


	/**
	 * 连接到目标服务器
	 * 
	 * @return
	 */
	public boolean connect() {
		try {
			String hostname = this.server.getHostname();
			String username = this.server.getUsername();
			String password = this.server.getPassword();
			// 建立连接
			conn = new Connection(hostname, 22);
			// 连接
			conn.connect();
			
			// 授权
			boolean isAuthenticate = conn.authenticateWithPassword(username, password);
			if (isAuthenticate) {
				// 打开连接
				session = conn.openSession();

				// 打开bash
				//第一个参数：模拟终端类型
				//第二个参数：模拟终端宽度
				//第三个参数：模拟终端高度

				//				session.requestPTY("bash", 90, 30, 0, 0, null);


//				session.requestPTY("bash");
				session.requestDumbPTY();


				// 启动shell
				session.startShell();

				// 向客户端写数据
				startWriter();

				// 输出流 
				out = new BufferedWriter(new OutputStreamWriter(session.getStdin(), "utf-8")); 
				
				// 开启term
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向服务器端写数据
	 */
	private void startWriter() {
		// 启动多线程，来获取我们运行的结果
		// 第一个参数 输入流
		// 第二个参数 输出流，这个直接输出的是控制台
		writeThread = new SshWriteThread(session.getStdout(), socket);
		new Thread(writeThread).start();

	}

	/**
	 * 写数据到服务器端，让机器执行命令
	 * @param cmd
	 * @return 
	 */
	public boolean write(String cmd) {
		try {
			this.out.write(cmd );
			this.out.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}
	/**
	 * 关闭连接
	 */
	public void disconnect() {
		try {
			writeThread.stopThread();
		} catch (Exception e) {
		}

		try {
			conn.close();
			conn = null;
		} catch (Exception e) {
		}

		try {
			session.close();
			session = null;
		} catch (Exception e) {
		}

	}
}
