package com.nognight.sever;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.nognight.util.IoUtil;


public class ClientManage {
	// 保存登录成功用户的用户名和Socket对
	private Map<String, Socket> clientMap = new HashMap<String, Socket>();

	public synchronized Map<String, Socket> getClientMap() {
		return clientMap;
	}

	// 用户登 录时创建客户端信息并加入到链表中
	public synchronized void createClient(String client, Socket socket) {
		if (client != null) {
			clientMap.put(client, socket);
		}
	}

	// 用户注销录时从链表中删除客户端信息
	public synchronized void removeClient(String client) {
		if (client != null) {
			this.clientMap.remove(client);
		}
	}

	/*
	 * 查找指定的用户的连接信息
	 */
	public synchronized Socket findSocketByUser(String client) {
		if (client != null) {
			Socket socket = this.clientMap.get(client);
			if (socket != null) {
				return socket;
			}
		}
		return null;
	}
}
