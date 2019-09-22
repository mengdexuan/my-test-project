package com.itangquan.datagenerate.biz.webshell;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;

public class SshWriteThread implements Runnable {
	
	//定义一个flag,来停止线程用
	private boolean isStop = false;
	
	//接入输入流数据
	private InputStream in; 
	
	//用于输出数据
	private WebSocketSession session;
	
	private static final String UTF8 = "UTF-8";
	
	//停止线程
	public void stopThread() {
		this.isStop = true;
	}
	
	
	public SshWriteThread(InputStream in, WebSocketSession session) {
		super();
		this.in = in;
		this.session = session;
	}

	public void run() {
        try {
            //读取数据
            while (!isStop  &&                       //线程是否停止
            		session != null &&                //session 不是空的
            		session.isOpen()) {               //session是打开的状态
            	//写数据到客户端
            	writeToWeb(in);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	/**
	 * 写数据到web控制界面
	 * @param in
	 */
	private synchronized void writeToWeb(InputStream in) {
		
		try {
			//定义一个缓存
			//一个UDP 的用户数据报的数据字段长度为8192字节
			byte [] buff = new byte[8192];	
			
			int len =0;
			StringBuffer sb = new StringBuffer();
			while((len = in.read(buff)) > 0) {
				//设定从0 开始
				sb.setLength(0);
				
				//读取数组里面的数据，进行补码
				for (int i = 0; i < len; i++){
					//进行补码操作
					char c = (char) (buff[i] & 0xff);
					sb.append(c);
				}
				String line = new String(sb.toString().getBytes("ISO-8859-1"),UTF8);

				//写数据到服务器端
				session.sendMessage(new TextMessage(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
