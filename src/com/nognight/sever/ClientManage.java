package com.nognight.sever;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.nognight.util.IoUtil;


public class ClientManage {
	// �����¼�ɹ��û����û�����Socket��
	private Map<String, Socket> clientMap = new HashMap<String, Socket>();

	public synchronized Map<String, Socket> getClientMap() {
		return clientMap;
	}

	// �û��� ¼ʱ�����ͻ�����Ϣ�����뵽������
	public synchronized void createClient(String client, Socket socket) {
		if (client != null) {
			clientMap.put(client, socket);
		}
	}

	// �û�ע��¼ʱ��������ɾ���ͻ�����Ϣ
	public synchronized void removeClient(String client) {
		if (client != null) {
			this.clientMap.remove(client);
		}
	}

	/*
	 * ����ָ�����û���������Ϣ
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
