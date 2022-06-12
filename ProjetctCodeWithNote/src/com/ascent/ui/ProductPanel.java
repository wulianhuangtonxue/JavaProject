package com.ascent.ui;

import javax.swing.*;
import javax.swing.event.*;

// �����Ʒ�Ͳ�Ʒ������
import com.ascent.bean.Product;
import com.ascent.util.ProductDataClient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.io.*;

/**
 * ����๹����Ʒ���
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProductPanel extends JPanel
{

	protected Client openClient;                //�ͻ���
	protected JLabel selectionLabel;			// ѡ���ǩ

	protected JComboBox categoryComboBox;		// ��Ͽ�

	protected JPanel topPanel;					// �������

	protected JList productListBox;				// ��Ʒ�б�

	protected JScrollPane productScrollPane;	// ��Ʒ�ƶ����

	protected JButton client;                   //�ͻ��˰�ť

	protected JButton detailsButton;			// ��ϸ��ť

	protected JButton clearButton;				// ��հ�ť

	protected JButton exitButton;				// �˳���ť

	protected JButton shoppingButton;			// �鿴���ﳵ��ť

	protected JPanel bottomPanel;				// �ײ����

	protected MainFrame parentFrame;			// �����ڣ�����MainFrame

	protected ArrayList<Product> productArrayList;	// ��Ʒ�����б�

	protected ProductDataClient myDataClient;		// ��Ʒ���ݶ���

	/**
	 * ������Ʒ���Ĺ��췽��
	 * @param theParentFrame ���ĸ�����
	 */
	public ProductPanel(MainFrame theParentFrame)
	{
		try
		{
			// ������
			parentFrame = theParentFrame;
			// �½�һ����Ʒ�����������ʵ����׼����ȡ��Ʒ����
			myDataClient = new ProductDataClient();
			// ѡ���ǩ�Ӹ�����
			selectionLabel = new JLabel("ѡ�����");
			// �½�һ����Ͽ����
			categoryComboBox = new JComboBox();
			// �������Ͽ��----- �������
			categoryComboBox.addItem("-------");

			// �Ӳ�Ʒ���ݻ�ȡ��Ʒ��𼯺�
			ArrayList categoryArrayList = myDataClient.getCategories();

			// ���������� ����𼯺������ȡ������
			Iterator iterator = categoryArrayList.iterator();
			// ��ǰ���
			String aCategory;

			// ����������
			while (iterator.hasNext())
			{
				// ��ȡ��Ӧ�����
				aCategory = (String) iterator.next();
				// �������������Ͽ���
				categoryComboBox.addItem(aCategory);
			}

			// ��Ӧ�½�����
			topPanel = new JPanel();
			productListBox = new JList();
			// ����ѡ��ʽ-----����ѡ��
			productListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// ��Ʒ�������
			productScrollPane = new JScrollPane(productListBox);

			// �½������ť
			detailsButton = new JButton("��ϸ...");
			clearButton = new JButton("���");
			exitButton = new JButton("�˳�");
			shoppingButton = new JButton("�鿴���ﳵ");
			client = new JButton("��ϵ�ͷ�");

			// �ײ����
			bottomPanel = new JPanel();

			// ����Ϊ��ʽ����
			this.setLayout(new BorderLayout());

			// ��������Ϊ��ʽ���֣��Ҿ���
			topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			// ��ѡ���ǩ�������Ͽ���뵽����
			topPanel.add(selectionLabel);
			topPanel.add(categoryComboBox);

			// ��������������ڶ���
			this.add(BorderLayout.NORTH, topPanel);
			// ����Ʒ���������뵽��ʽ���ֵ��м�
			this.add(BorderLayout.CENTER, productScrollPane);

			// �ײ��������Ϊ��ʽ����
			bottomPanel.setLayout(new FlowLayout());
			bottomPanel.add(client);
			bottomPanel.add(shoppingButton);
			bottomPanel.add(detailsButton);
			bottomPanel.add(clearButton);
			bottomPanel.add(exitButton);

			// ���ײ������뵽��ʽ���ֵĵײ�
			this.add(BorderLayout.SOUTH, bottomPanel);

			// ����ť�������򣬲�Ʒ��������Ӷ�Ӧ���¼�
			// ��ϸ
			detailsButton.addActionListener(new DetailsActionListener());
			// ���
			clearButton.addActionListener(new ClearActionListener());
			// �˳�
			exitButton.addActionListener(new ExitActionListener());
			// ���ﳵ��ť
			shoppingButton.addActionListener(new ShoppingActionListener());
			// �ͻ��˰�ť
			client.addActionListener(new clientActionListener());
			// �����Ͽ�
			categoryComboBox.addItemListener(new GoItemListener());
			// ��Ʒ�б�
			productListBox.addListSelectionListener(new ProductListSelectionListener());

			// ��ʼʱ�����˳���ť������Ϊ���ɵ��
			detailsButton.setEnabled(false);
			clearButton.setEnabled(false);
			shoppingButton.setEnabled(false);

		}
		// ����IO�쳣
		catch (IOException exc)
		{
			JOptionPane.showMessageDialog(this, "�������� " + exc, "��������", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * ����������ѡ�еķ���ѡ��
	 */
	protected void populateListBox()
	{
		try
		{
			// ��ȡ���
			String category = (String) categoryComboBox.getSelectedItem();
			// ֻҪ���ǿ���𣬾ͻ�ȡ��Ӧ�Ĳ�Ʒ����
			if (!category.startsWith("---"))
			{
				productArrayList = myDataClient.getProducts(category);
			}
			// ����ֱ���½�һ���յĲ�Ʒ����
			else
			{
				productArrayList = new ArrayList<Product>();
			}

			// ��data���黯
			Object[] theData = productArrayList.toArray();

			// ��ӡ��Ӧ�Ĳ�Ʒ��Ϣ
			System.out.println(productArrayList + ">>>>>>>>>>>");
			// �����б�����
			productListBox.setListData(theData);
			// ����UI��������ʵ���½���
			productListBox.updateUI();

			// �����Ʒ����������0����հ�ť����Ϊ�ɵ��
			if (productArrayList.size() > 0)
			{
				clearButton.setEnabled(true);
			}
			else
			{
				clearButton.setEnabled(false);
			}
		}
		// ����IO�쳣
		catch (IOException exc)
		{
			JOptionPane.showMessageDialog(this, "��������: " + exc, "��������", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} 
	}

	/**
	 * ����ѡ����ϸ...��ťʱ�������¼�������
	 * @author ascent
	 */
	class DetailsActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// �Ӳ�Ʒ�л�ȡ����
			int index = productListBox.getSelectedIndex();
			// ����������ȡ��Ʒ
			Product product = (Product) productArrayList.get(index);
			// �½���ϸ����
			ProductDetailsDialog myDetailsDialog = new ProductDetailsDialog(parentFrame, product, shoppingButton);
			// ������ϸ����ɼ�
			myDetailsDialog.setVisible(true);
		}
	}

	/**
	 * ����ѡ��鿴���ﳵ��ťʱ�������¼�������
	 * @author ascent
	 */
	class ShoppingActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// �½�һ�����ﳵ����
			// ����Ĳ����������ﳵ�İ�ť
			ShoppingCartDialog myShoppingDialog = new ShoppingCartDialog(
					parentFrame, shoppingButton);
			// ���ÿɼ�
			myShoppingDialog.setVisible(true);
		}
	}

	/**
	 * ����ѡ���˳���ťʱ�������¼�������
	 * @author ascent
	 */
	// �����������˳�
	class ExitActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event) {
			parentFrame.exit();
		}
	}

	/**
	 * ����ѡ����հ�ťʱ�������¼�������
	 * @author ascent
	 */
	class ClearActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// ������
			Object[] noData = new Object[1];
			// ���������
			productListBox.setListData(noData);
			categoryComboBox.setSelectedIndex(0);
		}
	}

	/**
	 * ����ѡ�з���������ѡ��ѡ��ʱ�������¼�������
	 * @author ascent
	 */
	class GoItemListener implements ItemListener
	{
		// ��дitem״̬�ı亯��
		public void itemStateChanged(ItemEvent event)
		{
			if (event.getStateChange() == ItemEvent.SELECTED)
			{
				populateListBox();
			}
		}
	}

	/**
	 * ����ѡ�з����б���ѡ��ʱ�������¼�������
	 * @author ascent
	 */
	class ProductListSelectionListener implements ListSelectionListener
	{
		// ��дֵ�Ƿ�ı亯��������ʵ����item�Ƿ�ѡȡ
		public void valueChanged(ListSelectionEvent event)
		{
			// ���û�б�ѡȡ
			if (productListBox.isSelectionEmpty())
			{
				// ��ϸ��ť����ʾ
				detailsButton.setEnabled(false);
			}
			else
			{
				detailsButton.setEnabled(true);
			}
		}
	}
	/**
	 * ����ͻ��˰�ť���¼�������
	 * @author ascent
	 */
	class clientActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			openClient = new Client();
		}
	}
}