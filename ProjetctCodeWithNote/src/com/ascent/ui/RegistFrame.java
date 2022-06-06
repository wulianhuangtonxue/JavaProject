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
				// 将Window设置为焦点窗口时调用，这意味着Window或其子组件之一将接收键盘事件
				public void windowGainedFocus(WindowEvent e) {}
				// 当窗口不再是焦点窗口时调用，这意味着键盘事件将不再传递到窗口或其任何子组件。
				public void windowLostFocus(WindowEvent e)
				{
					// 这里获取父窗口
					e.getWindow().toFront();
				}
			});

		try
		{
			// 尝试获取用户数据
			userDataClient = new UserDataClient();
		}
		// 捕获IO错误
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	/**
	 * 退出按钮事件监听
	 * @author ascent
	 */
	class ExitActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			setVisible(false);
			dispose();
		}
	}

	/**
	 * 注册按钮事件监听
	 * @author ascent
	 */
	class RegistActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			// 用户注册操作
			boolean bo = userDataClient.addUser(userText.getText(), new String(password.getPassword()));
			if (bo)
			{
				tip.setText("注册成功！");
			} else
			{
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
	 * 这个类没用上
	 * @author ascent
	 */
	class MyFocusListener implements FocusListener
	{

		// 这个就是表示密码框被聚焦的时候接收键盘
		public void focusGained(FocusEvent arg0) {
		}

		// 密码框失去聚焦时
		public void focusLost(FocusEvent e)
		{
			// 重复密码框中的密码是否等于输入的密码
			if (e.getSource().equals(password))
			{
				// 如果相等需要判断是否为空
				if (new String(password.getPassword()) == "" || new String(password.getPassword()) == null)
				{
					tip.setText("密码不能为空!");
				}
			}

			//
			else if (e.getSource().equals(repassword))
			{
				if (!new String(password.getPassword()).equals(new String(password.getPassword())))
				{
					tip.setText("两次密码不一致！");
				}
			}
			else
			{
				tip.setText("");
			}
		}
	}
}
