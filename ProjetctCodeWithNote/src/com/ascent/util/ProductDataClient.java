package com.ascent.util;

import java.io.*;
import java.net.*;
import java.util.*;
// 导入产品
import com.ascent.bean.Product;

/**
 * 这个类连接数据服务器来获得数据
 * @author ascent
 * @version 1.0
 */
// 对接接口
public class ProductDataClient implements ProtocolPort {

	/**
	 * socket引用
	 */
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
	public ProductDataClient() throws IOException
	{
		// 调用含参数的构造， 默认本地， 端口号5150
		this(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * 接受主机名和端口号的构造方法
	 */
	public ProductDataClient(String hostName, int port) throws IOException
	{

		log("连接数据服务器..." + hostName + ":" + port);

		hostSocket = new Socket(hostName, port);
		outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
		inputFromServer = new ObjectInputStream(hostSocket.getInputStream());

		log("连接成功.");
	}

	/**
	 * 返回类别集合
	 */
	@SuppressWarnings("unchecked")
	// 获取产品信息
	public ArrayList<String> getCategories() throws IOException
	{
		// 初始为空
		ArrayList<String> categoryList = null;
		try
		{
			// 发送请求
			log("发送请求: OP_GET_PRODUCT_CATEGORIES");
			// 向服务器发送一个获取所有的产品的信号
			outputToServer.writeInt(ProtocolPort.OP_GET_PRODUCT_CATEGORIES);
			outputToServer.flush();			// 刷新

			// 对应接收请求
			log("接收数据...");
			// 列表获取输入流获取产品信息数据对象
			categoryList = (ArrayList<String>) inputFromServer.readObject();
			// 收到多少产品还有类别
			log("收到 " + categoryList.size() + " 类别.");
		}
		// 打印类未找到的信息
		catch (ClassNotFoundException exc)
		{
			log("=====>>>  异常: " + exc);
			throw new IOException("找不到相关类");
		}

		// 返回产品集合
		return categoryList;
	}

	/**
	 * 返回产品集合
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Product> getProducts(String category) throws IOException
	{

		ArrayList<Product> productList = null;

		try
		{
			log("发送请求: OP_GET_PRODUCTS  类别 = " + category);
			// 发送一个 获取当前分类名称获得分类下的所有商品对象信号
			outputToServer.writeInt(ProtocolPort.OP_GET_PRODUCTS);
			// 在传入对应的类别名称
			outputToServer.writeObject(category);
			outputToServer.flush();

			log("接收数据...");
			// 接收产品数据
			productList = (ArrayList<Product>)inputFromServer.readObject();
			log("收到 " + productList.size() + " 产品.");
		}
		// 捕获类无法找到的异常
		catch (ClassNotFoundException exc)
		{
			log("=====>>>  异常: " + exc);
			throw new IOException("找不到相关类");
		}

		return productList;
	}

	/**
	 * 日志方法.
	 */
	protected void log(Object msg) {
		System.out.println("ProductDataClient类: " + msg);
	}
}