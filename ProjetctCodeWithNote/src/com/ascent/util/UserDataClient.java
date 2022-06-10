package com.ascent.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
// 导入用户类
import com.ascent.bean.User;

/**
 * 这个类连接数据服务器来获得数据
 * @author ascent
 * @version 1.0
 */
// protocolPort
public class UserDataClient implements ProtocolPort {

	/**
	 * socket引用
	 */
	// 主机socket
	protected Socket hostSocket;

	/**
	 * 输出流的引用
	 */
	protected ObjectOutputStream outputToServer;

	/**
	 * 输入流的引用
	 */
	protected ObjectInputStream inputFromServer;

	/**
	 * 默认构造方法
	 */
	public UserDataClient() throws IOException
	{
		// 默认构造，传入默认主机名"localhost" 和 端口号 5150
		this(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * 接受主机名和端口号的构造方法
	 */
	public UserDataClient(String hostName, int port) throws IOException
	{
		// 打印日志
		log("连接数据服务器..." + hostName + ":" + port);

		// 尝试连接
		try
		{
			// 创建socket，这里默认连本地服务器
			hostSocket = new Socket(hostName, port);
			// 从socket获取输入，输出流对象从而对应创建新概念对象
			outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
			inputFromServer = new ObjectInputStream(hostSocket.getInputStream());
			// 对应打印信息
			log("连接成功.");
		}
		// 否则捕获异常，并打印
		catch (Exception e)
		{
			log("连接失败.");
		}
	}

	/**
	 * 返回用户
	 * @return userTable 
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String,User> getUsers()
	{

		HashMap<String,User> userTable = null;

		try
		{
			log("发送请求: OP_GET_USERS  ");

			// 输出流预备
			outputToServer.writeInt(ProtocolPort.OP_GET_USERS);
			// 服务器端接收到
			outputToServer.flush();

			log("接收数据...");
			// 输入流获取对象
			userTable = (HashMap<String,User>) inputFromServer.readObject();

		}

		// 捕获类未找到的异常1
		// 捕获IO异常
		// 捕获异常
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// 返回用户
		return userTable;
	}

	/**
	 * 关闭当前SocKet
	 */
	public void closeSocKet()
	{
		try
		{
			this.hostSocket.close();
		}
		// 只捕获IO异常
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 日志方法.
	 * @param msg 打印的日志信息
	 */
	protected void log(Object msg) {
		System.out.println("UserDataClient类: " + msg);
	}

	/**
	 * 注册用户
	 * @param username 用户名
	 * @param password 密码
	 * @return boolean true:注册成功，false:注册失败
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
				log("发送请求: OP_ADD_USERS  ");
				// 给服务器输出一个注册标志Int
				outputToServer.writeInt(ProtocolPort.OP_ADD_USERS);
				// 给服务器输出一个用户类对象
				outputToServer.writeObject(new User(username, password, 0));
				// 刷新，服务器可以接收
				outputToServer.flush();
				log("接收数据...");
				return true;
			}
			// 捕获IO异常并打印
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
		return false;
	}

}
