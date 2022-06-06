package com.ascent.util;

import java.io.*;
import java.net.*;
import java.util.*;
import com.ascent.bean.Product;

/**
 * ������������ݷ��������������
 * @author ascent
 * @version 1.0
 */
public class ProductDataClient implements ProtocolPort {

	/**
	 * socket����
	 */
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
	public ProductDataClient() throws IOException {
		this(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * �����������Ͷ˿ںŵĹ��췽��
	 */
	public ProductDataClient(String hostName, int port) throws IOException {

		log("�������ݷ�����..." + hostName + ":" + port);

		hostSocket = new Socket(hostName, port);
		outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
		inputFromServer = new ObjectInputStream(hostSocket.getInputStream());

		log("���ӳɹ�.");
	}

	/**
	 * ������𼯺�
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getCategories() throws IOException {

		ArrayList<String> categoryList = null;

		try {
			log("��������: OP_GET_PRODUCT_CATEGORIES");
			outputToServer.writeInt(ProtocolPort.OP_GET_PRODUCT_CATEGORIES);
			outputToServer.flush();

			log("��������...");
			categoryList = (ArrayList<String>) inputFromServer.readObject();
			log("�յ� " + categoryList.size() + " ���.");
		} catch (ClassNotFoundException exc) {
			log("=====>>>  �쳣: " + exc);
			throw new IOException("�Ҳ��������");
		}

		return categoryList;
	}

	/**
	 * ���ز�Ʒ����
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Product> getProducts(String category) throws IOException {

		ArrayList<Product> productList = null;

		try {
			log("��������: OP_GET_PRODUCTS  ��� = " + category);
			outputToServer.writeInt(ProtocolPort.OP_GET_PRODUCTS);
			outputToServer.writeObject(category);
			outputToServer.flush();

			log("��������...");
			productList = (ArrayList<Product>)inputFromServer.readObject();
			log("�յ� " + productList.size() + " ��Ʒ.");
		} catch (ClassNotFoundException exc) {
			log("=====>>>  �쳣: " + exc);
			throw new IOException("�Ҳ��������");
		}

		return productList;
	}

	/**
	 * ��־����.
	 */
	protected void log(Object msg) {
		System.out.println("ProductDataClient��: " + msg);
	}
}