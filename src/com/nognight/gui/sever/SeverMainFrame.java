package com.nognight.gui.sever;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.theme.SubstanceTerracottaTheme;

import com.nognight.sever.ClientManage;
import com.nognight.sever.ItalkServer;
import com.nognight.util.IoUtil;
import com.nognight.util.WindowUtils;

public class SeverMainFrame extends JFrame {
	private ItalkServer server;
	private int port = 10000;
	JLabel severStatu;
	JLabel severIP;
	JLabel severPort;
	JLabel onlineUserNum;
	private JButton startButton;
	private JButton stopButton;
	private JButton settingButton;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// SwingƤ��
					UIManager.setLookAndFeel(new SubstanceLookAndFeel());
					JFrame.setDefaultLookAndFeelDecorated(true);
					JDialog.setDefaultLookAndFeelDecorated(true);
					SubstanceLookAndFeel
							.setCurrentTheme(new SubstanceTerracottaTheme());// Ĭ��Ƥ��
					SeverMainFrame frame = new SeverMainFrame();
					WindowUtils.setWindowCenter(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SeverMainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("ITalkSystemSever");
		lblNewLabel.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel, BorderLayout.NORTH);

		JLabel lblNewLabel_1 = new JLabel("\u7248\u672C:alpha");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblNewLabel_1, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		startButton = new JButton("\u542F\u52A8");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startServer();
			}
		});
		startButton.setBounds(314, 72, 93, 23);
		panel.add(startButton);

		stopButton = new JButton("\u505C\u6B62");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stopServer();

			}
		});
		stopButton.setBounds(314, 103, 93, 23);
		panel.add(stopButton);

		settingButton = new JButton("\u8BBE\u7F6E");
		settingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showSettingFrame();

			}
		});
		settingButton.setBounds(314, 132, 93, 23);
		panel.add(settingButton);

		severStatu = new JLabel("stop");
		severStatu.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		severStatu.setBounds(194, 45, 99, 22);
		panel.add(severStatu);

		JLabel lblNewLabel_3 = new JLabel("\u670D\u52A1\u7AEFIP");
		lblNewLabel_3.setBounds(110, 115, 54, 15);
		panel.add(lblNewLabel_3);

		// ��ʾ������ip
		// getLocalHost();

		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
		String ip = addr.getHostAddress().toString();// ��ñ���IP
		severIP = new JLabel(ip);
		severIP.setBounds(187, 115, 106, 15);
		panel.add(severIP);

		severPort = new JLabel("10000");
		severPort.setBounds(187, 140, 54, 15);
		panel.add(severPort);

		JLabel lblNewLabel_6 = new JLabel("\u7AEF \u53E3 \u53F7");
		lblNewLabel_6.setBounds(110, 140, 54, 15);
		panel.add(lblNewLabel_6);

		JLabel lblNewLabel_2 = new JLabel("\u670D\u52A1\u5668\u72B6\u6001");
		lblNewLabel_2.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(75, 45, 99, 22);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_4 = new JLabel("\u5728\u7EBF\u4EBA\u6570");
		lblNewLabel_4.setBounds(110, 90, 54, 15);
		panel.add(lblNewLabel_4);

		// ��ʾ��������
		ClientManage clientManage = new ClientManage();
		String num = "" + clientManage.getClientMap().size();
		onlineUserNum = new JLabel(num);
		onlineUserNum.setBounds(187, 90, 54, 15);
		panel.add(onlineUserNum);
	}

	// ip������
	/*
	 * public void getLocalHost() { InetAddress myIP = null; try{ myIP =
	 * InetAddress.getLocalHost(); }catch(UnknownHostException e){}
	 * severIP.setText(""+myIP); }
	 */

	// ����������
	public void startServer() {
		// �������������
		server = new ItalkServer(port);
		server.setExitFlag(false);
		// ʹ���߳����з������Ƿ�ֹaccept��������GUI���������
		new Thread(server).start();

		severStatu.setText("��������...");

		// ʹֹͣ��ť����
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		settingButton.setEnabled(false);
	}

	// ֹͣ
	public void stopServer() {
		// ������˳�����
		if (server != null) {
			server.setExitFlag(true);
			sendExitMessage(); // �����˳���Ϣ��ǿ�ƻ��ѷ���˵����߳�
			severStatu.setText("ֹͣ");

			// ʹ������ť����
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
			settingButton.setEnabled(true);
		}
	}

	public void sendExitMessage() {
		Socket socket = null;
		try {
			// ����Socket�����ӵ�����ˡ�Ҫ��˿ںźͷ���˵�ServerSocket�Ķ˿ں���ͬ��
			socket = new Socket("127.0.0.1", port);
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			dos.writeUTF("QUIT");
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoUtil.close(socket);
		}
	}

	// ��ʾ���öԻ���
	public void showSettingFrame() {
		SeverSettingFrame settingFrame = new SeverSettingFrame();
		settingFrame.setPort(this.port); // ���ö˿���ʾ�ĳ�ʼֵ

		settingFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);// ���ùر�ʱ�ͷ���Դ
		WindowUtils.setWindowCenter(settingFrame);// ���ھ���
		settingFrame.setVisible(true);
		// ��÷���ֵ
		this.port = settingFrame.getPort();
		this.severPort.setText("" + this.port);
		settingFrame = null;
	}

}
