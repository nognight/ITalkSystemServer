package com.nognight.sever;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.nognight.util.IoUtil;

public class ItalkServer implements Runnable {

	private int port; // �����������˿�
	private boolean exitFlag = false; // �������߳�ѭ���˳�

	// �������ں�Ӧ��һ��������������в�������
	private final ClientManage clientManage = new ClientManage();

	public ItalkServer(int port) {
		this.port = port;
	}

	// ����������
	@Override
	public void run() {
		// ���������Socket
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(this.port);
			while (!this.exitFlag) {
				// �����ͻ������󣬲�Ϊÿ���ͻ������󷵻�һ��Socket����ͨ��
				Socket socket = ss.accept(); // ��ִ�е���仰ʱ�����������ֻ�е��ͻ���������ʱ�Żỽ�ѡ�

				// ����һ����Ӧ�߳�
				new Thread(new ServerThread(clientManage, socket)).start();
				// System.out.println("�������������С�����������������������������������");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoUtil.close(ss); // ���߷���
		}
	}

	public boolean isExitFlag() {
		return exitFlag;
	}

	// �������߳�ѭ���˳�
	public void setExitFlag(boolean flag) {
		this.exitFlag = flag;
	}
}
