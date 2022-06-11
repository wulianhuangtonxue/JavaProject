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
 * ����๹����Ʒ���
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
	 * ������Ʒ���Ĺ��췽��
	 *
	 * @param theParentFrame ���ĸ�����
	 */
	public ProductPanel(MainFrame theParentFrame) {
		try {
			parentFrame = theParentFrame;
			myDataClient = new ProductDataClient();
			selectionLabel = new JLabel("ѡ�����");
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

			//detailsButton = new JButton("��ϸ...");
			clearButton = new JButton("���");
			exitButton = new JButton("�˳�");
			shoppingButton = new JButton("�鿴���ﳵ");

			bottomPanel = new JPanel();

			this.setLayout(new BorderLayout());
			JTextField findmy = new JTextField(15);
			JButton okfine= new JButton("����");
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
			JPopupMenu popupMenu = new JPopupMenu();//����ʽ�˵�
			JMenu copyItem=new JMenu("�޸�");
			JMenu pasteItem=new JMenu("ɾ��");
			JMenu buyItem = new JMenu("���빺�ﳵ");
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
					JOptionPane.showMessageDialog(null, "�������", "ϵͳ��Ϣ", JOptionPane.INFORMATION_MESSAGE);
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
					JOptionPane.showMessageDialog(null, "�������", "ϵͳ��Ϣ", JOptionPane.INFORMATION_MESSAGE);
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
			 * ����ѡ�з����б���ѡ��ʱ�������¼�������
			 *
			 * @author ascent
			 */

			clearButton.setEnabled(false);
			//shoppingButton.setEnabled(false);

		} catch (IOException exc) {
			JOptionPane.showMessageDialog(this, "�������� " + exc, "��������", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * ����������ѡ�еķ���ѡ��
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
			JOptionPane.showMessageDialog(this, "��������: " + exc, "��������", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}


	/**
	 * ����ѡ��鿴���ﳵ��ťʱ�������¼�������
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
	 * ����ѡ���˳���ťʱ�������¼�������
	 *
	 * @author ascent
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			parentFrame.exit();
		}
	}

	/**
	 * ����ѡ����հ�ťʱ�������¼�������
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
	 * ����ѡ�з���������ѡ��ѡ��ʱ�������¼�������
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