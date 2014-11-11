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

	private int port; // ����������˿�

	private final JPanel contentPanel = new JPanel();
	private JTextField portField;

	/**
	 * Create the dialog.
	 */
	public SeverSettingFrame() {
		setModal(true);// ����ģʽ�Ի���ֻ�жԻ���رպ������ڲſɼ���ʹ��
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
						// ��ö˿�ֵ
						String strPort = portField.getText();
						int tempPort = 0;
						if (!strPort.equalsIgnoreCase("")) {
							tempPort = Integer.parseInt(strPort);
							if ((tempPort < 1024) || (tempPort > 65535)) {

								// �������ֵ��������ʾ
								JOptionPane.showMessageDialog(null,
										"�˿�ֵ�ķ�Χ��1024��65535֮��", "ϵͳ���ô���",
										JOptionPane.ERROR_MESSAGE);
								portField.setText("");// �������ֵ
								return;
							}
							port = tempPort;

							// �رնԻ���
							closeDialog();
						} else {
							// ������Ϣ��
							JOptionPane.showMessageDialog(null, "������˿���ֵ");
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
						// �رնԻ���
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
