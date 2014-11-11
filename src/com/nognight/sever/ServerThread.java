package com.nognight.sever;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.nognight.dao.AccountDAO;
import com.nognight.dao.FilePathDAO;
import com.nognight.dao.FriendDao;
import com.nognight.dao.MessageDAO;
import com.nognight.entity.Account;
import com.nognight.entity.FilePath;
import com.nognight.entity.Friend;
import com.nognight.entity.Message;
import com.nognight.util.IoUtil;

/*
 * 每个请求上来都会重新开启一个线程，每次请求完成后线程即退出
 */
public class ServerThread implements Runnable {
	private ClientManage clientManage;

	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;

	public ServerThread(ClientManage clientManage, Socket socket)
			throws IOException {
		this.clientManage = clientManage;
		this.socket = socket;
		this.dis = new DataInputStream(socket.getInputStream());
		this.dos = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		String cmd = "";
		try {
			// 判断当前是那种消息
			cmd = dis.readUTF();

			if (cmd.equalsIgnoreCase("LOGIN")) {
				// 测试（ok）
				// System.out.println(cmd);
				handleLoginMessage();
			} else if (cmd.equalsIgnoreCase("CHAT")) {
				handleChatMessage();
				// 处理注册信息
			} else if (cmd.equalsIgnoreCase("REGISTER")) {
				handleRegisterMessage();

			} else if (cmd.equalsIgnoreCase("FRIENDLIST")) {
				friendList();

			} else if (cmd.equalsIgnoreCase("HISTORYLIST")) {
				historyList();

			} else if (cmd.equalsIgnoreCase("SAVEFILE")) {
				saveFile();
			} else if (cmd.equalsIgnoreCase("FINDUSER")) {
				// 测试（ok）
				// System.out.println(cmd);
				addFriend();
			} else if (cmd.equalsIgnoreCase("LOGOUT")) {
				handleLogoutMessage();
				// 传输文件
			} else if (cmd.equalsIgnoreCase("SENDFILE")) {
				transferFile();

			}

			// 将数据提交到客户端
			if (dos != null) {
				dos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 因为登录的socket需要用于后面的数据发送，因此不能关闭，其它请求的socket都需要关闭
			if (!"LOGIN".equalsIgnoreCase(cmd)) {
				IoUtil.close(socket);
			}
		}
	}

	// 处理登录请求(完成)
	public void handleLoginMessage() throws IOException {
		// 获得输入数据
		String username = dis.readUTF();
		String password = dis.readUTF();

		// 登录处理
		boolean flag = false;

		AccountDAO dao = new AccountDAO();
		Account account = dao.findByUserName(username);
		// 判断密码是否匹配

		if (account != null) {
			// 返回请求结果
			dos.writeUTF("LOGINRESULT"); // 返回结果类型
			if (account.getPassword().equals(password)) {
				this.clientManage.createClient(username, socket);

				String success = "用户登录成功";
				dos.writeUTF("SUCCESS");
				dos.writeUTF(success);
				flag = true;

			} else {
				String error = "用户名或密码不正确";
				dos.writeUTF("ERROR");
				dos.writeUTF(error);
			}
			dos.flush();

			// 登录失败则关闭链接
			if (!flag) {
				IoUtil.close(socket);
			}
		}
	}

	// 处理注册请求（完成）
	public void handleRegisterMessage() throws IOException {
		// 获得输入数据
		String username = dis.readUTF();
		String password = dis.readUTF();

		// 注册处理
		boolean flag = false;
		AccountDAO dao = new AccountDAO();
		Account account = new Account();
		account.setUsername(username);
		account.setPassword(password);

		dos.writeUTF("REGISTERRESULT");// 返回结果类型
		if (!isexist(username)) {
			if (true) {
				dao.insert(account);
				flag = true;
			}

			// 返回请求结果

			if (flag) {
				String success = "用户注册成功";
				dos.writeUTF("SUCCESS");
				dos.writeUTF(success);
			}
			dos.flush();

			// 失败则关闭链接
			if (!flag) {
				IoUtil.close(socket);
			}
		} else {
			String error = "用户名已经存在";
			dos.writeUTF("ERROR");
			dos.writeUTF(error);
		}
	}

	// 判断数据库是否有该用户信息（完成）
	public boolean isexist(String username) {
		AccountDAO dao = new AccountDAO();
		Account account = dao.findByUserName(username);
		if (account == null) {
			return false;
		} else {
			return true;
		}

	}

	// 处理好友列表（完成）
	public void friendList() throws IOException {

		// 读入username
		String username = dis.readUTF();

		// 通过username找acc_id
		FriendDao friendDao = new FriendDao();
		Friend friend = new Friend();
		AccountDAO accountDAO = new AccountDAO();
		Account account = new Account();
		account = accountDAO.findByUserName(username);
		int acc_id = account.getId();
		// 通过acc_id查找到好友列表
		ArrayList<Friend> friends = new ArrayList<Friend>();
		friends = friendDao.findFriendList(acc_id);

		dos.writeUTF("FRIENDRESULT");// 返回结果类型

		// 列表返回协议设计
		for (int i = 0; i < friends.size(); i++) {
			dos.writeUTF("NAME");
			friend = friends.get(i);
			dos.writeUTF(friend.getFriendname());
			dos.writeUTF(isOnline(friend.getFriendname()));
			// dos.writeUTF(isOnline(friend.getFriendname()));
			// 测试输出（正确）
			// System.out.println(friend.toString());
		}
		dos.writeUTF("OVER");
		// 测试输出（正确）
		// System.out.print(friends.toString());

	}

	// 判断是否在线（ok）
	public String isOnline(String friend) {
		String result = null;
		// 这个Socket是receiver对应的客户端登录成功时保存的服务端的Socket
		Socket socket = this.clientManage.findSocketByUser(friend);
		if (socket != null) {
			if (socket.isConnected()) {
				result = "(在线)";
			}
		} else {
			result = "(离线)";
			// 测试(ok)
			// System.out.println(result);
		}

		return result;
	}

	// 添加好友处理（ok）
	public void addFriend() throws IOException {
		// 读入当前用户与需要查找的用户
		String username = dis.readUTF();
		String friendname = dis.readUTF();
		// 测试(notok)
		System.out.println(username + friendname);

		AccountDAO accountDAO = new AccountDAO();
		Account account1 = new Account();
		Account account2 = new Account();
		account1 = accountDAO.findByUserName(friendname);
		account2 = accountDAO.findByUserName(username);
		if (account1 != null) {
			Friend friend = new Friend();
			FriendDao friendDao = new FriendDao();
			friend.setAcc_id(account2.getId());
			friend.setFriendname(account1.getUsername());
			friendDao.insert(friend);
			String success = "好友添加成功";
			dos.writeUTF("SUCCESS");
			dos.writeUTF(success);
			// 测试（ok）
			// System.out.println(friendname);
		} else {
			String error = "该用户不存在";
			dos.writeUTF("ERROR");
			dos.writeUTF(error);
		}

	}

	// 处理历史消息（ok）
	public void historyList() throws IOException {

		// 读入username
		String username = dis.readUTF();
		String receiver = dis.readUTF();

		// 通过username找acc_id
		MessageDAO messageDAO = new MessageDAO();
		Message message = new Message();
		AccountDAO accountDAO = new AccountDAO();
		Account account = new Account();
		account = accountDAO.findByUserName(username);
		int acc_id = account.getId();
		// 通过acc_id查找到好友列表
		ArrayList<Message> messages = new ArrayList<Message>();
		messages = messageDAO.findMessage(acc_id, receiver);
		dos.writeUTF("HISTORYRESULT");// 返回结果类型

		// 列表返回协议设计
		for (int i = 0; i < messages.size(); i++) {
			dos.writeUTF("HIS");
			message = messages.get(i);
			dos.writeUTF(message.getMessageStr());
			// dos.writeUTF(message.getDate());
			// 测试输出（正确）
			// System.out.println(message.toString());
		}
		dos.writeUTF("OVER");
		// 测试输出（正确）
		// System.out.print(message.toString());

	}

	// 处理聊天信息（ok）
	public void handleChatMessage() throws IOException {
		// 获得输入数据
		String sender = dis.readUTF(); // 发送者
		String receiver = dis.readUTF(); // 接收者
		String messageStr = dis.readUTF(); // 消息

		// 创建一个message写入sever数据库
		Message message = new Message();
		AccountDAO dao = new AccountDAO();
		Account account = new Account();
		account = dao.findByUserName(sender);// 根据sender找message外键Acc-ID
		message.setAcc_id(account.getId());
		message.setMessageStr(messageStr);
		message.setReceiver(receiver);

		// 获取当前日期（未完成）
		// Date date = new Date();

		// DateFormat df3 = DateFormat.getDateTimeInstance(2, 2);

		// message.setDate(df3.toString());

		MessageDAO messageDAO = new MessageDAO();
		messageDAO.insert(message);

		// 这个Socket是receiver对应的客户端登录成功时保存的服务端的Socket
		Socket socket = this.clientManage.findSocketByUser(receiver);

		if (socket != null) {
			try {
				// 这里发送的消息由receiver对应客户端的读线程读取
				DataOutputStream tempDos = new DataOutputStream(
						socket.getOutputStream());
				tempDos.writeUTF("CHAT"); // 消息类型
				tempDos.writeUTF(sender);// 发送者
				tempDos.writeUTF(receiver);// 接收者
				tempDos.writeUTF(messageStr);// 消息
				tempDos.flush();
			} catch (Exception e) {
				e.printStackTrace();
				clientManage.removeClient(receiver);
			}
		}

		// 返回请求结查
		dos.writeUTF("RESGISTERRESULT"); // 返回结果类型
		dos.writeUTF("SUCCESS"); // 成功标识
		String success = "消息发送成功";
		dos.writeUTF(success);
		dos.flush();
	}

	// 点对点文件传输（未完成）
	public void transferFile() throws IOException {

		String sender = this.dis.readUTF();
		String receiver = this.dis.readUTF();
		String fileName = this.dis.readUTF();
		// byte[] buf = new byte[1024 * 4];
		// while (this.dis.readInt() != -1) {// flag
		// int length = this.dis.readInt();// length
		// this.dis.read(buf, 0, length);

		// }

		Socket socket = this.clientManage.findSocketByUser(receiver);
		if (socket != null) {
			try {
				// 这里发送的消息由receiver对应客户端的读线程读取
				DataOutputStream tempDos = new DataOutputStream(
						socket.getOutputStream());
				tempDos.writeUTF("FILE"); // 消息类型
				tempDos.writeUTF(sender);// 发送者
				tempDos.writeUTF(receiver);// 接收者
				tempDos.writeUTF(fileName);// 消息
				// 传输文件（未完成）

				tempDos.flush();
			} catch (Exception e) {
				e.printStackTrace();
				clientManage.removeClient(receiver);
			}
		}

		// 返回请求结查
		dos.writeUTF("SENDRESULT"); // 返回结果类型
		dos.writeUTF("SUCCESS"); // 成功标识
		String success = "文件发送成功";
		dos.writeUTF(success);
		dos.flush();
	}

	// 文件上传(ok大文件不可行)
	public void saveFile() throws IOException {
		String sender = this.dis.readUTF();
		String fileName = this.dis.readUTF();

		// 测试(ok)
		// System.out.println(fileName);
		Account account = new Account();
		AccountDAO accountDAO = new AccountDAO();
		account = accountDAO.findByUserName(sender);
		int acc_id = account.getId();
		FilePath filePath = new FilePath();
		FilePathDAO filePathDAO = new FilePathDAO();
		File file = new File("D:\\severfile\\" + fileName);
		filePath.setAcc_id(1);
		filePath.setPath(file.getPath());
		filePathDAO.insert(filePath);

		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		// 测试(ok)
		// System.out.println("1");

		// 接收文 件数据，并写入文 件
		byte[] buf = new byte[1024 * 4];
		while (this.dis.readInt() != -1) {// flag
			int length = this.dis.readInt();// length
			this.dis.read(buf, 0, length);
			fos.write(buf, 0, length);
		}
		dos.writeUTF("FILERESULT");
		dos.writeUTF("SUCCESS");
		String success = "文件发送成功";
		dos.writeUTF(success);
		dos.flush();

		fos.flush();
		IoUtil.close(fos);
	}

	// 登出（ok）
	public void handleLogoutMessage() throws IOException {

		String username = this.dis.readUTF();
		this.clientManage.removeClient(username);
		dos.writeUTF("LOGOUT");
		dos.writeUTF("SUCCESS");
		String success = "注销成功";
		dos.writeUTF(success);
		dos.flush();

	}
}
