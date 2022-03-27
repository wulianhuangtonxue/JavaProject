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
 * 用户登陆窗体
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
	 * 默认的构造方法，初始化登陆窗体
	 */
	public LoginFrame() {

		setTitle("用户登陆");

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		JPanel loginPanel = new JPanel();

		JLabel userLabel = new JLabel("用户帐号：");
		JLabel passwordLabel = new JLabel("用户密码：");

		userText = new JTextField(15);
		password = new JPasswordField(15);

		JButton loginButton = new JButton("登陆");
		JButton regist = new JButton("注册");
		JButton exitButton = new JButton("退出");

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
	 * 处理"退出"按钮事件监听的内部类
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
			dispose();
			userDataClient.closeSocKet();
		}
	}

	/**
	 * 处理"登陆"按钮事件监听的内部类
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
					tip.setText("帐号不存在,或密码错误.");
				}
			} else {
				tip.setText("服务器连接失败,请稍候再试.");
			}
		}
	}

	/**
	 * 处理"注册"按钮事件监听的内部类.
	 */
	class RegistActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// 打开注册用户的窗口
			RegistFrame registFrame = new RegistFrame();
			registFrame.setVisible(true);
		}
	}

	/**
	 * 处理"关闭窗口"事件监听的内部类.
	 */
	class WindowCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			setVisible(false);
			dispose();
			userDataClient.closeSocKet();
		}
	}
}
