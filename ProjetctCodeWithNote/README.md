# README

只做了User类，Ascentsys类，LoginFrame类，RegistFrame类，ProtocolPort接口，UserDataClient类文件的注释。

其他部分后续更新。



////

## 新的更新

这次更新了MainFrame，ProductPane以及几个产品后台数据的注释。

更新了新增Product的接口，同时写了一个可以用上的获取所有Product的接口。

```java
// 获取所有的商品
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


// 新增商品
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
```





## Update

更新了增删查改，并且应该可以将数据保存在文件中。





## Update

新增了获取所有用户的接口，该接口区别于之前的接口，返回一个二维数组，用于区分普通用户和一般用户。

如下所示

```java
	/**
	 * 获取两种用户，分别为普通用户和注册用户
	 * 返回一个二维对象数组，第一个存储普通用户，第二个存储管理者
	 * @return
	 */
	public ArrayList<ArrayList<User>> getTwoKindUsers()
	{
		// 新建需要返回的答案
		ArrayList<ArrayList<User>> twoUsers = new ArrayList<ArrayList<User>>();
		ArrayList<User> ordinaryUsers = new ArrayList<User>();				// 一般用户
		ArrayList<User> controller = new ArrayList<User>();					// 管理者
		HashMap<String, User> userHashMap = getUsers();						// 调用函数获取用户集

		// 遍历获取到的用户对象
		for(User user : userHashMap.values())
		{
			// 根据权限判断是普通还是管理者
			if(user.getAuthority() == 0)
			{
				ordinaryUsers.add(user);
			}
			else
			{
				controller.add(user);
			}
		}
		// 对应加就行了
		twoUsers.add(ordinaryUsers);
		twoUsers.add(controller);

		return twoUsers;
	}

```

