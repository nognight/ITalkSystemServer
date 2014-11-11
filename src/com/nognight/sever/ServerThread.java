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
 * ÿ�����������������¿���һ���̣߳�ÿ��������ɺ��̼߳��˳�
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
			// �жϵ�ǰ��������Ϣ
			cmd = dis.readUTF();

			if (cmd.equalsIgnoreCase("LOGIN")) {
				// ���ԣ�ok��
				// System.out.println(cmd);
				handleLoginMessage();
			} else if (cmd.equalsIgnoreCase("CHAT")) {
				handleChatMessage();
				// ����ע����Ϣ
			} else if (cmd.equalsIgnoreCase("REGISTER")) {
				handleRegisterMessage();

			} else if (cmd.equalsIgnoreCase("FRIENDLIST")) {
				friendList();

			} else if (cmd.equalsIgnoreCase("HISTORYLIST")) {
				historyList();

			} else if (cmd.equalsIgnoreCase("SAVEFILE")) {
				saveFile();
			} else if (cmd.equalsIgnoreCase("FINDUSER")) {
				// ���ԣ�ok��
				// System.out.println(cmd);
				addFriend();
			} else if (cmd.equalsIgnoreCase("LOGOUT")) {
				handleLogoutMessage();
				// �����ļ�
			} else if (cmd.equalsIgnoreCase("SENDFILE")) {
				transferFile();

			}

			// �������ύ���ͻ���
			if (dos != null) {
				dos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// ��Ϊ��¼��socket��Ҫ���ں�������ݷ��ͣ���˲��ܹرգ����������socket����Ҫ�ر�
			if (!"LOGIN".equalsIgnoreCase(cmd)) {
				IoUtil.close(socket);
			}
		}
	}

	// �����¼����(���)
	public void handleLoginMessage() throws IOException {
		// �����������
		String username = dis.readUTF();
		String password = dis.readUTF();

		// ��¼����
		boolean flag = false;

		AccountDAO dao = new AccountDAO();
		Account account = dao.findByUserName(username);
		// �ж������Ƿ�ƥ��

		if (account != null) {
			// ����������
			dos.writeUTF("LOGINRESULT"); // ���ؽ������
			if (account.getPassword().equals(password)) {
				this.clientManage.createClient(username, socket);

				String success = "�û���¼�ɹ�";
				dos.writeUTF("SUCCESS");
				dos.writeUTF(success);
				flag = true;

			} else {
				String error = "�û��������벻��ȷ";
				dos.writeUTF("ERROR");
				dos.writeUTF(error);
			}
			dos.flush();

			// ��¼ʧ����ر�����
			if (!flag) {
				IoUtil.close(socket);
			}
		}
	}

	// ����ע��������ɣ�
	public void handleRegisterMessage() throws IOException {
		// �����������
		String username = dis.readUTF();
		String password = dis.readUTF();

		// ע�ᴦ��
		boolean flag = false;
		AccountDAO dao = new AccountDAO();
		Account account = new Account();
		account.setUsername(username);
		account.setPassword(password);

		dos.writeUTF("REGISTERRESULT");// ���ؽ������
		if (!isexist(username)) {
			if (true) {
				dao.insert(account);
				flag = true;
			}

			// ����������

			if (flag) {
				String success = "�û�ע��ɹ�";
				dos.writeUTF("SUCCESS");
				dos.writeUTF(success);
			}
			dos.flush();

			// ʧ����ر�����
			if (!flag) {
				IoUtil.close(socket);
			}
		} else {
			String error = "�û����Ѿ�����";
			dos.writeUTF("ERROR");
			dos.writeUTF(error);
		}
	}

	// �ж����ݿ��Ƿ��и��û���Ϣ����ɣ�
	public boolean isexist(String username) {
		AccountDAO dao = new AccountDAO();
		Account account = dao.findByUserName(username);
		if (account == null) {
			return false;
		} else {
			return true;
		}

	}

	// ��������б���ɣ�
	public void friendList() throws IOException {

		// ����username
		String username = dis.readUTF();

		// ͨ��username��acc_id
		FriendDao friendDao = new FriendDao();
		Friend friend = new Friend();
		AccountDAO accountDAO = new AccountDAO();
		Account account = new Account();
		account = accountDAO.findByUserName(username);
		int acc_id = account.getId();
		// ͨ��acc_id���ҵ������б�
		ArrayList<Friend> friends = new ArrayList<Friend>();
		friends = friendDao.findFriendList(acc_id);

		dos.writeUTF("FRIENDRESULT");// ���ؽ������

		// �б���Э�����
		for (int i = 0; i < friends.size(); i++) {
			dos.writeUTF("NAME");
			friend = friends.get(i);
			dos.writeUTF(friend.getFriendname());
			dos.writeUTF(isOnline(friend.getFriendname()));
			// dos.writeUTF(isOnline(friend.getFriendname()));
			// �����������ȷ��
			// System.out.println(friend.toString());
		}
		dos.writeUTF("OVER");
		// �����������ȷ��
		// System.out.print(friends.toString());

	}

	// �ж��Ƿ����ߣ�ok��
	public String isOnline(String friend) {
		String result = null;
		// ���Socket��receiver��Ӧ�Ŀͻ��˵�¼�ɹ�ʱ����ķ���˵�Socket
		Socket socket = this.clientManage.findSocketByUser(friend);
		if (socket != null) {
			if (socket.isConnected()) {
				result = "(����)";
			}
		} else {
			result = "(����)";
			// ����(ok)
			// System.out.println(result);
		}

		return result;
	}

	// ��Ӻ��Ѵ���ok��
	public void addFriend() throws IOException {
		// ���뵱ǰ�û�����Ҫ���ҵ��û�
		String username = dis.readUTF();
		String friendname = dis.readUTF();
		// ����(notok)
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
			String success = "������ӳɹ�";
			dos.writeUTF("SUCCESS");
			dos.writeUTF(success);
			// ���ԣ�ok��
			// System.out.println(friendname);
		} else {
			String error = "���û�������";
			dos.writeUTF("ERROR");
			dos.writeUTF(error);
		}

	}

	// ������ʷ��Ϣ��ok��
	public void historyList() throws IOException {

		// ����username
		String username = dis.readUTF();
		String receiver = dis.readUTF();

		// ͨ��username��acc_id
		MessageDAO messageDAO = new MessageDAO();
		Message message = new Message();
		AccountDAO accountDAO = new AccountDAO();
		Account account = new Account();
		account = accountDAO.findByUserName(username);
		int acc_id = account.getId();
		// ͨ��acc_id���ҵ������б�
		ArrayList<Message> messages = new ArrayList<Message>();
		messages = messageDAO.findMessage(acc_id, receiver);
		dos.writeUTF("HISTORYRESULT");// ���ؽ������

		// �б���Э�����
		for (int i = 0; i < messages.size(); i++) {
			dos.writeUTF("HIS");
			message = messages.get(i);
			dos.writeUTF(message.getMessageStr());
			// dos.writeUTF(message.getDate());
			// �����������ȷ��
			// System.out.println(message.toString());
		}
		dos.writeUTF("OVER");
		// �����������ȷ��
		// System.out.print(message.toString());

	}

	// ����������Ϣ��ok��
	public void handleChatMessage() throws IOException {
		// �����������
		String sender = dis.readUTF(); // ������
		String receiver = dis.readUTF(); // ������
		String messageStr = dis.readUTF(); // ��Ϣ

		// ����һ��messageд��sever���ݿ�
		Message message = new Message();
		AccountDAO dao = new AccountDAO();
		Account account = new Account();
		account = dao.findByUserName(sender);// ����sender��message���Acc-ID
		message.setAcc_id(account.getId());
		message.setMessageStr(messageStr);
		message.setReceiver(receiver);

		// ��ȡ��ǰ���ڣ�δ��ɣ�
		// Date date = new Date();

		// DateFormat df3 = DateFormat.getDateTimeInstance(2, 2);

		// message.setDate(df3.toString());

		MessageDAO messageDAO = new MessageDAO();
		messageDAO.insert(message);

		// ���Socket��receiver��Ӧ�Ŀͻ��˵�¼�ɹ�ʱ����ķ���˵�Socket
		Socket socket = this.clientManage.findSocketByUser(receiver);

		if (socket != null) {
			try {
				// ���﷢�͵���Ϣ��receiver��Ӧ�ͻ��˵Ķ��̶߳�ȡ
				DataOutputStream tempDos = new DataOutputStream(
						socket.getOutputStream());
				tempDos.writeUTF("CHAT"); // ��Ϣ����
				tempDos.writeUTF(sender);// ������
				tempDos.writeUTF(receiver);// ������
				tempDos.writeUTF(messageStr);// ��Ϣ
				tempDos.flush();
			} catch (Exception e) {
				e.printStackTrace();
				clientManage.removeClient(receiver);
			}
		}

		// ����������
		dos.writeUTF("RESGISTERRESULT"); // ���ؽ������
		dos.writeUTF("SUCCESS"); // �ɹ���ʶ
		String success = "��Ϣ���ͳɹ�";
		dos.writeUTF(success);
		dos.flush();
	}

	// ��Ե��ļ����䣨δ��ɣ�
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
				// ���﷢�͵���Ϣ��receiver��Ӧ�ͻ��˵Ķ��̶߳�ȡ
				DataOutputStream tempDos = new DataOutputStream(
						socket.getOutputStream());
				tempDos.writeUTF("FILE"); // ��Ϣ����
				tempDos.writeUTF(sender);// ������
				tempDos.writeUTF(receiver);// ������
				tempDos.writeUTF(fileName);// ��Ϣ
				// �����ļ���δ��ɣ�

				tempDos.flush();
			} catch (Exception e) {
				e.printStackTrace();
				clientManage.removeClient(receiver);
			}
		}

		// ����������
		dos.writeUTF("SENDRESULT"); // ���ؽ������
		dos.writeUTF("SUCCESS"); // �ɹ���ʶ
		String success = "�ļ����ͳɹ�";
		dos.writeUTF(success);
		dos.flush();
	}

	// �ļ��ϴ�(ok���ļ�������)
	public void saveFile() throws IOException {
		String sender = this.dis.readUTF();
		String fileName = this.dis.readUTF();

		// ����(ok)
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
		// ����(ok)
		// System.out.println("1");

		// ������ �����ݣ���д���� ��
		byte[] buf = new byte[1024 * 4];
		while (this.dis.readInt() != -1) {// flag
			int length = this.dis.readInt();// length
			this.dis.read(buf, 0, length);
			fos.write(buf, 0, length);
		}
		dos.writeUTF("FILERESULT");
		dos.writeUTF("SUCCESS");
		String success = "�ļ����ͳɹ�";
		dos.writeUTF(success);
		dos.flush();

		fos.flush();
		IoUtil.close(fos);
	}

	// �ǳ���ok��
	public void handleLogoutMessage() throws IOException {

		String username = this.dis.readUTF();
		this.clientManage.removeClient(username);
		dos.writeUTF("LOGOUT");
		dos.writeUTF("SUCCESS");
		String success = "ע���ɹ�";
		dos.writeUTF(success);
		dos.flush();

	}
}
