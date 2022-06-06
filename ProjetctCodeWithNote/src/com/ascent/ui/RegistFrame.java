package com.ascent.ui;

// 相比Login 多了Focus
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
import javax.swing.JPasswordField;		// 密码框组件
import javax.swing.JScrollPane;
import javax.swing.JTextField;

// 导入用户数据客户端的java文件
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

	private JPasswordField repassword;		// 重复密码的密码框

	private JLabel tip;						// 提示标签

	private UserDataClient userDataClient;

	/**
	 * 默认构造方法，初始化用户注册窗体
	 */
	public RegistFrame()
	{
		this.setTitle("用户注册");

		// 获取一个容器，用来存放面板
		Container container = this.getContentPane();
		// 设置为盒式布局
		container.setLayout(new BorderLayout());

		// 新建一个登录面板
		JPanel registPanel = new JPanel();

		// 以下分别对应 用户账号，用户密码，重复密码的三个标签
		JLabel userLabel = new JLabel("用户帐号：");
		JLabel passwordLabel = new JLabel("用户密码：");
		JLabel repasswordLabel = new JLabel("重复密码：");

		// 文本框
		userText = new JTextField(15);
		// 密码框
		password = new JPasswordField(15);
		repassword = new JPasswordField(15);
		// 注册按钮
		JButton regist = new JButton("注册");
		// 退出按钮
		JButton exitButton = new JButton("退出");

		// 往注册面板分别添加上述组件：标签，文本框，密码框，按钮
		registPanel.add(userLabel);
		registPanel.add(new JScrollPane(userText));
		registPanel.add(passwordLabel);
		registPanel.add(new JScrollPane(password));
		registPanel.add(repasswordLabel);
		registPanel.add(new JScrollPane(repassword));
		registPanel.add(regist);
		registPanel.add(exitButton);

		// 先设置不可见
		setResizable(false);
		// 然后设置尺寸和位置
		setSize(260, 180);
		setLocation(300, 100);

		// 新建一个面板用于输出提示信息
		JPanel tipPanel = new JPanel();

		// 提示标签实例化
		tip = new JLabel();

		// 将标签加入到提示面板
		tipPanel.add(tip);

		// 将注册面板设置在容器居中，提示设置在容器顶部
		container.add(BorderLayout.CENTER, registPanel);
		container.add(BorderLayout.NORTH, tip);

		// 两个按钮绑定对应的监听事件
		exitButton.addActionListener(new ExitActionListener());
		regist.addActionListener(new RegistActionListener());
		// 重复密码的密码框也添加一个监听事件，用于在组件上接收键盘焦点事件的侦听器接口
		repassword.addFocusListener(new MyFocusListener());
		// 添加一个窗口监听器
		this.addWindowListener(new WindowCloser());
		// 添加指定的窗口焦点侦听器，以从此窗口接收窗口事件
		this.addWindowFocusListener(new WindowFocusListener()
		{// 设置父窗口
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
