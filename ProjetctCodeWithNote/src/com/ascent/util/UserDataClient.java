package com.ascent.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
// �����û���
import com.ascent.bean.User;

/**
 * ������������ݷ��������������
 * @author ascent
 * @version 1.0
 */
// protocolPort
public class UserDataClient implements ProtocolPort {

	/**
	 * socket����
	 */
	// ����socket
	protected Socket hostSocket;

	/**
	 * �����������
	 */
	protected ObjectOutputStream outputToServer;

	/**
	 * ������������
	 */
	protected ObjectInputStream inputFromServer;

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public UserDataClient() throws IOException
	{
		// Ĭ�Ϲ��죬����Ĭ��������"localhost" �� �˿ں� 5150
		this(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * �����������Ͷ˿ںŵĹ��췽��
	 */
	public UserDataClient(String hostName, int port) throws IOException
	{
		// ��ӡ��־
		log("�������ݷ�����..." + hostName + ":" + port);

		// ��������
		try
		{
			// ����socket������Ĭ�������ط�����
			hostSocket = new Socket(hostName, port);
			// ��socket��ȡ���룬���������Ӷ���Ӧ�����¸������
			outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
			inputFromServer = new ObjectInputStream(hostSocket.getInputStream());
			// ��Ӧ��ӡ��Ϣ
			log("���ӳɹ�.");
		}
		// ���򲶻��쳣������ӡ
		catch (Exception e)
		{
			log("����ʧ��.");
		}
	}

	/**
	 * �����û�
	 * @return userTable 
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String,User> getUsers()
	{

		HashMap<String,User> userTable = null;

		try
		{
			log("��������: OP_GET_USERS  ");

			// �����Ԥ��
			outputToServer.writeInt(ProtocolPort.OP_GET_USERS);
			// �������˽��յ�
			outputToServer.flush();

			log("��������...");
			// ��������ȡ����
			userTable = (HashMap<String,User>) inputFromServer.readObject();

		}

		// ������δ�ҵ����쳣1
		// ����IO�쳣
		// �����쳣
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// �����û�
		return userTable;
	}

	/**
	 * �رյ�ǰSocKet
	 */
	public void closeSocKet()
	{
		try
		{
			this.hostSocket.close();
		}
		// ֻ����IO�쳣
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ��־����.
	 * @param msg ��ӡ����־��Ϣ
	 */
	protected void log(Object msg) {
		System.out.println("UserDataClient��: " + msg);
	}

	/**
	 * ע���û�
	 * @param username �û���
	 * @param password ����
	 * @return boolean true:ע��ɹ���false:ע��ʧ��
	 */
	public boolean addUser(String username, String password)
	{
		HashMap<String,User> map = this.getUsers();
		if (map.containsKey(username))
		{
			return false;
		}
		else
		{
			try
			{
				log("��������: OP_ADD_USERS  ");
				// �����������һ��ע���־Int
				outputToServer.writeInt(ProtocolPort.OP_ADD_USERS);
				// �����������һ���û������
				outputToServer.writeObject(new User(username, password, 0));
				// ˢ�£����������Խ���
				outputToServer.flush();
				log("��������...");
				return true;
			}
			// ����IO�쳣����ӡ
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
		return false;
	}

}
