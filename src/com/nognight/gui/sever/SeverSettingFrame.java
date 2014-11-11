package com.nognight.gui.sever;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SeverSettingFrame extends JDialog {

	private int port; // 服务端侦听端口

	private final JPanel contentPanel = new JPanel();
	private JTextField portField;

	/**
	 * Create the dialog.
	 */
	public SeverSettingFrame() {
		setModal(true);// 设置模式对话框，只有对话框关闭后，主窗口才可继续使用
		setResizable(false);
		setTitle("\u7CFB\u7EDF\u8BBE\u7F6E");
		setBounds(100, 100, 246, 104);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel label = new JLabel("\u7AEF\u53E3\uFF1A");
			contentPanel.add(label);
		}
		{
			portField = new JTextField();
			contentPanel.add(portField);
			portField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("\u786E\u5B9A");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						// 获得端口值
						String strPort = portField.getText();
						int tempPort = 0;
						if (!strPort.equalsIgnoreCase("")) {
							tempPort = Integer.parseInt(strPort);
							if ((tempPort < 1024) || (tempPort > 65535)) {

								// 如果设置值错误则提示
								JOptionPane.showMessageDialog(null,
										"端口值的范围在1024到65535之间", "系统设置错误",
										JOptionPane.ERROR_MESSAGE);
								portField.setText("");// 清除设置值
								return;
							}
							port = tempPort;

							// 关闭对话框
							closeDialog();
						} else {
							// 错误消息框
							JOptionPane.showMessageDialog(null, "请输入端口数值");
						}
					}

				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("\u53D6\u6D88");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						// 关闭对话框
						closeDialog();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
		this.portField.setText("" + port);
	}

	private void closeDialog() {
		this.setVisible(false);
		this.dispose();
	}
}
