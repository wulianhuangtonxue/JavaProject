package com.ascent.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.ascent.bean.Product;
import com.ascent.util.ShoppingCart;

/**
 * 这个类显示产品详细信息对话框
 * @author ascent
 * @version 1.0
 */

@SuppressWarnings("serial")
public class ProductDetailsDialog extends JDialog
{

	protected Product myProduct;

	protected Frame parentFrame;

	protected JButton shoppingButton;

	/**
	 * 带三个参数的构造方法
	 * @param theParentFrame 父窗体
	 * @param theProduct 当前查看的商品对象
	 * @param shoppingButton 购买按钮
	 */
	public ProductDetailsDialog(Frame theParentFrame, Product theProduct,
			JButton shoppingButton)
	{
		this(theParentFrame, "药品详细信息 " + theProduct.getProductname(),
				theProduct, shoppingButton);
	}

	/**
	 * 带四个参数的构造方法
	 * @param theParentFrame 父窗体
	 * @param theTitle 窗体标题
	 * @param theProduct 当前查看的商品对象
	 * @param shoppingButton 购买按钮
	 */
	public ProductDetailsDialog(Frame theParentFrame, String theTitle,
			Product theProduct, JButton shoppingButton)
	{
		// 调用超类构造
		super(theParentFrame, theTitle, true);

		// 对应成员用参数直接构建
		myProduct = theProduct;
		parentFrame = theParentFrame;
		this.shoppingButton = shoppingButton;

		// 调用函数构建商品信息
		buildGui();
	}

	/**
	 * 用来构建显示商品信息窗体
	 */
	private void buildGui()
	{
		// 获取框体
		Container container = this.getContentPane();

		// 设置布局边界布局
		container.setLayout(new BorderLayout());

		// 新建顶部面板
		JPanel topPanel = new JPanel();

		// 将 顶部面板加入到盒式布局（才发现之前说错了，之前都是边界布局）
		// 盒式以从左到右进行布局
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		// 新增信息面板
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

		// 将信息面板设置对应布局
		infoPanel.setLayout(new GridBagLayout());

		// 一个布局管理器
		GridBagConstraints c = new GridBagConstraints();

		// 对布局的对应的各种属性的设置
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;

		// 这里其实一个区域的大小
		c.insets = new Insets(10, 0, 2, 10);
		JLabel artistLabel = new JLabel("产品名:  " + myProduct.getProductname());
		artistLabel.setForeground(Color.black);
		infoPanel.add(artistLabel, c);

		c.gridy = GridBagConstraints.RELATIVE;
		c.insets = new Insets(2, 0, 10, 10);
		JLabel titleLabel = new JLabel("CAS号:  " + myProduct.getCas());
		titleLabel.setForeground(Color.black);
		infoPanel.add(titleLabel, c);

		JLabel categoryLabel = new JLabel("公式:  " + myProduct.getFormula());
		c.insets = new Insets(2, 0, 2, 0);
		categoryLabel.setForeground(Color.black);
		infoPanel.add(categoryLabel, c);

		JLabel durationLabel = new JLabel("数量:  " + myProduct.getRealstock());
		durationLabel.setForeground(Color.black);
		infoPanel.add(durationLabel, c);

		JLabel priceLabel = new JLabel("类别： " + myProduct.getCategory());
		c.insets = new Insets(10, 0, 2, 0);
		priceLabel.setForeground(Color.black);
		infoPanel.add(priceLabel, c);


		//重新调整布局
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 5;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(5, 5, 20, 0);
		String imageName = myProduct.getStructure();
		ImageIcon recordingIcon = null;
		JLabel recordingLabel = null;

		// 读取图片
		try
		{
			if (imageName.trim().length() == 0)
			{
				recordingLabel = new JLabel("  图片不存在  ");
			}
			else
			{
				recordingIcon = new ImageIcon(getClass().getResource("/images/" + imageName));
				recordingLabel = new JLabel(recordingIcon);
			}
		}
		catch (Exception exc)
		{
			recordingLabel = new JLabel("  图片不存在  ");
		}

		// 设置图片标签的便签
		recordingLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		recordingLabel.setToolTipText(myProduct.getProductname());

		infoPanel.add(recordingLabel, c);

		container.add(BorderLayout.NORTH, infoPanel);

		JPanel bottomPanel = new JPanel();
		JButton okButton = new JButton("关闭");
		bottomPanel.add(okButton);
		JButton purchaseButton = new JButton("加入购物车");
		bottomPanel.add(purchaseButton);
		container.add(BorderLayout.SOUTH, bottomPanel);

		// 添加监听事件
		okButton.addActionListener(new OkButtonActionListener());
		purchaseButton.addActionListener(new PurchaseButtonActionListener());

		this.pack();

		// 获取主窗口的位置
		Point parentLocation = parentFrame.getLocation();
		// 设置位置
		this.setLocation(parentLocation.x + 50, parentLocation.y + 50);
	}

	/**
	 * 处理"OK"按钮的内部类
	 */
	class OkButtonActionListener implements ActionListener
	{
		// 点击ok，关闭详细界面
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
		}
	}

	/**
	 * 处理"购买"按钮的内部类
	 */
	class PurchaseButtonActionListener implements ActionListener
	{
		// 创建购物车对象
		ShoppingCart shoppingCar = new ShoppingCart();
		public void actionPerformed(ActionEvent event)
		{
			// 将商品加入到购物车
			shoppingCar.addProduct(myProduct);
			// 购物车按钮设为可见
			shoppingButton.setEnabled(true);
			// 关闭详情界面
			setVisible(false);
		}
	}
}