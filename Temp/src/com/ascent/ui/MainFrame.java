package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 艾斯医药主框架界面
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	/**
	 * tabbed pane组件
	 */
	protected JTabbedPane tabbedPane;			// 就是一个选项卡面板

	/**
	 * 产品 panel
	 */
	protected ProductPanel productPanel;		// 产品面板类，自定义的

	/**
	 * 默认构造方法
	 */
	public MainFrame() {

		setTitle("欢迎使用AscentSys应用! ");

		// 获取个容器
		Container container = this.getContentPane();
		// 设置为盒式布局
		container.setLayout(new BorderLayout());

		// 建立顶部选项卡的对象
		tabbedPane = new JTabbedPane();

		// 新建一个产品面板， 用当前面板作为产品面板的初始面板的父窗体
		productPanel = new ProductPanel(this);
		// 新建一个药品Tab，并绑定对应面板
		tabbedPane.addTab("药品", productPanel);

		// 将选项卡面板加入
		container.add(BorderLayout.CENTER, tabbedPane);

		// 新建一个菜单栏
		JMenuBar myMenuBar = new JMenuBar();

		// 文件菜单对象
		JMenu fileMenu = new JMenu("文件");

		// 打开菜单对象
		JMenu openMenu = new JMenu("打开");
		// 事项对象
		JMenuItem localMenuItem = new JMenuItem("本地硬盘...");

		openMenu.add(localMenuItem);		// 将菜单事项加入到菜单对象中

		// 基本同上
		JMenuItem networkMenuItem = new JMenuItem("网络...");
		// 加入打开磁盘的监听事件函数
		localMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					Desktop.getDesktop().open(new File("C:\\Program Files (x86)"));
				} catch (IOException e1) {

					// TODO 自动生成的 catch 块
					JOptionPane.showMessageDialog(null, "无法打开资源管理器！", "系统信息", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();

				}
			}
		});

		openMenu.add(networkMenuItem);

		JMenuItem webMenuItem = new JMenuItem("互联网...");
		// 加入互联网的点击监听
		webMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.bilibili.com/video/BV1GJ411x7h7"));
				} catch (IOException | URISyntaxException e1) {
					// TODO 自动生成的 catch 块
					JOptionPane.showMessageDialog(null, "无法打开浏览器！", "系统信息", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
				}


			}
		});

		openMenu.add(webMenuItem);
		fileMenu.add(openMenu);			// 将打开添加到文件菜单中

		// 基本同上
		JMenuItem saveMenuItem = new JMenuItem("保存");
		fileMenu.add(saveMenuItem);

		JMenuItem exitMenuItem = new JMenuItem("退出");
		fileMenu.add(exitMenuItem);

		// 将文件菜单加入到菜单栏中
		myMenuBar.add(fileMenu);

		// 退出菜单事项绑定一个退出事件监听
		exitMenuItem.addActionListener(new ExitActionListener());

		// 设定外观函数
		setupLookAndFeelMenu(myMenuBar);

		// 帮助菜单栏
		JMenu helpMenu = new JMenu("帮助");
		JMenuItem aboutMenuItem = new JMenuItem("关于");
		helpMenu.add(aboutMenuItem);

		myMenuBar.add(helpMenu);

		// 给 关于 事项加一个监听器
		aboutMenuItem.addActionListener(new AboutActionListener());

		// 将菜单设为菜单导航
		this.setJMenuBar(myMenuBar);

		// 设置尺寸和位置
		setSize(500, 400);
		setLocation(100, 100);

		// 添加一个窗口监听
		this.addWindowListener(new WindowCloser());

		// 设置快捷键 alt+
		fileMenu.setMnemonic('f');
		exitMenuItem.setMnemonic('x');
		helpMenu.setMnemonic('h');
		aboutMenuItem.setMnemonic('a');

		// 设定快捷键
		// 分别是退出，保存，关于
		// 键盘事件对应的键盘和事件对应的ctrl键
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));

		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
	}

	/**
	 * 设定和选择外观
	 */
	protected void setupLookAndFeelMenu(JMenuBar theMenuBar)
	{

		// UIManager管理当前外观、在外观更改时通知的可用外观集、外观默认值以及用于获取各种默认值的便捷方法。
		// LookAndFeelInfo[] 提供有关为配置菜单或初始应用程序设置而安装的一些信息。
		// getInstalledLookAndFeels 返回一个 s 数组，表示当前可用的实现。应用程序可以使用这些对象为用户构造外观选项菜单，
		// 或确定在启动时要设置的外观。为了避免创建大量对象的损失，请保留类的类名，而不是实际实例
		UIManager.LookAndFeelInfo[] lookAndFeelInfo = UIManager
				.getInstalledLookAndFeels();		// 其实这个就是获取已有的
		// 新建一个菜单对象
		JMenu lookAndFeelMenu = new JMenu("选项");
		// 初始事项设为空
		JMenuItem anItem = null;
		// 新建一个外观侦听对象
		LookAndFeelListener myListener = new LookAndFeelListener();

		try
		{
			// 循环遍历已有的UI样式
			for (int i = 0; i < lookAndFeelInfo.length; i++)
			{
				anItem = new JMenuItem(lookAndFeelInfo[i].getName() + " 外观");
				// 设置一个属性的字符串值，在监听类中的实现函数会判断是哪个按钮或选项
				anItem.setActionCommand(lookAndFeelInfo[i].getClassName());
				// 添加一个监听器
				anItem.addActionListener(myListener);

				// 将该外观选项事项加入到外观菜单中
				lookAndFeelMenu.add(anItem);
			}
		}
		// 捕获异常
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// 将外观菜单加入菜单导航栏中
		theMenuBar.add(lookAndFeelMenu);
	}

	/**
	 * 退出方法.
	 */
	public void exit()
	{
		setVisible(false);
		dispose();
		System.exit(0);
	}

	/**
	 * "退出"事件处理内部类.
	 */
	class ExitActionListener implements ActionListener
	{
		// 退出监听的实现函数直接调用退出函数即可
		public void actionPerformed(ActionEvent event)
		{
			exit();
		}
	}

	/**
	 * 处理"关闭窗口"事件的内部类.
	 */
	class WindowCloser extends WindowAdapter
	{

		/**
		 * let's call our exit() method defined above
		 */
		public void windowClosing(WindowEvent e)
		{
			exit();
		}
	}

	/**
	 * 处理"外观"选择监听器的内部类
	 */
	class LookAndFeelListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// 获取对应外观的名字
			String className = event.getActionCommand();
			try
			{
				// 用UIManager 来控制外观
				UIManager.setLookAndFeel(className);
				// 更新对应的外观
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
			// 捕获异常
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理"关于"菜单监听器的内部类
	 */
	class AboutActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String msg = "超值享受!";
			// 一个面板显示在当前组件上并显示msg
			JOptionPane.showMessageDialog(MainFrame.this, msg);
		}
	}
}