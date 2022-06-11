package com.ascent.util;

import java.util.*;
import java.io.*;
// 导入产品包和用户包
import com.ascent.bean.Product;
import com.ascent.bean.User;

/**
 * 产品数据读取的实现类
 * @author ascent
 * @version 1.0
 */
// 继承数据存储器（抽象类）
public class ProductDataAccessor extends DataAccessor {

	// ////////////////////////////////////////////////////
	//
	// 产品文件格式如下
	// 产品名称,化学文摘登记号,结构图,公式,价格,数量,类别
	// ----------------------------------------------------
	//
	// ////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////
	//
	// 用户文件格式如下
	// 用户帐号,用户密码,用户权限
	// ----------------------------------------------------
	//
	// ////////////////////////////////////////////////////
	/**
	 * 商品信息数据文件名
	 */
	protected static final String PRODUCT_FILE_NAME = "product.db";

	/**
	 * 用户信息数据文件名
	 */
	protected static final String USER_FILE_NAME = "user.db";

	/**
	 * 数据记录的分割符
	 */
	protected static final String RECORD_SEPARATOR = "----------";

	/**
	 * 默认构造方法
	 */
	public ProductDataAccessor() {
		load();
	}

	/**
	 * 读取数据的方法
	 */
	@Override
	public void load()
	{
		// 基类的两个成员
		dataTable = new HashMap<String,ArrayList<Product>>();
		userTable = new HashMap<String,User>();

		ArrayList<Product> productArrayList = null;
		// 此类允许一个应用程序进入一个令牌（tokens）
		StringTokenizer st = null;

		Product productObject = null;	// 产品对象
		User userObject = null;			// 用户对象
		String line = "";

		String productName, cas, structure, formula, price, realstock, category;
		String userName, password, authority;

		try {
			log("读取文件: " + PRODUCT_FILE_NAME + "...");		// 输出日志
			// 缓冲阅读对象，用来作为从文件读取的输入对象
			BufferedReader inputFromFile1 = new BufferedReader(new FileReader(PRODUCT_FILE_NAME));

			// 分行读取，直至读完文件
			while ((line = inputFromFile1.readLine()) != null)
			{
				// 标记 ','
				st = new StringTokenizer(line, ",");

				// nextToken用于表示获取下一个token，其实可以理解为分组
				// trim用于去除两端的空格（空白字符）
				// 就是读取一行的信息，其实就是一种商品的信息
				productName = st.nextToken().trim();
				cas = st.nextToken().trim();
				structure = st.nextToken().trim();
				formula = st.nextToken().trim();
				price = st.nextToken().trim();
				realstock = st.nextToken().trim();
				category = st.nextToken().trim();

				// 创建一个商品对象
				productObject = getProductObject(productName, cas, structure,formula, price, realstock, category);

				// 检测已有的数据表中是否包含该类别
				// 如果包含，则直接通过类别获取对应的产品列表
				if (dataTable.containsKey(category))
				{
					productArrayList = dataTable.get(category);
				}
				// 如果不包含，则新建一个列表
				else
				{
					productArrayList = new ArrayList<Product>();
					// 将数据表中添加该类
					dataTable.put(category, productArrayList);
				}
				// 列表添加当前的产品
				productArrayList.add(productObject);
			}

			// 关闭缓冲阅读器，其实就是关闭io
			inputFromFile1.close();
			log("文件读取结束!");

			// 然后开始读取用户信息文件
			line = "";
			log("读取文件: " + USER_FILE_NAME + "...");
			BufferedReader inputFromFile2 = new BufferedReader(new FileReader(USER_FILE_NAME));
			while ((line = inputFromFile2.readLine()) != null)
			{

				st = new StringTokenizer(line, ",");

				userName = st.nextToken().trim();
				password = st.nextToken().trim();
				authority = st.nextToken().trim();
				userObject = new User(userName, password, Integer.parseInt(authority));

				// 检测用户是否重复
				if (!userTable.containsKey(userName))
				{
					userTable.put(userName, userObject);
				}
			}

			inputFromFile2.close();
			log("文件读取结束!");
			log("准备就绪!\n");
		}
		// 捕获文件查找错误
		catch (FileNotFoundException exc)
		{
			log("没有找到文件: " + PRODUCT_FILE_NAME + " 或 "+USER_FILE_NAME+".");
			log(exc);
		}
		// 捕获IO错误
		catch (IOException exc)
		{
			log("读取文件发生异常: " + PRODUCT_FILE_NAME+ " 或 "+USER_FILE_NAME+".");
			log(exc);
		}
	}

	/**
	 * 返回带有这些参数的商品对象
	 * @param productName 药品名称
	 * @param cas 化学文摘登记号
	 * @param structure 结构图名称
	 * @param formula 公式
	 * @param price 价格
	 * @param realstock 数量
	 * @param category 类别
	 * @return new Product(productName, cas, structure, formula, price, realstock, category);
	 */
	private Product getProductObject(String productName, String cas,
			String structure, String formula, String price, String realstock, String category)
	{
		return new Product(productName, cas, structure, formula, price, realstock, category);
	}

	/**
	 * 保存数据
	 */
	// 比较可惜，只有用户文件保存
	@Override
	public void save(User user)
	{
		log("读取文件: " + USER_FILE_NAME + "...");
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
	 * 用于新增产品对象的保存
	 * @param product 产品对象
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
	 * 日志方法.
	 */
	@Override
	protected void log(Object msg)
	{
		System.out.println("ProductDataAccessor类: " + msg);
	}

	// 重写，将用户信息返回
	@Override
	public HashMap<String,User> getUsers()
	{
		this.load();
		return this.userTable;
	}

	// 写一个获取当前所有商品的函数
	public HashMap<String, Product> getAllProducts()
	{
		// 加载文件
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
	 * 重新保存产品数据
	 * 我们每做一次对product数据文件的修改后，需要重新保存
	 * 这里将将数据保存在dataTable中
	 */
	@Override
	public void rSave()
	{
		log("重新存储product数据文件");
		log("清空文件内已有的数据");
		try {
			File file = new File(PRODUCT_FILE_NAME);
			FileWriter fw = new FileWriter(file);
		}
		catch (IOException e)
		{
			log("数据清空失败");
			e.printStackTrace();
		}
		// 遍历每个类别对应的商品列表
		for(ArrayList<Product> productArrayList : dataTable.values())
		{
			// 将列表中的商品按名字排列
			Collections.sort(productArrayList);
			// 遍历当前列表的产品数据
			for(Product product : productArrayList)
			{
				// 直接调用product的保存函数
				save(product);
			}
		}
	}

	/**
	 * 修改Product数据
	 * @param pre 原Product
	 * @param now 新的Product
	 */
	public boolean productChange(Product pre, Product now)
	{
		// 获取类别
		String category = pre.getCategory();
		ArrayList<Product> productArrayList = dataTable.get(category);		// 获取对应列表
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
			log("可以改");
			// 将产品先从列表中移除
			productArrayList.remove(p);
			category = now.getCategory();				// 获取新的类别
			// 然后判断类别是否存在
			if(!dataTable.containsKey(category))
			{
				// 不存在则新建一个新的类别
				productArrayList = new ArrayList<Product>();
				dataTable.put(category, productArrayList);
			}
			// 先获取新的列表
			productArrayList = dataTable.get(category);
			boolean b1 = false;
			// 遍历看新的产品是否有相同产品
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
				log("修改后的内容重复");
				return false;
			}
			productArrayList.add(now);
			rSave();				// 更新数据
			log("改好了");
			return true;
		}
		else
		{
			log("不能改，太菜了，改都不会改！");
			return false;
		}
	}

	/**
	 * 删除相关产品
	 * @param product 待删除的产品
	 */
	public void deleteProduct(Product product)
	{
		String category = product.getCategory();
		ArrayList<Product> productArrayList = dataTable.get(category);
		productArrayList.remove(product);

		rSave();
	}
}