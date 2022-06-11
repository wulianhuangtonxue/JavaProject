package com.ascent.util;

import java.util.*;
import java.io.*;
// �����Ʒ�����û���
import com.ascent.bean.Product;
import com.ascent.bean.User;

/**
 * ��Ʒ���ݶ�ȡ��ʵ����
 * @author ascent
 * @version 1.0
 */
// �̳����ݴ洢���������ࣩ
public class ProductDataAccessor extends DataAccessor {

	// ////////////////////////////////////////////////////
	//
	// ��Ʒ�ļ���ʽ����
	// ��Ʒ����,��ѧ��ժ�ǼǺ�,�ṹͼ,��ʽ,�۸�,����,���
	// ----------------------------------------------------
	//
	// ////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////
	//
	// �û��ļ���ʽ����
	// �û��ʺ�,�û�����,�û�Ȩ��
	// ----------------------------------------------------
	//
	// ////////////////////////////////////////////////////
	/**
	 * ��Ʒ��Ϣ�����ļ���
	 */
	protected static final String PRODUCT_FILE_NAME = "product.db";

	/**
	 * �û���Ϣ�����ļ���
	 */
	protected static final String USER_FILE_NAME = "user.db";

	/**
	 * ���ݼ�¼�ķָ��
	 */
	protected static final String RECORD_SEPARATOR = "----------";

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public ProductDataAccessor() {
		load();
	}

	/**
	 * ��ȡ���ݵķ���
	 */
	@Override
	public void load()
	{
		// �����������Ա
		dataTable = new HashMap<String,ArrayList<Product>>();
		userTable = new HashMap<String,User>();

		ArrayList<Product> productArrayList = null;
		// ��������һ��Ӧ�ó������һ�����ƣ�tokens��
		StringTokenizer st = null;

		Product productObject = null;	// ��Ʒ����
		User userObject = null;			// �û�����
		String line = "";

		String productName, cas, structure, formula, price, realstock, category;
		String userName, password, authority;

		try {
			log("��ȡ�ļ�: " + PRODUCT_FILE_NAME + "...");		// �����־
			// �����Ķ�����������Ϊ���ļ���ȡ���������
			BufferedReader inputFromFile1 = new BufferedReader(new FileReader(PRODUCT_FILE_NAME));

			// ���ж�ȡ��ֱ�������ļ�
			while ((line = inputFromFile1.readLine()) != null)
			{
				// ��� ','
				st = new StringTokenizer(line, ",");

				// nextToken���ڱ�ʾ��ȡ��һ��token����ʵ�������Ϊ����
				// trim����ȥ�����˵Ŀո񣨿հ��ַ���
				// ���Ƕ�ȡһ�е���Ϣ����ʵ����һ����Ʒ����Ϣ
				productName = st.nextToken().trim();
				cas = st.nextToken().trim();
				structure = st.nextToken().trim();
				formula = st.nextToken().trim();
				price = st.nextToken().trim();
				realstock = st.nextToken().trim();
				category = st.nextToken().trim();

				// ����һ����Ʒ����
				productObject = getProductObject(productName, cas, structure,formula, price, realstock, category);

				// ������е����ݱ����Ƿ���������
				// �����������ֱ��ͨ������ȡ��Ӧ�Ĳ�Ʒ�б�
				if (dataTable.containsKey(category))
				{
					productArrayList = dataTable.get(category);
				}
				// ��������������½�һ���б�
				else
				{
					productArrayList = new ArrayList<Product>();
					// �����ݱ�����Ӹ���
					dataTable.put(category, productArrayList);
				}
				// �б���ӵ�ǰ�Ĳ�Ʒ
				productArrayList.add(productObject);
			}

			// �رջ����Ķ�������ʵ���ǹر�io
			inputFromFile1.close();
			log("�ļ���ȡ����!");

			// Ȼ��ʼ��ȡ�û���Ϣ�ļ�
			line = "";
			log("��ȡ�ļ�: " + USER_FILE_NAME + "...");
			BufferedReader inputFromFile2 = new BufferedReader(new FileReader(USER_FILE_NAME));
			while ((line = inputFromFile2.readLine()) != null)
			{

				st = new StringTokenizer(line, ",");

				userName = st.nextToken().trim();
				password = st.nextToken().trim();
				authority = st.nextToken().trim();
				userObject = new User(userName, password, Integer.parseInt(authority));

				// ����û��Ƿ��ظ�
				if (!userTable.containsKey(userName))
				{
					userTable.put(userName, userObject);
				}
			}

			inputFromFile2.close();
			log("�ļ���ȡ����!");
			log("׼������!\n");
		}
		// �����ļ����Ҵ���
		catch (FileNotFoundException exc)
		{
			log("û���ҵ��ļ�: " + PRODUCT_FILE_NAME + " �� "+USER_FILE_NAME+".");
			log(exc);
		}
		// ����IO����
		catch (IOException exc)
		{
			log("��ȡ�ļ������쳣: " + PRODUCT_FILE_NAME+ " �� "+USER_FILE_NAME+".");
			log(exc);
		}
	}

	/**
	 * ���ش�����Щ��������Ʒ����
	 * @param productName ҩƷ����
	 * @param cas ��ѧ��ժ�ǼǺ�
	 * @param structure �ṹͼ����
	 * @param formula ��ʽ
	 * @param price �۸�
	 * @param realstock ����
	 * @param category ���
	 * @return new Product(productName, cas, structure, formula, price, realstock, category);
	 */
	private Product getProductObject(String productName, String cas,
			String structure, String formula, String price, String realstock, String category)
	{
		return new Product(productName, cas, structure, formula, price, realstock, category);
	}

	/**
	 * ��������
	 */
	// �ȽϿ�ϧ��ֻ���û��ļ�����
	@Override
	public void save(User user)
	{
		log("��ȡ�ļ�: " + USER_FILE_NAME + "...");
		try
		{
			String userinfo = user.getUsername() + "," + user.getPassword() + "," + user.getAuthority();
			RandomAccessFile fos = new RandomAccessFile(USER_FILE_NAME, "rws");
			fos.seek(fos.length());
			fos.write(("\n" + userinfo).getBytes());
			fos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ����������Ʒ����ı���
	 * @param product ��Ʒ����
	 */
	@Override
	public void save(Product product)
	{
		try
		{
			String productInfo = product.getProductname() + "," + product.getCas() + "," +
					product.getStructure() + "," + product.getFormula() + "," + product.getPrice()
					+ "," + product.getRealstock() + "," +product.getCategory();
			RandomAccessFile fos = new RandomAccessFile(PRODUCT_FILE_NAME, "rws");
			fos.seek(fos.length());
			fos.write(("\n" + productInfo).getBytes());
			fos.close();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ��־����.
	 */
	@Override
	protected void log(Object msg)
	{
		System.out.println("ProductDataAccessor��: " + msg);
	}

	// ��д�����û���Ϣ����
	@Override
	public HashMap<String,User> getUsers()
	{
		this.load();
		return this.userTable;
	}

	// дһ����ȡ��ǰ������Ʒ�ĺ���
	public HashMap<String, Product> getAllProducts()
	{
		// �����ļ�
		this.load();
		HashMap<String, Product> productTable = null;
		for(ArrayList<Product> products : dataTable.values())
		{
			for(Product product : products)
			{
				String key = product.getProductname();
				productTable.put(key, product);
			}
		}
		return productTable;
	}

	/**
	 * ���±����Ʒ����
	 * ����ÿ��һ�ζ�product�����ļ����޸ĺ���Ҫ���±���
	 * ���ｫ�����ݱ�����dataTable��
	 */
	@Override
	public void rSave()
	{
		log("���´洢product�����ļ�");
		log("����ļ������е�����");
		try {
			File file = new File(PRODUCT_FILE_NAME);
			FileWriter fw = new FileWriter(file);
		}
		catch (IOException e)
		{
			log("�������ʧ��");
			e.printStackTrace();
		}
		// ����ÿ������Ӧ����Ʒ�б�
		for(ArrayList<Product> productArrayList : dataTable.values())
		{
			// ���б��е���Ʒ����������
			Collections.sort(productArrayList);
			// ������ǰ�б�Ĳ�Ʒ����
			for(Product product : productArrayList)
			{
				// ֱ�ӵ���product�ı��溯��
				save(product);
			}
		}
	}

	/**
	 * �޸�Product����
	 * @param pre ԭProduct
	 * @param now �µ�Product
	 */
	public boolean productChange(Product pre, Product now)
	{
		// ��ȡ���
		String category = pre.getCategory();
		ArrayList<Product> productArrayList = dataTable.get(category);		// ��ȡ��Ӧ�б�
		Product p = null;
		for (Product p1 : productArrayList)
		{
			if(p1.isEqual(pre))
			{
				p = p1;
				break;
			}
		}
		if(p != null && !p.isEqual(now))
		{
			log("���Ը�");
			// ����Ʒ�ȴ��б����Ƴ�
			productArrayList.remove(p);
			category = now.getCategory();				// ��ȡ�µ����
			// Ȼ���ж�����Ƿ����
			if(!dataTable.containsKey(category))
			{
				// ���������½�һ���µ����
				productArrayList = new ArrayList<Product>();
				dataTable.put(category, productArrayList);
			}
			// �Ȼ�ȡ�µ��б�
			productArrayList = dataTable.get(category);
			boolean b1 = false;
			// �������µĲ�Ʒ�Ƿ�����ͬ��Ʒ
			for(Product p2 : productArrayList)
			{
				if(p2.isEqual(now))
				{
					b1 = true;
					break;
				}
			}
			if(b1)
			{
				log("�޸ĺ�������ظ�");
				return false;
			}
			productArrayList.add(now);
			rSave();				// ��������
			log("�ĺ���");
			return true;
		}
		else
		{
			log("���ܸģ�̫���ˣ��Ķ�����ģ�");
			return false;
		}
	}

	/**
	 * ɾ����ز�Ʒ
	 * @param product ��ɾ���Ĳ�Ʒ
	 */
	public void deleteProduct(Product product)
	{
		String category = product.getCategory();
		ArrayList<Product> productArrayList = dataTable.get(category);
		productArrayList.remove(product);

		rSave();
	}
}