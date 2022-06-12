package com.ascent.ui;

import javax.swing.*;

import com.ascent.bean.Product;
import com.ascent.util.ProductDataClient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.io.*;
import java.awt.event.MouseEvent;


/**
 * 这个类构建产品面板
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProductPanel extends JPanel {

	protected JLabel selectionLabel;

	protected JComboBox categoryComboBox;

	protected JPanel topPanel;

	protected JList productListBox;

	protected JScrollPane productScrollPane;

	//protected JButton detailsButton;

	protected JButton clearButton;

	protected JButton exitButton;

	protected JButton shoppingButton;

	protected JPanel bottomPanel;

	protected MainFrame parentFrame;

	protected ArrayList<Product> productArrayList;

	protected ProductDataClient myDataClient;


	/**
	 * 构建产品面板的构造方法
	 *
	 * @param theParentFrame 面板的父窗体
	 */
	public ProductPanel(MainFrame theParentFrame) {
		try {
			parentFrame = theParentFrame;
			myDataClient = new ProductDataClient();
			selectionLabel = new JLabel("选择类别");
			categoryComboBox = new JComboBox();
			categoryComboBox.addItem("-------");

			ArrayList categoryArrayList = myDataClient.getCategories();

			Iterator iterator = categoryArrayList.iterator();
			String aCategory;

			while (iterator.hasNext()) {
				aCategory = (String) iterator.next();
				categoryComboBox.addItem(aCategory);
			}
			//categoryComboBox.addItemListener();

			topPanel = new JPanel();
			productListBox = new JList();
			productListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			productScrollPane = new JScrollPane(productListBox);

			//detailsButton = new JButton("详细...");
			clearButton = new JButton("清空");
			exitButton = new JButton("退出");
			shoppingButton = new JButton("查看购物车");

			bottomPanel = new JPanel();

			this.setLayout(new BorderLayout());
			JTextField findmy = new JTextField(15);
			JButton okfine= new JButton("搜索");
			okfine.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Find
				}
			});
			topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			topPanel.add(selectionLabel);
			topPanel.add(categoryComboBox);
			topPanel.add(findmy);
			topPanel.add(okfine);
			this.add(BorderLayout.NORTH, topPanel);
			this.add(BorderLayout.CENTER, productScrollPane);

			bottomPanel.setLayout(new FlowLayout());
			bottomPanel.add(shoppingButton);
			//bottomPanel.add(detailsButton);
			bottomPanel.add(clearButton);
			bottomPanel.add(exitButton);

			this.add(BorderLayout.SOUTH, bottomPanel);

			//detailsButton.addActionListener(new DetailsActionListener());
			clearButton.addActionListener(new ClearActionListener());
			exitButton.addActionListener(new ExitActionListener());
			shoppingButton.addActionListener(new ShoppingActionListener());
			categoryComboBox.addItemListener(new GoItemListener());
			JPopupMenu popupMenu = new JPopupMenu();//弹出式菜单
			JMenu copyItem=new JMenu("修改");
			JMenu pasteItem=new JMenu("删除");
			JMenu buyItem = new JMenu("加入购物车");
			buyItem.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					ProductDetailsDialog pd= new ProductDetailsDialog(parentFrame,(Product)productListBox.getSelectedValue(),shoppingButton);



					pd.setVisible(true);
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {

				}
			});
			copyItem.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JOptionPane.showMessageDialog(null, "彬神彬神！", "系统信息", JOptionPane.INFORMATION_MESSAGE);
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {

				}
			});
			pasteItem.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JOptionPane.showMessageDialog(null, "彬神彬神！", "系统信息", JOptionPane.INFORMATION_MESSAGE);
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {

				}
			});
			popupMenu.add(copyItem);
			popupMenu.add(pasteItem);
			popupMenu.add(buyItem);
			productListBox.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3)
					{	System.out.println(productListBox.getSelectedValue());
						popupMenu.show(productListBox, e.getX(), e.getY());
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {

				}
			});
			/**
			 * 处理选中分类列表中选项时触发的事件监听器
			 *
			 * @author ascent
			 */

			clearButton.setEnabled(false);
			//shoppingButton.setEnabled(false);

		} catch (IOException exc) {
			JOptionPane.showMessageDialog(this, "网络问题 " + exc, "网络问题", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * 设置下拉列选中的分类选项
	 */
	protected void populateListBox() {
		try {
			String category = (String) categoryComboBox.getSelectedItem();
			if (!category.startsWith("---")) {
				productArrayList = myDataClient.getProducts(category);
			} else {
				productArrayList = new ArrayList<Product>();
			}

			Object[] theData = productArrayList.toArray();

			System.out.println(productArrayList + ">>>>>>>>>>>");
			productListBox.setListData(theData);
			productListBox.updateUI();

			if (productArrayList.size() > 0) {
				clearButton.setEnabled(true);
			} else {
				clearButton.setEnabled(false);
			}
		} catch (IOException exc) {
			JOptionPane.showMessageDialog(this, "网络问题: " + exc, "网络问题", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}


	/**
	 * 处理选择查看购物车按钮时触发的事件监听器
	 *
	 * @author ascent
	 */
	class ShoppingActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ShoppingCartDialog myShoppingDialog = new ShoppingCartDialog(
					parentFrame, shoppingButton);
			myShoppingDialog.setVisible(true);
		}
	}

	/**
	 * 处理选择退出按钮时触发的事件监听器
	 *
	 * @author ascent
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			parentFrame.exit();
		}
	}

	/**
	 * 处理选择清空按钮时触发的事件监听器
	 *
	 * @author ascent
	 */
	class ClearActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object[] noData = new Object[1];
			productListBox.setListData(noData);
			categoryComboBox.setSelectedIndex(0);
		}
	}

	/**
	 * 处理选中分类下拉列选的选项时触发的事件监听器
	 *
	 * @author ascent
	 */
	class GoItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				populateListBox();
			}
		}
	}


}