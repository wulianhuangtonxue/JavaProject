package com.ascent.ui;

import javax.swing.*;
import javax.swing.event.*;

// 导入产品和产品数据类
import com.ascent.bean.Product;
import com.ascent.util.ProductDataClient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.io.*;

/**
 * 这个类构建产品面板
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProductPanel extends JPanel
{

	protected Client openClient;                //客户端
	protected JLabel selectionLabel;			// 选择标签

	protected JComboBox categoryComboBox;		// 组合框

	protected JPanel topPanel;					// 顶部面板

	protected JList productListBox;				// 产品列表

	protected JScrollPane productScrollPane;	// 产品移动面板

	protected JButton client;                   //客户端按钮

	protected JButton detailsButton;			// 详细按钮

	protected JButton clearButton;				// 清空按钮

	protected JButton exitButton;				// 退出按钮

	protected JButton shoppingButton;			// 查看购物车按钮

	protected JPanel bottomPanel;				// 底部面板

	protected MainFrame parentFrame;			// 父窗口，就是MainFrame

	protected ArrayList<Product> productArrayList;	// 产品数组列表

	protected ProductDataClient myDataClient;		// 产品数据对象

	/**
	 * 构建产品面板的构造方法
	 * @param theParentFrame 面板的父窗体
	 */
	public ProductPanel(MainFrame theParentFrame)
	{
		try
		{
			// 父窗口
			parentFrame = theParentFrame;
			// 新建一个产品数据类对象，其实就是准备获取产品数据
			myDataClient = new ProductDataClient();
			// 选择标签加个名字
			selectionLabel = new JLabel("选择类别");
			// 新建一个组合框对象
			categoryComboBox = new JComboBox();
			// 往类别组合框加----- 类别事项
			categoryComboBox.addItem("-------");

			// 从产品数据获取产品类别集合
			ArrayList categoryArrayList = myDataClient.getCategories();

			// 迭代器对象 从类别集合数组获取迭代器
			Iterator iterator = categoryArrayList.iterator();
			// 当前类别
			String aCategory;

			// 迭代器后还有
			while (iterator.hasNext())
			{
				// 获取对应的类别
				aCategory = (String) iterator.next();
				// 将类别加入类别组合框中
				categoryComboBox.addItem(aCategory);
			}

			// 对应新建对象
			topPanel = new JPanel();
			productListBox = new JList();
			// 设置选择方式-----单个选择
			productListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// 产品滑动面板
			productScrollPane = new JScrollPane(productListBox);

			// 新建五个按钮
			detailsButton = new JButton("详细...");
			clearButton = new JButton("清空");
			exitButton = new JButton("退出");
			shoppingButton = new JButton("查看购物车");
			client = new JButton("联系客服");

			// 底部面板
			bottomPanel = new JPanel();

			// 设置为盒式布局
			this.setLayout(new BorderLayout());

			// 顶部设置为流式布局，且居左
			topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			// 将选择标签和类别组合框加入到顶部
			topPanel.add(selectionLabel);
			topPanel.add(categoryComboBox);

			// 将顶部面板设置于顶部
			this.add(BorderLayout.NORTH, topPanel);
			// 将产品滑动面板加入到盒式布局的中间
			this.add(BorderLayout.CENTER, productScrollPane);

			// 底部面板设置为流式布局
			bottomPanel.setLayout(new FlowLayout());
			bottomPanel.add(client);
			bottomPanel.add(shoppingButton);
			bottomPanel.add(detailsButton);
			bottomPanel.add(clearButton);
			bottomPanel.add(exitButton);

			// 将底部面板加入到盒式布局的底部
			this.add(BorderLayout.SOUTH, bottomPanel);

			// 给按钮还有类别框，产品滑动框添加对应的事件
			// 详细
			detailsButton.addActionListener(new DetailsActionListener());
			// 清空
			clearButton.addActionListener(new ClearActionListener());
			// 退出
			exitButton.addActionListener(new ExitActionListener());
			// 购物车按钮
			shoppingButton.addActionListener(new ShoppingActionListener());
			// 客户端按钮
			client.addActionListener(new clientActionListener());
			// 类别组合框
			categoryComboBox.addItemListener(new GoItemListener());
			// 产品列表
			productListBox.addListSelectionListener(new ProductListSelectionListener());

			// 初始时除了退出按钮都设置为不可点击
			detailsButton.setEnabled(false);
			clearButton.setEnabled(false);
			shoppingButton.setEnabled(false);

		}
		// 捕获IO异常
		catch (IOException exc)
		{
			JOptionPane.showMessageDialog(this, "网络问题 " + exc, "网络问题", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * 设置下拉列选中的分类选项
	 */
	protected void populateListBox()
	{
		try
		{
			// 获取类别
			String category = (String) categoryComboBox.getSelectedItem();
			// 只要不是空类别，就获取对应的产品数据
			if (!category.startsWith("---"))
			{
				productArrayList = myDataClient.getProducts(category);
			}
			// 否则直接新建一个空的产品数据
			else
			{
				productArrayList = new ArrayList<Product>();
			}

			// 将data数组化
			Object[] theData = productArrayList.toArray();

			// 打印对应的产品信息
			System.out.println(productArrayList + ">>>>>>>>>>>");
			// 设置列表数据
			productListBox.setListData(theData);
			// 更新UI，这里其实更新界面
			productListBox.updateUI();

			// 如果产品的数量大于0，清空按钮就设为可点击
			if (productArrayList.size() > 0)
			{
				clearButton.setEnabled(true);
			}
			else
			{
				clearButton.setEnabled(false);
			}
		}
		// 捕获IO异常
		catch (IOException exc)
		{
			JOptionPane.showMessageDialog(this, "网络问题: " + exc, "网络问题", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} 
	}

	/**
	 * 处理选择详细...按钮时触发的事件监听器
	 * @author ascent
	 */
	class DetailsActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// 从产品中获取索引
			int index = productListBox.getSelectedIndex();
			// 利用索引获取产品
			Product product = (Product) productArrayList.get(index);
			// 新建详细界面
			ProductDetailsDialog myDetailsDialog = new ProductDetailsDialog(parentFrame, product, shoppingButton);
			// 设置详细界面可见
			myDetailsDialog.setVisible(true);
		}
	}

	/**
	 * 处理选择查看购物车按钮时触发的事件监听器
	 * @author ascent
	 */
	class ShoppingActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// 新建一个购物车界面
			// 传入的参数包含购物车的按钮
			ShoppingCartDialog myShoppingDialog = new ShoppingCartDialog(
					parentFrame, shoppingButton);
			// 设置可见
			myShoppingDialog.setVisible(true);
		}
	}

	/**
	 * 处理选择退出按钮时触发的事件监听器
	 * @author ascent
	 */
	// 调用主界面退出
	class ExitActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event) {
			parentFrame.exit();
		}
	}

	/**
	 * 处理选择清空按钮时触发的事件监听器
	 * @author ascent
	 */
	class ClearActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// 无数据
			Object[] noData = new Object[1];
			// 将数据清空
			productListBox.setListData(noData);
			categoryComboBox.setSelectedIndex(0);
		}
	}

	/**
	 * 处理选中分类下拉列选的选项时触发的事件监听器
	 * @author ascent
	 */
	class GoItemListener implements ItemListener
	{
		// 重写item状态改变函数
		public void itemStateChanged(ItemEvent event)
		{
			if (event.getStateChange() == ItemEvent.SELECTED)
			{
				populateListBox();
			}
		}
	}

	/**
	 * 处理选中分类列表中选项时触发的事件监听器
	 * @author ascent
	 */
	class ProductListSelectionListener implements ListSelectionListener
	{
		// 重写值是否改变函数——其实就是item是否被选取
		public void valueChanged(ListSelectionEvent event)
		{
			// 如果没有被选取
			if (productListBox.isSelectionEmpty())
			{
				// 详细按钮不显示
				detailsButton.setEnabled(false);
			}
			else
			{
				detailsButton.setEnabled(true);
			}
		}
	}
	/**
	 * 处理客户端按钮的事件监听器
	 * @author ascent
	 */
	class clientActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			openClient = new Client();
		}
	}
}