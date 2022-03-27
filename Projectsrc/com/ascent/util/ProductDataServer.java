package com.ascent.util;

import java.io.*;
import java.net.*;

/**
 * ���ݷ�������
 * @author ascent
 * @version 1.0
 */
public class ProductDataServer implements ProtocolPort {

	protected ServerSocket myServerSocket;

	protected ProductDataAccessor myProductDataAccessor;

	protected boolean done;

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public ProductDataServer() {
		this(ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * ��һ���������췽��
	 * @param thePort �������˶˿ں�
	 */
	public ProductDataServer(int thePort) {

		try {
			done = false;
			log("���������� " + thePort);
			myServerSocket = new ServerSocket(thePort);
			myProductDataAccessor = new ProductDataAccessor();

			log("\n������׼������!");
			listenForConnections();
		} catch (IOException exc) {
			log(exc);
			System.exit(1);
		}
	}

	/**
	 * �����ͻ��˷�����������
	 */
	protected void listenForConnections() {
		Socket clientSocket = null;
		Handler aHandler = null;

		try {
			while (!done) {
				log("\n�ȴ�����...");
				clientSocket = myServerSocket.accept();

				String clientHostName = clientSocket.getInetAddress().getHostName();

				log("�յ�����: " + clientHostName);
				aHandler = new Handler(clientSocket, myProductDataAccessor);
				aHandler.start();
			}
		} catch (IOException exc) {
			log("listenForConnections()�з����쳣:  " + exc);
		}
	}

	/**
	 * ��־����
	 * @param msg ��ӡ��־����Ϣ
	 */
	protected void log(Object msg) {
		System.out.println("ProductDataServer��: " + msg);
	}

	/**
	 * �������� ������������
	 * @param args ��
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