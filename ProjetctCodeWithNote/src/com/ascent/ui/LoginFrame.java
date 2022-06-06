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

// �����û���
import com.ascent.bean.User;
// �����û��ͻ�����
import com.ascent.util.UserDataClient;

/**
 * �û���½����
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame
{

	protected JTextField userText;				// �û���

	protected JPasswordField password;			// ����

	protected JLabel tip;						// ��ʾ��ǩ

	protected UserDataClient userDataClient;	// �û������˻�

	/**
	 * Ĭ�ϵĹ��췽������ʼ����½����
	 */
	public LoginFrame()
	{

		setTitle("�û���½");

		// ��ȡһ��JFrame��������壬���൱�ڻ�ȡһ������
		Container container = this.getContentPane();
		// ���ò���Ϊ��ʽ����
		container.setLayout(new BorderLayout());

		// ����һ����¼���
		JPanel loginPanel = new JPanel();

		// ����������ǩ�����һ�����˺ţ�һ��������
		JLabel userLabel = new JLabel("�û��ʺţ�");
		JLabel passwordLabel = new JLabel("�û����룺");

		// �û��� �����룬���ȶ�����Ϊ15
		userText = new JTextField(15);
		password = new JPasswordField(15);

		// ����������ť���ֱ��Ӧ��¼��ע�ᣬ�˳�
		JButton loginButton = new JButton("��½");
		JButton regist = new JButton("ע��");
		JButton exitButton = new JButton("�˳�");

		// ��¼������һ���û���ǩ
		loginPanel.add(userLabel);
		// ��ӿɻ��������û���
		loginPanel.add(new JScrollPane(userText));
		// ��������ǩ
		loginPanel.add(passwordLabel);
		// ����������
		loginPanel.add(new JScrollPane(password));
		// ��ӵ�¼��ע�ᣬ�˳�������ť
		loginPanel.add(loginButton);
		loginPanel.add(regist);
		loginPanel.add(exitButton);

		// ���ô��ڴ�С���ɸı�
		setResizable(false);
		// ���óߴ�
		setSize(260, 150);
		// ���ô��ڳ��ֵ�λ��
		setLocation(300, 100);

		// ����һ����������������ʾ���
		JPanel tipPanel = new JPanel();

		// ����һ����ǩ�����tip
		tip = new JLabel();

		// ����ʾ�������һ��tip��ǩ
		tipPanel.add(tip);

		// ���ǽ���¼����tip��嶼�ӽ�������
		// ��¼������
		container.add(BorderLayout.CENTER, loginPanel);
		// ��ʾ������
		container.add(BorderLayout.NORTH, tip);

		// �˳���ť����˳������¼�
		exitButton.addActionListener(new ExitActionListener());
		// ����������Ӷ�Ӧ�ĵ�¼������ע������
		loginButton.addActionListener(new LoginActionListener());
		regist.addActionListener(new RegistActionListener());
		// ��ǰ����Ӵ�������
		this.addWindowListener(new WindowCloser());
		// ���Դ���һ���û��˻�
		try
		{
			userDataClient = new UserDataClient();
		}
		// �������������Ϊ����IO�쳣
		catch (IOException e)
		{
			// ֱ�Ӵ�ӡ�쳣
			e.printStackTrace();
		}
	}

	/**
	 * ����"�˳�"��ť�¼��������ڲ���
	 */
	// �ԽӶ��������ӿ�
	class ExitActionListener implements ActionListener
	{
		//
		public void actionPerformed(ActionEvent event)
		{
			// ���ø�������ɼ�
			setVisible(false);
			// �ͷŴ�ʹ�õ��ı�����������Ļ��Դ
			dispose();
			// �ر��û������˻���socket
			userDataClient.closeSocKet();
		}
	}

	/**
	 * ����"��½"��ť�¼��������ڲ���
	 */
	class LoginActionListener implements ActionListener
	{
		// ���������һ��Ҫ��д�ģ������������ǳ�������
		public void actionPerformed(ActionEvent e)
		{

			boolean bo = false;
			// ���û����ݿͻ��˻�ȡ�û���
			HashMap userTable = userDataClient.getUsers();
			// �û���ǿ�
			if (userTable != null)
			{
				// �ж��û������Ƿ�����Ӧ���û�
				if (userTable.containsKey(userText.getText()))
				{
					// ͨ��������û���ȡ��Ӧ���û�
					User userObject = (User) userTable.get(userText.getText());
					// ��ȡ��������
					char[] chr = password.getPassword();
					// �������ַ�����
					String pwd = new String(chr);
					// �û�����������������Ƿ�ϵͳ
					if (userObject.getPassword().equals(pwd))
					{
						// ��־�û���ȷ
						bo = true;
					}
				}
				// ����û���ȷ����ǰ����رգ�����������
				if (bo)
				{
					// �ر��û�����socket
					userDataClient.closeSocKet();
					// ���ò��ɼ�
					setVisible(false);
					// �ͷŵ�ǰ��¼�������Դ���൱�ڵ�¼��������
					dispose();
					// ���뵽������
					MainFrame myFrame = new MainFrame();
					// ���������ÿɼ�
					myFrame.setVisible(true);
				}
				// �����ӡ �˻�����
				else
				{
					tip.setText("�ʺŲ�����,���������.");
				}
			}
			// ���ط���������
			else
			{
				tip.setText("����������ʧ��,���Ժ�����.");
			}
		}
	}

	/**
	 * ����"ע��"��ť�¼��������ڲ���.
	 */
	class RegistActionListener implements ActionListener
	{
		// ��д�ӿ��¼������ĺ���
		public void actionPerformed(ActionEvent arg0)
		{
			// ��ע���û��Ĵ���
			RegistFrame registFrame = new RegistFrame();
			registFrame.setVisible(true);				// ���ÿɼ�
		}
	}

	/**
	 * ����"�رմ���"�¼��������ڲ���.
	 * ��������Ǹ���棬�Ҳ����������Ϊʲô�ӿ��Ǳ߲�ֱ��дһ��������Ҫ������������д
	 */
	// �̳д�����Ӧ��
	class WindowCloser extends WindowAdapter
	{
		// �رմ����¼���д������д�������Ǵ����¼�
		public void windowClosing(WindowEvent e)
		{
			setVisible(false);
			dispose();
			userDataClient.closeSocKet();			// �ر�socket
		}
	}
}
