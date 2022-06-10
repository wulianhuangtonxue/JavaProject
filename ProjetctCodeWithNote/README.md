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

