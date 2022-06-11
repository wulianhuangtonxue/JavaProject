package com.ascent.util;

import java.io.*;
import java.net.*;
import java.util.*;

// �����û���ҩƷ��
import com.ascent.bean.Product;
import com.ascent.bean.User;

/**
 * �������socket���ӵĴ����� ���磺
 * <pre>
 * Handler aHandler = new Handler(clientSocket, myProductDataAccessor);
 * aHandler.start();
 * </pre>
 * @author ascent
 * @version 1.0
 */
// �̳н������ʹ�ò�Ʒ�ӿ�
public class Handler extends Thread implements ProtocolPort {

	protected Socket clientSocket;			// �û��˵�Socket

	protected ObjectOutputStream outputToClient;	// ������û������������

	protected ObjectInputStream inputFromClient;	// ������

	protected ProductDataAccessor myProductDataAccessor;	// ��Ʒ���ݶ���

	protected boolean done;

	/**
	 * �����������Ĺ��췽��
	 * @param theClientSocket �ͻ���Socket����
	 * @param theProductDataAccessor ������Ʒ���ݵĶ���
	 * @throws IOException �������ʱ���ܷ���IOException�쳣
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
	 * ִ�ж��̵߳�run()����������ͻ��˷��͵�����
	 */
	@Override
	public void run()
	{
		try
		{
			while (!done)
			{
				log("�ȴ�����...");

				// ����������ȡ������Ϊ������
				int opCode = inputFromClient.readInt();
				log("opCode = " + opCode);

				// ���ݲ�����ִ�ж�Ӧ�Ĳ���
				switch (opCode)
				{
					// ��ȡ���еĲ�Ʒ��������
					case ProtocolPort.OP_GET_PRODUCT_CATEGORIES:
						opGetProductCategories();
						break;
					// ���ݷ������ƻ�ȡ��Ӧ�������е���Ʒ
					case ProtocolPort.OP_GET_PRODUCTS:
						opGetProducts();
						break;
					// ��ȡ�û���Ϣ
					case ProtocolPort.OP_GET_USERS:
						opGetUsers();
						break;
					// ע���û�
					case ProtocolPort.OP_ADD_USERS:
						opAddUser();
						break;
					// �����Ʒ
					case ProtocolPort.OP_ADD_PRODUCT:
						opAddProduct();
						break;
					// ��ȡ���е���Ʒ
					case ProtocolPort.OP_GET_ALL_PRODUCTS:
						opGetALLProducts();
						break;
					// �޸Ķ�Ӧ����Ʒ����
					case ProtocolPort.OP_CHANGE:
						opChangeProduct();
						break;
					// ɾ����Ӧ����Ʒ
					case ProtocolPort.OP_DELETE_PRODUCT:
						opDeleteProduct();
						break;
					default:
						System.out.println("�������");
				}
			}
		} catch (IOException exc) {
			log(exc);
		}
	}

	/**
	 * �����û���Ϣ
	 */
	private void opGetUsers() {
		try
		{
			// ��ȡ�û����ݱ����
			HashMap<String,User> userTable = myProductDataAccessor.getUsers();
			// ���û���Ϣ���͸��ͻ���
			outputToClient.writeObject(userTable);
			outputToClient.flush();
		}
		catch (IOException exe)
		{
			log("�����쳣��" + exe);
		}
	}

	/**
	 * ���ط�������
	 */
	protected void opGetProductCategories()
	{
		try
		{
			// ��ȡ��𼯺��б�
			ArrayList<String> categoryList = myProductDataAccessor.getCategories();
			// ����ȡ�ķ�����Ϣ�����ͻ���
			outputToClient.writeObject(categoryList);
			outputToClient.flush();
			log("���� " + categoryList.size() + " �����Ϣ���ͻ���");
		}
		// ����io�쳣
		catch (IOException exc) {
			log("�����쳣:  " + exc);
		}
	}

	/**
	 * ����ĳ���������Ƶ�������Ʒ
	 */
	protected void opGetProducts()
	{
		try {
			log("��ȡ������Ϣ");
			// �ȴ��������л�ȡ���
			String category = (String) inputFromClient.readObject();
			log("����� " + category);

			// �ٸ�������ȡ��Ӧ����Ʒ�б�
			ArrayList<Product> recordingList = myProductDataAccessor.getProducts(category);

			// ����Ʒ�б��͸��ͻ���
			outputToClient.writeObject(recordingList);
			outputToClient.flush();
			log("���� " + recordingList.size() + "����Ʒ��Ϣ���ͻ���.");
		}
		catch (IOException | ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}

	/**
	 * �����û�ע��
	 */
	// ������ע��ʱ���Ѿ����ǹ��ظ��ˣ���������ֱ�ӵ��ü���
	public void opAddUser()
	{
		try
		{
			// ��io��ȡ�������������л�ȡ�û���Ϣ�������û�����
			User user = (User) this.inputFromClient.readObject();
			this.myProductDataAccessor.save(user);
		}
		catch (IOException | ClassNotFoundException e)
		{
			log("�����쳣:  " + e);
			e.printStackTrace();
		}
	}

	/**
	 * ����������Ʒ��������������Ʒ�Ѿ��ڿͻ���ȷ���Ƿ�ԭ���Ѿ�����
	 */
	public void opAddProduct()
	{
		// ��ȡ������Ʒ���������������ļ���
		try
		{
			Product product = (Product) this.inputFromClient.readObject();
			String category = product.getCategory();			// ��ȡ��𣬽����ݴ���dataTable��
			ArrayList<Product> productArrayList = null;			// ��ʼ������¶�Ӧ����Ʒ�б�
			// �ж���������Ʒ����Ƿ����
			// ����������Ҫ�½�
			if(!myProductDataAccessor.dataTable.containsKey(category))
			{
				productArrayList = new ArrayList<Product>();
				myProductDataAccessor.dataTable.put(category, productArrayList);
			}
			else
			{
				productArrayList = myProductDataAccessor.dataTable.get(category);
			}
			// ����Ʒ�����Ӧ�������б���
			productArrayList.add(product);

			this.myProductDataAccessor.save(product);
		}
		catch (IOException | ClassNotFoundException e)
		{
			log("�����쳣:  " + e);
			e.printStackTrace();
		}
	}

	/**
	 * ���ڻ�ȡ���е���Ʒ
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
			log("�����쳣:  " + e);
			e.printStackTrace();
		}
	}

	/**
	 * �޸���Ʒ����
	 * ���������ж�ȡ
	 */
	public void opChangeProduct()
	{
		try
		{
			// �Ȼ�ȡ�ӿͻ����յ���ԭ��Ʒ���޸ĺ��²�Ʒ
			Product pre = (Product) inputFromClient.readObject();
			Product now = (Product) inputFromClient.readObject();
			boolean b = myProductDataAccessor.productChange(pre, now);			// �޸�����
			outputToClient.writeBoolean(b);
			outputToClient.flush();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}


	public void opDeleteProduct()
	{
		log("ɾ����ز�Ʒ");
		try
		{
			Product product = (Product) inputFromClient.readObject();
			myProductDataAccessor.deleteProduct(product);
			log("ɾ���ɹ�");
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			log("�����쳣����");
		}
	}

	/**
	 * �����߳�����ʱ��־
	 * @param flag
	 */
	public void setDone(boolean flag) {
		done = flag;
	}

	/**
	 * ��ӡ��Ϣ������̨
	 * @param msg
	 */
	protected void log(Object msg) {
		System.out.println("������: " + msg);
	}

}