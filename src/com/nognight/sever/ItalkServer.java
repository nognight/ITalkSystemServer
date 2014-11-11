package com.nognight.sever;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.nognight.util.IoUtil;

public class ItalkServer implements Runnable {

	private int port; // 服务器侦听端口
	private boolean exitFlag = false; // 控制主线程循环退出

	// 生命周期和应用一样长，这个类中有并发处理
	private final ClientManage clientManage = new ClientManage();

	public ItalkServer(int port) {
		this.port = port;
	}

	// 启动服务器
	@Override
	public void run() {
		// 构建服务端Socket
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(this.port);
			while (!this.exitFlag) {
				// 侦听客户端请求，并为每个客户端请求返回一个Socket进行通信
				Socket socket = ss.accept(); // 当执行到这句话时程序会阻塞，只有当客户端请求到来时才会唤醒。

				// 创建一个响应线程
				new Thread(new ServerThread(clientManage, socket)).start();
				// System.out.println("服务器正在运行——————————————————");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoUtil.close(ss); // 工具方法
		}
	}

	public boolean isExitFlag() {
		return exitFlag;
	}

	// 控制主线程循环退出
	public void setExitFlag(boolean flag) {
		this.exitFlag = flag;
	}
}
