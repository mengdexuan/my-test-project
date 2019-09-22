package com.itangquan.datagenerate.biz.webshell;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class SshShellHandler extends TextWebSocketHandler{


	private static ConcurrentMap<String, SshClient> sessionMap = Maps.newConcurrentMap();


	//关闭连接后的处理
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);

		SshClient client = sessionMap.remove(session.getId());

		//关闭连接
		if (client != null) {
			client.disconnect();
		}

		log.info("session is closed,sessionId:{}",session.getId());
	}
	
	//建立socket连接
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
	}

	//处理socker发送过来的消息
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message){
		try {
			super.handleMessage(session, message);
		} catch (Exception e) {
		}

		//处理连接
		try {
			TextMessage msg = (TextMessage) message;

			String payload = msg.getPayload();
			log.info("receive socket msg:{}",payload);

			SshServerInfo sshServerInfo = null;
			boolean isFirstConn = false;

			try {
				sshServerInfo = JSONUtil.toBean(payload,SshServerInfo.class);
				isFirstConn = true;
			} catch (Exception e) {
			}


			if (isFirstConn){
				//初始化客户端
				SshClient client = new SshClient(sshServerInfo, session);

				//连接服务器
				client.connect();

				sessionMap.put(session.getId(),client);

				session.sendMessage(new TextMessage("firstConn"));
			}else {
				SshClient client = sessionMap.get(session.getId());

				//当客户端不为空的情况
				if (client != null) {
					//receive a close cmd ?
					if (Arrays.equals("exit".getBytes(), msg.asBytes())) {

						if (client != null) {
							client.disconnect();
						}

						session.close();
						return ;
					}
					//写入前台传递过来的命令，发送到目标服务器上
					client.write(new String(msg.asBytes(), "UTF-8"));
				}
			}
		} catch (Exception e) {
			log.error("An error occur, websocket is closed.",e);
			if (session!=null&&session.isOpen()){
				try {
					session.sendMessage(new TextMessage("An error occur, websocket is closed."));
					session.close();
				} catch (IOException e1) {
				}
			}
		}
	}
}
