package com.ascent.util;

import java.io.*;
import java.net.*;
import java.util.*;
// �����Ʒ
import com.ascent.bean.Product;

/**
 * ������������ݷ��������������
 * @author ascent
 * @version 1.0
 */
// �Խӽӿ�
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
	public ProductDataClient() throws IOException
	{
		// ���ú������Ĺ��죬 Ĭ�ϱ��أ� �˿ں�5150
		this(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * �����������Ͷ˿ںŵĹ��췽��
	 */
	public ProductDataClient(String hostName, int port) throws IOException
	{

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
	// ��ȡ��Ʒ��Ϣ
	public ArrayList<String> getCategories() throws IOException
	{
		// ��ʼΪ��
		ArrayList<String> categoryList = null;
		try
		{
			// ��������
			log("��������: OP_GET_PRODUCT_CATEGORIES");
			// �����������һ����ȡ���еĲ�Ʒ���ź�
			outputToServer.writeInt(ProtocolPort.OP_GET_PRODUCT_CATEGORIES);
			outputToServer.flush();			// ˢ��

			// ��Ӧ��������
			log("��������...");
			// �б��ȡ��������ȡ��Ʒ��Ϣ���ݶ���
			categoryList = (ArrayList<String>) inputFromServer.readObject();
			// �յ����ٲ�Ʒ�������
			log("�յ� " + categoryList.size() + " ���.");
		}
		// ��ӡ��δ�ҵ�����Ϣ
		catch (ClassNotFoundException exc)
		{
			log("=====>>>  �쳣: " + exc);
			throw new IOException("�Ҳ��������");
		}

		// ������𼯺�
		return categoryList;
	}

	/**
	 * ���ز�Ʒ����
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Product> getProducts(String category) throws IOException
	{

		ArrayList<Product> productList = null;

		try
		{
			log("��������: OP_GET_PRODUCTS  ��� = " + category);
			// ����һ�� ��ȡ��ǰ�������ƻ�÷����µ�������Ʒ�����ź�
			outputToServer.writeInt(ProtocolPort.OP_GET_PRODUCTS);
			// �ڴ����Ӧ���������
			outputToServer.writeObject(category);
			outputToServer.flush();

			log("��������...");
			// ���ղ�Ʒ����
			productList = (ArrayList<Product>)inputFromServer.readObject();
			log("�յ� " + productList.size() + " ��Ʒ.");
		}
		// �������޷��ҵ����쳣
		catch (ClassNotFoundException exc)
		{
			log("=====>>>  �쳣: " + exc);
			throw new IOException("�Ҳ��������");
		}

		return productList;
	}

	/**
	 * �ú����ӷ������˻�ȡ�ļ������е�product
	 * @return ����һ����ϣ����Ʒ����Ϊ����ֵΪ��Ʒ����
	 * @throws IOException
	 */
	public HashMap<String, Product> getAllProducts()
	{
		HashMap<String, Product> productTable = null;

		try
		{
			log("��������: OP_GET_ALL_PRODUCTS");

			// �����
			outputToServer.writeInt(ProtocolPort.OP_GET_ALL_PRODUCTS);
			outputToServer.flush();

			log("��������...");
			productTable = (HashMap<String, Product>) inputFromServer.readObject();
		}
		// �����쳣
		catch (ClassNotFoundException | IOException e)
		{
			e.printStackTrace();
		}
		return productTable;
	}

	/**
	 * ������Ʒ �����Ƿ���������������Ʒ
	 * �������������ļ��д���
	 * @param productName
	 * @param cas
	 * @param structure
	 * @param formula
	 * @param price
	 * @param realStock
	 * @param category
	 * @return true-�����ɹ���false-����ʧ��
	 */
	public boolean addProduct(String productName, String cas, String structure, String formula,
							  String price, String realStock, String category)
	{
		HashMap<String, Product> productHashMap = this.getAllProducts();
		if(productHashMap.containsKey(productName))
		{
			log("");
			return false;
		}
		try
		{
			log("��������:OP_ADD_PRODUCT");
			outputToServer.writeInt(ProtocolPort.OP_ADD_PRODUCT);
			outputToServer.writeObject(new Product(productName, cas, structure, formula, price,
					realStock, category));
			outputToServer.flush();
			log("��������...");
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * ��־����.
	 */
	protected void log(Object msg) {
		System.out.println("ProductDataClient��: " + msg);
	}
}