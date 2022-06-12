package com.ascent.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

// 导入用户类
import com.ascent.bean.User;
// 导入用户客户端类
import com.ascent.util.Server;
import com.ascent.util.UserDataClient;

/**
 * 用户登陆窗体
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame
{

	protected JTextField userText;				// 用户名

	public static String name = "cat";          //客户默认名

	protected Server server;                    //创建客服端

	protected JPasswordField password;			// 密码

	protected JLabel tip;						// 提示标签

	protected UserDataClient userDataClient;	// 用户数据账户

	/**
	 * 默认的构造方法，初始化登陆窗体
	 */
	public LoginFrame()
	{

		setTitle("用户登陆");

		// 获取一个JFrame的内容面板，就相当于获取一个容器
		Container container = this.getContentPane();
		// 设置布局为盒式布局
		container.setLayout(new BorderLayout());

		// 创建一个登录面板
		JPanel loginPanel = new JPanel();

		// 创建两个标签类对象，一个是账号，一个是密码
		JLabel userLabel = new JLabel("用户帐号：");
		JLabel passwordLabel = new JLabel("用户密码：");

		// 用户名 和密码，长度都限制为15
		userText = new JTextField(20);
		password = new JPasswordField(20);

		// 新增三个按钮，分别对应登录，注册，账号管理，退出
		JButton loginButton = new JButton("登陆");
		JButton regist = new JButton("注册");
		JButton account = new JButton("账号管理");
		JButton exitButton = new JButton("退出");

		// 给密码输入框多加一个输入即可实现登录的功能
		password.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					System.out.println("111");
					login();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});

		// 登录面板添加一个用户标签
		loginPanel.add(userLabel);
		// 添加可滑动面板的用户名
		loginPanel.add(new JScrollPane(userText));
		// 添加密码标签
		loginPanel.add(passwordLabel);
		// 密码的输入框
		loginPanel.add(new JScrollPane(password));
		// 添加登录，注册，账号管理，退出三个按钮
		loginPanel.add(loginButton);
		loginPanel.add(regist);
		loginPanel.add(account);
		loginPanel.add(exitButton);

		// 设置窗口大小不可改变
		setResizable(false);
		// 设置尺寸
		setSize(300, 150);
		// 设置窗口出现的位置
		setLocation(300, 100);

		// 创建一个面板对象，用来做提示面板
		JPanel tipPanel = new JPanel();

		// 创建一个标签对象给tip
		tip = new JLabel();

		// 给提示面板新增一个tip标签
		tipPanel.add(tip);

		// 我们将登录面板和tip面板都加进容器内
		// 登录面板居中
		container.add(BorderLayout.CENTER, loginPanel);
		// 提示面板居上
		container.add(BorderLayout.NORTH, tip);

		// 退出按钮添加退出侦听事件
		exitButton.addActionListener(new ExitActionListener());
		// 类似上面添加对应的登录侦听、注册侦听
		loginButton.addActionListener(new LoginActionListener());
		account.addActionListener(new AccountActionListener());
		regist.addActionListener(new RegistActionListener());
		// 当前类添加窗口侦听
		this.addWindowListener(new WindowCloser());
		// 尝试创建一个用户账户

		try
		{
			userDataClient = new UserDataClient();
		}
		// 如果错误，这里认为都是IO异常
		catch (IOException e)
		{
			// 直接打印异常
			e.printStackTrace();
		}
	}

	/**
	 * 处理"退出"按钮事件监听的内部类
	 */
	// 对接动作监听接口
	class ExitActionListener implements ActionListener
	{
		//
		public void actionPerformed(ActionEvent event)
		{
			// 设置该组件不可见
			setVisible(false);
			// 释放此使用到的本机的所有屏幕资源
			dispose();
			// 关闭用户数据账户的socket
			userDataClient.closeSocKet();
		}
	}

	/**
	 * 处理"登陆"按钮事件监听的内部类
	 */
	class LoginActionListener implements ActionListener
	{
		// 这个函数是一定要重写的，否则这个类就是抽象类了
		public void actionPerformed(ActionEvent e)
		{
			boolean bo = false;
			// 用用户数据客户端获取用户表
			HashMap userTable = userDataClient.getUsers();
			// 用户表非空
			if (userTable != null)
			{
				// 判断用户表中是否有相应的用户
				if (userTable.containsKey(userText.getText()))
				{
					// 通过输入的用户获取对应的用户
					User userObject = (User) userTable.get(userText.getText());
					// 获取输入密码
					char[] chr = password.getPassword();
					// 记录用户名称
					name = userText.getText();
					// 将密码字符串化
					String pwd = new String(chr);
					// 用户的密码和输入密码是否系统
					if (userObject.getPassword().equals(pwd))
					{
						// 标志用户正确
						bo = true;
					}
				}
				// 如果用户正确，则当前界面关闭，调用主界面
				if (bo)
				{
					// 关闭用户数据socket
					userDataClient.closeSocKet();
					// 设置不可见
					setVisible(false);
					// 释放当前登录界面的资源，相当于登录窗口无了
					dispose();
					// 客服端创建
					server = new Server();
					// 进入到主界面
					MainFrame myFrame = new MainFrame();
					// 主界面设置可见
					myFrame.setVisible(true);
				}
				// 否则打印 账户错误
				else
				{
					tip.setText("帐号不存在,或密码错误.");
				}
			}
			// 返回服务器错误
			else
			{
				tip.setText("服务器连接失败,请稍候再试.");
			}
		}
	}

	/**
	 * 处理"注册"按钮事件监听的内部类.
	 */
	class RegistActionListener implements ActionListener
	{
		// 重写接口事件监听的函数
		public void actionPerformed(ActionEvent arg0)
		{
			// 打开注册用户的窗口
			RegistFrame registFrame = new RegistFrame();
			registFrame.setVisible(true);				// 设置可见
		}
	}

	/**
	 * 处理"账号"按钮事件监听的内部类.
	 */
	class AccountActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// 打开注册用户的窗口
			AccountFrame accountFrame = new AccountFrame();
			accountFrame.setVisible(true);
		}
	}

	/**
	 * 处理"关闭窗口"事件监听的内部类.
	 * 这个就是那个红叉，我不理解这玩意为什么接口那边不直接写一个，还需要别人在这里重写
	 */
	// 继承窗口适应器
	class WindowCloser extends WindowAdapter
	{
		// 关闭窗口事件重写函数重写，参数是窗口事件
		public void windowClosing(WindowEvent e)
		{
			setVisible(false);
			dispose();
			userDataClient.closeSocKet();			// 关闭socket
		}
	}

	/**
	 * 将登录函数抽出来
	 */
	public void login()
	{
		boolean bo = false;
		HashMap<String, User> userTable = userDataClient.getUsers();
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
