package com.ascent.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.ascent.util.UserDataClient;

/**
 * 用户注册窗体
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class RegistFrame extends JFrame {
	private JTextField userText;

	private JPasswordField password;

	private JPasswordField repassword;

	private JLabel tip;

	private UserDataClient userDataClient;

	/**
	 * 默认构造方法，初始化用户注册窗体
	 */
	public RegistFrame() {
		this.setTitle("用户注册");

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		JPanel registPanel = new JPanel();

		JLabel userLabel = new JLabel("用户帐号：");
		JLabel passwordLabel = new JLabel("用户密码：");
		JLabel repasswordLabel = new JLabel("重复密码：");

		userText = new JTextField(15);
		password = new JPasswordField(15);
		repassword = new JPasswordField(15);
		JButton regist = new JButton("注册");
		JButton exitButton = new JButton("退出");

		registPanel.add(userLabel);
		registPanel.add(new JScrollPane(userText));
		registPanel.add(passwordLabel);
		registPanel.add(new JScrollPane(password));
		registPanel.add(repasswordLabel);
		registPanel.add(new JScrollPane(repassword));
		registPanel.add(regist);
		registPanel.add(exitButton);

		setResizable(false);
		setSize(260, 180);
		setLocation(300, 100);

		JPanel tipPanel = new JPanel();

		tip = new JLabel();

		tipPanel.add(tip);

		container.add(BorderLayout.CENTER, registPanel);
		container.add(BorderLayout.NORTH, tip);

		exitButton.addActionListener(new ExitActionListener());
		regist.addActionListener(new RegistActionListener());
		repassword.addFocusListener(new MyFocusListener());
		this.addWindowListener(new WindowCloser());
		this.addWindowFocusListener(new WindowFocusListener() {// 设置父窗口
					public void windowGainedFocus(WindowEvent e) {
					}
					public void windowLostFocus(WindowEvent e) {
						e.getWindow().toFront();
					}
				});
		try {
			userDataClient = new UserDataClient();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 退出按钮事件监听
	 * @author ascent
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
			dispose();
		}
	}

	/**
	 * 注册按钮事件监听
	 * @author ascent
	 */
	class RegistActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// 用户注册操作
			boolean bo = userDataClient.addUser(userText.getText(), new String(password.getPassword()));
			if (bo) {
				tip.setText("注册成功！");
			} else {
				tip.setText("用户名已存在！");
			}
		}
	}

	/**
	 * "关闭窗口"事件处理内部类
	 * @author ascent
	 */
	class WindowCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			setVisible(false);
			dispose();
		}
	}

	/**
	 * 密码不一致触发的事件监听器处理类
	 * @author ascent
	 */
	class MyFocusListener implements FocusListener {

		public void focusGained(FocusEvent arg0) {
		}

		public void focusLost(FocusEvent e) {
			if (e.getSource().equals(password)) {
				if (new String(password.getPassword()) == "" || new String(password.getPassword()) == null) {
					tip.setText("密码不能为空!");
				}
			} else if (e.getSource().equals(repassword)) {
				if (!new String(password.getPassword()).equals(new String(password.getPassword()))) {
					tip.setText("两次密码不一致！");
				}
			} else {
				tip.setText("");
			}
		}
	}
}
