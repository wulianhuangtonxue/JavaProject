package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ��˹ҽҩ����ܽ���
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	/**
	 * tabbed pane���
	 */
	protected JTabbedPane tabbedPane;

	/**
	 * ��Ʒ panel
	 */
	protected ProductPanel productPanel;

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public MainFrame() {

		setTitle("��ӭʹ��AscentSysӦ��! ");

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();

		productPanel = new ProductPanel(this);
		tabbedPane.addTab("ҩƷ", productPanel);

		container.add(BorderLayout.CENTER, tabbedPane);

		JMenuBar myMenuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("�ļ�");

		JMenu openMenu = new JMenu("��");
		JMenuItem localMenuItem = new JMenuItem("����Ӳ��...");
		openMenu.add(localMenuItem);

		JMenuItem networkMenuItem = new JMenuItem("����...");
		openMenu.add(networkMenuItem);

		JMenuItem webMenuItem = new JMenuItem("������...");
		openMenu.add(webMenuItem);
		fileMenu.add(openMenu);

		JMenuItem saveMenuItem = new JMenuItem("����");
		fileMenu.add(saveMenuItem);

		JMenuItem exitMenuItem = new JMenuItem("�˳�");
		fileMenu.add(exitMenuItem);

		myMenuBar.add(fileMenu);

		exitMenuItem.addActionListener(new ExitActionListener());

		setupLookAndFeelMenu(myMenuBar);

		JMenu helpMenu = new JMenu("����");
		JMenuItem aboutMenuItem = new JMenuItem("����");
		helpMenu.add(aboutMenuItem);

		myMenuBar.add(helpMenu);

		aboutMenuItem.addActionListener(new AboutActionListener());

		this.setJMenuBar(myMenuBar);

		setSize(500, 400);
		setLocation(100, 100);

		this.addWindowListener(new WindowCloser());

		fileMenu.setMnemonic('f');
		exitMenuItem.setMnemonic('x');
		helpMenu.setMnemonic('h');
		aboutMenuItem.setMnemonic('a');

		// �趨��ݼ�
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));

		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
	}

	/**
	 * �趨��ѡ�����
	 */
	protected void setupLookAndFeelMenu(JMenuBar theMenuBar) {

		UIManager.LookAndFeelInfo[] lookAndFeelInfo = UIManager
				.getInstalledLookAndFeels();
		JMenu lookAndFeelMenu = new JMenu("ѡ��");
		JMenuItem anItem = null;
		LookAndFeelListener myListener = new LookAndFeelListener();

		try {
			for (int i = 0; i < lookAndFeelInfo.length; i++) {
				anItem = new JMenuItem(lookAndFeelInfo[i].getName() + " ���");
				anItem.setActionCommand(lookAndFeelInfo[i].getClassName());
				anItem.addActionListener(myListener);

				lookAndFeelMenu.add(anItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		theMenuBar.add(lookAndFeelMenu);
	}

	/**
	 * �˳�����.
	 */
	public void exit() {
		setVisible(false);
		dispose();
		System.exit(0);
	}

	/**
	 * "�˳�"�¼������ڲ���.
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			exit();
		}
	}

	/**
	 * ����"�رմ���"�¼����ڲ���.
	 */
	class WindowCloser extends WindowAdapter {

		/**
		 * let's call our exit() method defined above
		 */
		public void windowClosing(WindowEvent e) {
			exit();
		}
	}

	/**
	 * ����"���"ѡ����������ڲ���
	 */
	class LookAndFeelListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String className = event.getActionCommand();
			try {
				UIManager.setLookAndFeel(className);
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����"����"�˵����������ڲ���
	 */
	class AboutActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String msg = "��ֵ����!";
			JOptionPane.showMessageDialog(MainFrame.this, msg);
		}
	}
}