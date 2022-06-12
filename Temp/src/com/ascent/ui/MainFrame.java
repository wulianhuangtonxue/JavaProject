package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

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
	protected JTabbedPane tabbedPane;			// ����һ��ѡ����

	/**
	 * ��Ʒ panel
	 */
	protected ProductPanel productPanel;		// ��Ʒ����࣬�Զ����

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public MainFrame() {

		setTitle("��ӭʹ��AscentSysӦ��! ");

		// ��ȡ������
		Container container = this.getContentPane();
		// ����Ϊ��ʽ����
		container.setLayout(new BorderLayout());

		// ��������ѡ��Ķ���
		tabbedPane = new JTabbedPane();

		// �½�һ����Ʒ��壬 �õ�ǰ�����Ϊ��Ʒ���ĳ�ʼ���ĸ�����
		productPanel = new ProductPanel(this);
		// �½�һ��ҩƷTab�����󶨶�Ӧ���
		tabbedPane.addTab("ҩƷ", productPanel);

		// ��ѡ�������
		container.add(BorderLayout.CENTER, tabbedPane);

		// �½�һ���˵���
		JMenuBar myMenuBar = new JMenuBar();

		// �ļ��˵�����
		JMenu fileMenu = new JMenu("�ļ�");

		// �򿪲˵�����
		JMenu openMenu = new JMenu("��");
		// �������
		JMenuItem localMenuItem = new JMenuItem("����Ӳ��...");

		openMenu.add(localMenuItem);		// ���˵�������뵽�˵�������

		// ����ͬ��
		JMenuItem networkMenuItem = new JMenuItem("����...");
		// ����򿪴��̵ļ����¼�����
		localMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					Desktop.getDesktop().open(new File("C:\\Program Files (x86)"));
				} catch (IOException e1) {

					// TODO �Զ����ɵ� catch ��
					JOptionPane.showMessageDialog(null, "�޷�����Դ��������", "ϵͳ��Ϣ", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();

				}
			}
		});

		openMenu.add(networkMenuItem);

		JMenuItem webMenuItem = new JMenuItem("������...");
		// ���뻥�����ĵ������
		webMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.bilibili.com/video/BV1GJ411x7h7"));
				} catch (IOException | URISyntaxException e1) {
					// TODO �Զ����ɵ� catch ��
					JOptionPane.showMessageDialog(null, "�޷����������", "ϵͳ��Ϣ", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
				}


			}
		});

		openMenu.add(webMenuItem);
		fileMenu.add(openMenu);			// ������ӵ��ļ��˵���

		// ����ͬ��
		JMenuItem saveMenuItem = new JMenuItem("����");
		fileMenu.add(saveMenuItem);

		JMenuItem exitMenuItem = new JMenuItem("�˳�");
		fileMenu.add(exitMenuItem);

		// ���ļ��˵����뵽�˵�����
		myMenuBar.add(fileMenu);

		// �˳��˵������һ���˳��¼�����
		exitMenuItem.addActionListener(new ExitActionListener());

		// �趨��ۺ���
		setupLookAndFeelMenu(myMenuBar);

		// �����˵���
		JMenu helpMenu = new JMenu("����");
		JMenuItem aboutMenuItem = new JMenuItem("����");
		helpMenu.add(aboutMenuItem);

		myMenuBar.add(helpMenu);

		// �� ���� �����һ��������
		aboutMenuItem.addActionListener(new AboutActionListener());

		// ���˵���Ϊ�˵�����
		this.setJMenuBar(myMenuBar);

		// ���óߴ��λ��
		setSize(500, 400);
		setLocation(100, 100);

		// ���һ�����ڼ���
		this.addWindowListener(new WindowCloser());

		// ���ÿ�ݼ� alt+
		fileMenu.setMnemonic('f');
		exitMenuItem.setMnemonic('x');
		helpMenu.setMnemonic('h');
		aboutMenuItem.setMnemonic('a');

		// �趨��ݼ�
		// �ֱ����˳������棬����
		// �����¼���Ӧ�ļ��̺��¼���Ӧ��ctrl��
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
	protected void setupLookAndFeelMenu(JMenuBar theMenuBar)
	{

		// UIManager����ǰ��ۡ�����۸���ʱ֪ͨ�Ŀ�����ۼ������Ĭ��ֵ�Լ����ڻ�ȡ����Ĭ��ֵ�ı�ݷ�����
		// LookAndFeelInfo[] �ṩ�й�Ϊ���ò˵����ʼӦ�ó������ö���װ��һЩ��Ϣ��
		// getInstalledLookAndFeels ����һ�� s ���飬��ʾ��ǰ���õ�ʵ�֡�Ӧ�ó������ʹ����Щ����Ϊ�û��������ѡ��˵���
		// ��ȷ��������ʱҪ���õ���ۡ�Ϊ�˱��ⴴ�������������ʧ���뱣�����������������ʵ��ʵ��
		UIManager.LookAndFeelInfo[] lookAndFeelInfo = UIManager
				.getInstalledLookAndFeels();		// ��ʵ������ǻ�ȡ���е�
		// �½�һ���˵�����
		JMenu lookAndFeelMenu = new JMenu("ѡ��");
		// ��ʼ������Ϊ��
		JMenuItem anItem = null;
		// �½�һ�������������
		LookAndFeelListener myListener = new LookAndFeelListener();

		try
		{
			// ѭ���������е�UI��ʽ
			for (int i = 0; i < lookAndFeelInfo.length; i++)
			{
				anItem = new JMenuItem(lookAndFeelInfo[i].getName() + " ���");
				// ����һ�����Ե��ַ���ֵ���ڼ������е�ʵ�ֺ������ж����ĸ���ť��ѡ��
				anItem.setActionCommand(lookAndFeelInfo[i].getClassName());
				// ���һ��������
				anItem.addActionListener(myListener);

				// �������ѡ��������뵽��۲˵���
				lookAndFeelMenu.add(anItem);
			}
		}
		// �����쳣
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// ����۲˵�����˵���������
		theMenuBar.add(lookAndFeelMenu);
	}

	/**
	 * �˳�����.
	 */
	public void exit()
	{
		setVisible(false);
		dispose();
		System.exit(0);
	}

	/**
	 * "�˳�"�¼������ڲ���.
	 */
	class ExitActionListener implements ActionListener
	{
		// �˳�������ʵ�ֺ���ֱ�ӵ����˳���������
		public void actionPerformed(ActionEvent event)
		{
			exit();
		}
	}

	/**
	 * ����"�رմ���"�¼����ڲ���.
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
	 * ����"���"ѡ����������ڲ���
	 */
	class LookAndFeelListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// ��ȡ��Ӧ��۵�����
			String className = event.getActionCommand();
			try
			{
				// ��UIManager ���������
				UIManager.setLookAndFeel(className);
				// ���¶�Ӧ�����
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
			// �����쳣
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����"����"�˵����������ڲ���
	 */
	class AboutActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String msg = "��ֵ����!";
			// һ�������ʾ�ڵ�ǰ����ϲ���ʾmsg
			JOptionPane.showMessageDialog(MainFrame.this, msg);
		}
	}
}