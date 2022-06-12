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

		// 返回类别集合
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
	 * 该函数从服务器端获取文件中所有的product
	 * @return 返回一个哈希表，产品名字为键，值为产品对象
	 * @throws IOException
	 */
	public HashMap<String, Product> getAllProducts()
	{
		HashMap<String, Product> productTable = null;

		try
		{
			log("发送请求: OP_GET_ALL_PRODUCTS");

			// 输出流
			outputToServer.writeInt(ProtocolPort.OP_GET_ALL_PRODUCTS);
			outputToServer.flush();

			log("接收数据...");
			productTable = (HashMap<String, Product>) inputFromServer.readObject();
		}
		// 捕获异常
		catch (ClassNotFoundException | IOException e)
		{
			e.printStackTrace();
		}
		return productTable;
	}

	/**
	 * 新增商品 返回是否可以新增输入的商品
	 * 如果可以则会在文件中存入
	 * @param productName
	 * @param cas
	 * @param structure
	 * @param formula
	 * @param price
	 * @param realStock
	 * @param category
	 * @return true-新增成功，false-新增失败
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
			log("发送请求:OP_ADD_PRODUCT");
			outputToServer.writeInt(ProtocolPort.OP_ADD_PRODUCT);
			outputToServer.writeObject(new Product(productName, cas, structure, formula, price,
					realStock, category));
			outputToServer.flush();
			log("接收数据...");
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除产品
	 * 会先按名字判断是否有该产品，如果没有，则直接返回错误
	 * 如果没有，就调用相关的函数，删除产品数据，并且会更新在数据文件中
	 * @param product 要删除的产品
	 * @return 返回删除是否成功
	 */
	public boolean deleteProduct(Product product)
	{
		try
		{
			HashMap<String, Product> products = getAllProducts();
			if(!products.containsKey(product.getProductname()))
			{
				log("没有该产品，无法删除");
				return false;
			}
			// 删除产品
			outputToServer.writeInt(ProtocolPort.OP_DELETE_PRODUCT);
			outputToServer.writeObject(product);
			outputToServer.flush();
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 修改接口，从前端接收，修改前后的商品对象
	 * @param pre 修改前的对象
	 * @param now 修改后的商品
	 * @return 返回是否修改成功
	 */
	public boolean changeProduct(Product pre, Product now)
	{
		try
		{
			outputToServer.writeInt(ProtocolPort.OP_CHANGE);
			outputToServer.writeObject(pre);
			outputToServer.writeObject(now);
			outputToServer.flush();
			boolean b = inputFromServer.readBoolean();
			if(b)
			{
				log("修改成功");
				return true;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		log("修改失败");
		return false;
	}

	/**
	 * 查找商品函数
	 * 从前端获取查找的商品名字
	 * @param name 商品名字
	 * @return 商品对象
	 */
	public Product findProduct(String name)
	{
		HashMap<String, Product> products = getAllProducts();
		Product product = null;
		if(products.containsKey(name))
		{
			product = products.get(name);
		}
		return product;
	}

	/**
	 * 日志方法.
	 */
	protected void log(Object msg) {
		System.out.println("ProductDataClient类: " + msg);
	}
}