package com.ascent.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.ascent.bean.User;
import com.ascent.util.UserDataClient;

/**
 * �û���½����
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame {

	protected JTextField userText;

	protected JPasswordField password;

	protected JLabel tip;

	protected UserDataClient userDataClient;

	/**
	 * Ĭ�ϵĹ��췽������ʼ����½����
	 */
	public LoginFrame() {

		setTitle("�û���½");

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		JPanel loginPanel = new JPanel();

		JLabel userLabel = new JLabel("�û��ʺţ�");
		JLabel passwordLabel = new JLabel("�û����룺");

		userText = new JTextField(15);
		password = new JPasswordField(15);

		JButton loginButton = new JButton("��½");
		JButton regist = new JButton("ע��");
		JButton exitButton = new JButton("�˳�");

		loginPanel.add(userLabel);
		loginPanel.add(new JScrollPane(userText));
		loginPanel.add(passwordLabel);
		loginPanel.add(new JScrollPane(password));
		loginPanel.add(loginButton);
		loginPanel.add(regist);
		loginPanel.add(exitButton);

		setResizable(false);
		setSize(260, 150);
		setLocation(300, 100);

		JPanel tipPanel = new JPanel();

		tip = new JLabel();

		tipPanel.add(tip);

		container.add(BorderLayout.CENTER, loginPanel);
		container.add(BorderLayout.NORTH, tip);

		exitButton.addActionListener(new ExitActionListener());
		loginButton.addActionListener(new LoginActionListener());
		regist.addActionListener(new RegistActionListener());
		this.addWindowListener(new WindowCloser());
		try {
			userDataClient = new UserDataClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����"�˳�"��ť�¼��������ڲ���
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
			dispose();
			userDataClient.closeSocKet();
		}
	}

	/**
	 * ����"��½"��ť�¼��������ڲ���
	 */
	class LoginActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			boolean bo = false;
			HashMap userTable = userDataClient.getUsers();
			if (userTable != null) {
				if (userTable.containsKey(userText.getText())) {
					User userObject = (User) userTable.get(userText.getText());
					char[] chr = password.getPassword();
					String pwd = new String(chr);
					if (userObject.getPassword().equals(pwd)) {
						bo = true;
					}
				}
				if (bo) {
					userDataClient.closeSocKet();
					setVisible(false);
					dispose();
					MainFrame myFrame = new MainFrame();
					myFrame.setVisible(true);
				} else {
					tip.setText("�ʺŲ�����,���������.");
				}
			} else {
				tip.setText("����������ʧ��,���Ժ�����.");
			}
		}
	}

	/**
	 * ����"ע��"��ť�¼��������ڲ���.
	 */
	class RegistActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// ��ע���û��Ĵ���
			RegistFrame registFrame = new RegistFrame();
			registFrame.setVisible(true);
		}
	}

	/**
	 * ����"�رմ���"�¼��������ڲ���.
	 */
	class WindowCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			setVisible(false);
			dispose();
			userDataClient.closeSocKet();
		}
	}
}
