package com.ascent.util;

import java.io.*;
import java.net.*;

/**
 * 数据服务器类
 * @author ascent
 * @version 1.0
 */
public class ProductDataServer implements ProtocolPort {

	protected ServerSocket myServerSocket;

	protected ProductDataAccessor myProductDataAccessor;

	protected boolean done;

	/**
	 * 默认构造方法
	 */
	public ProductDataServer() {
		this(ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * 带一个参数构造方法
	 * @param thePort 服务器端端口号
	 */
	public ProductDataServer(int thePort) {

		try {
			done = false;
			log("启动服务器 " + thePort);
			myServerSocket = new ServerSocket(thePort);
			myProductDataAccessor = new ProductDataAccessor();

			log("\n服务器准备就绪!");
			listenForConnections();
		} catch (IOException exc) {
			log(exc);
			System.exit(1);
		}
	}

	/**
	 * 监听客户端发送请求连接
	 */
	protected void listenForConnections() {
		Socket clientSocket = null;
		Handler aHandler = null;

		try {
			while (!done) {
				log("\n等待请求...");
				clientSocket = myServerSocket.accept();

				String clientHostName = clientSocket.getInetAddress().getHostName();

				log("收到连接: " + clientHostName);
				aHandler = new Handler(clientSocket, myProductDataAccessor);
				aHandler.start();
			}
		} catch (IOException exc) {
			log("listenForConnections()中发生异常:  " + exc);
		}
	}

	/**
	 * 日志方法
	 * @param msg 打印日志的消息
	 */
	protected void log(Object msg) {
		System.out.println("ProductDataServer类: " + msg);
	}

	/**
	 * 主方法， 启动服务器端
	 * @param args 无
	 */
	public static void main(String[] args) {

		@SuppressWarnings("unused")
		ProductDataServer myServer = null;

		if (args.length == 1) {
			int port = Integer.parseInt(args[0]);
			myServer = new ProductDataServer(port);
		} else {
			myServer = new ProductDataServer();
		}
	}
}