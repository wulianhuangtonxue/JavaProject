package com.ascent.util;

import java.io.*;
import java.net.*;
import java.util.*;

// 导入用户和药品类
import com.ascent.bean.Product;
import com.ascent.bean.User;

/**
 * 这个类是socket连接的处理器 例如：
 * <pre>
 * Handler aHandler = new Handler(clientSocket, myProductDataAccessor);
 * aHandler.start();
 * </pre>
 * @author ascent
 * @version 1.0
 */
// 继承进程类和使用产品接口
public class Handler extends Thread implements ProtocolPort {

	protected Socket clientSocket;			// 用户端的Socket

	protected ObjectOutputStream outputToClient;	// 输出给用户的输出流对象

	protected ObjectInputStream inputFromClient;	// 输入流

	protected ProductDataAccessor myProductDataAccessor;	// 产品数据对象

	protected boolean done;

	/**
	 * 带两个参数的构造方法
	 * @param theClientSocket 客户端Socket对象
	 * @param theProductDataAccessor 处理商品数据的对象
	 * @throws IOException 构造对象时可能发生IOException异常
	 */
	public Handler(Socket theClientSocket,ProductDataAccessor theProductDataAccessor) throws IOException
	{
		clientSocket = theClientSocket;
		outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
		inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
		myProductDataAccessor = theProductDataAccessor;
		done = false;
	}

	/**
	 * 执行多线程的run()方法，处理客户端发送的命令
	 */
	@Override
	public void run()
	{
		try
		{
			while (!done)
			{
				log("等待命令...");

				// 从输入流读取整数作为操作码
				int opCode = inputFromClient.readInt();
				log("opCode = " + opCode);

				// 根据操作码执行对应的操作
				switch (opCode)
				{
					// 获取所有的产品分类名称
					case ProtocolPort.OP_GET_PRODUCT_CATEGORIES:
						opGetProductCategories();
						break;
					// 根据分类名称获取对应类下所有的商品
					case ProtocolPort.OP_GET_PRODUCTS:
						opGetProducts();
						break;
					// 获取用户信息
					case ProtocolPort.OP_GET_USERS:
						opGetUsers();
						break;
					// 注册用户
					case ProtocolPort.OP_ADD_USERS:
						opAddUser();
						break;
					// 添加商品
					case ProtocolPort.OP_ADD_PRODUCT:
						opAddProduct();
						break;
					// 获取所有的商品
					case ProtocolPort.OP_GET_ALL_PRODUCTS:
						opGetALLProducts();
						break;
					default:
						System.out.println("错误代码");
				}
			}
		} catch (IOException exc) {
			log(exc);
		}
	}

	/**
	 * 返回用户信息
	 */
	private void opGetUsers() {
		try
		{
			// 获取用户数据表对象
			HashMap<String,User> userTable = myProductDataAccessor.getUsers();
			// 将用户信息发送给客户端
			outputToClient.writeObject(userTable);
			outputToClient.flush();
		}
		catch (IOException exe)
		{
			log("发生异常：" + exe);
		}
	}

	/**
	 * 返回分类名称
	 */
	protected void opGetProductCategories()
	{
		try
		{
			// 获取类别集合列表
			ArrayList<String> categoryList = myProductDataAccessor.getCategories();
			// 将获取的分类信息发到客户端
			outputToClient.writeObject(categoryList);
			outputToClient.flush();
			log("发出 " + categoryList.size() + " 类别信息到客户端");
		}
		// 捕获io异常
		catch (IOException exc) {
			log("发生异常:  " + exc);
		}
	}

	/**
	 * 返回某个分类名称的所有商品
	 */
	protected void opGetProducts()
	{
		try {
			log("读取份类信息");
			// 先从输入流中获取类别
			String category = (String) inputFromClient.readObject();
			log("类别是 " + category);

			// 再根据类别获取对应的商品列表
			ArrayList<Product> recordingList = myProductDataAccessor.getProducts(category);

			// 将商品列表发送给客户端
			outputToClient.writeObject(recordingList);
			outputToClient.flush();
			log("发出 " + recordingList.size() + "个产品信息到客户端.");
		}
		catch (IOException | ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}

	/**
	 * 处理用户注册
	 */
	// 由于在注册时，已经考虑过重复了，所以这里直接调用即可
	public void opAddUser()
	{
		try
		{
			// 从io读取到输入流对象中获取用户信息并创建用户对象
			User user = (User) this.inputFromClient.readObject();
			this.myProductDataAccessor.save(user);
		}
		catch (IOException | ClassNotFoundException e)
		{
			log("发生异常:  " + e);
			e.printStackTrace();
		}
	}

	/**
	 * 用于新增商品，首先新增的商品已经在客户端确认是否原来已经存在
	 */
	public void opAddProduct()
	{
		// 读取新增商品，并保存在数据文件中
		try
		{
			Product product = (Product) this.inputFromClient.readObject();
			this.myProductDataAccessor.save(product);
		}
		catch (IOException | ClassNotFoundException e)
		{
			log("发生异常:  " + e);
			e.printStackTrace();
		}
	}

	/**
	 * 用于获取所有的商品
	 */
	public void opGetALLProducts()
	{
		try
		{
			HashMap<String, Product> productHashMap = this.myProductDataAccessor.getAllProducts();
			outputToClient.writeObject(productHashMap);
			outputToClient.flush();
		}
		catch(IOException e)
		{
			log("发生异常:  " + e);
			e.printStackTrace();
		}
	}

	/**
	 * 处理线程运行时标志
	 * @param flag
	 */
	public void setDone(boolean flag) {
		done = flag;
	}

	/**
	 * 打印信息到控制台
	 * @param msg
	 */
	protected void log(Object msg) {
		System.out.println("处理器: " + msg);
	}

}