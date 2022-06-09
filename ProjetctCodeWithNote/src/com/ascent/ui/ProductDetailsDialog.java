package com.ascent.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.ascent.bean.Product;
import com.ascent.util.ShoppingCart;

/**
 * �������ʾ��Ʒ��ϸ��Ϣ�Ի���
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
	 * �����������Ĺ��췽��
	 * @param theParentFrame ������
	 * @param theProduct ��ǰ�鿴����Ʒ����
	 * @param shoppingButton ����ť
	 */
	public ProductDetailsDialog(Frame theParentFrame, Product theProduct,
			JButton shoppingButton)
	{
		this(theParentFrame, "ҩƷ��ϸ��Ϣ " + theProduct.getProductname(),
				theProduct, shoppingButton);
	}

	/**
	 * ���ĸ������Ĺ��췽��
	 * @param theParentFrame ������
	 * @param theTitle �������
	 * @param theProduct ��ǰ�鿴����Ʒ����
	 * @param shoppingButton ����ť
	 */
	public ProductDetailsDialog(Frame theParentFrame, String theTitle,
			Product theProduct, JButton shoppingButton)
	{
		// ���ó��๹��
		super(theParentFrame, theTitle, true);

		// ��Ӧ��Ա�ò���ֱ�ӹ���
		myProduct = theProduct;
		parentFrame = theParentFrame;
		this.shoppingButton = shoppingButton;

		// ���ú���������Ʒ��Ϣ
		buildGui();
	}

	/**
	 * ����������ʾ��Ʒ��Ϣ����
	 */
	private void buildGui()
	{
		// ��ȡ����
		Container container = this.getContentPane();

		// ���ò��ֱ߽粼��
		container.setLayout(new BorderLayout());

		// �½��������
		JPanel topPanel = new JPanel();

		// �� ���������뵽��ʽ���֣��ŷ���֮ǰ˵���ˣ�֮ǰ���Ǳ߽粼�֣�
		// ��ʽ�Դ����ҽ��в���
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		// ������Ϣ���
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

		// ����Ϣ������ö�Ӧ����
		infoPanel.setLayout(new GridBagLayout());

		// һ�����ֹ�����
		GridBagConstraints c = new GridBagConstraints();

		// �Բ��ֵĶ�Ӧ�ĸ������Ե�����
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;

		// ������ʵһ������Ĵ�С
		c.insets = new Insets(10, 0, 2, 10);
		JLabel artistLabel = new JLabel("��Ʒ��:  " + myProduct.getProductname());
		artistLabel.setForeground(Color.black);
		infoPanel.add(artistLabel, c);

		c.gridy = GridBagConstraints.RELATIVE;
		c.insets = new Insets(2, 0, 10, 10);
		JLabel titleLabel = new JLabel("CAS��:  " + myProduct.getCas());
		titleLabel.setForeground(Color.black);
		infoPanel.add(titleLabel, c);

		JLabel categoryLabel = new JLabel("��ʽ:  " + myProduct.getFormula());
		c.insets = new Insets(2, 0, 2, 0);
		categoryLabel.setForeground(Color.black);
		infoPanel.add(categoryLabel, c);

		JLabel durationLabel = new JLabel("����:  " + myProduct.getRealstock());
		durationLabel.setForeground(Color.black);
		infoPanel.add(durationLabel, c);

		JLabel priceLabel = new JLabel("��� " + myProduct.getCategory());
		c.insets = new Insets(10, 0, 2, 0);
		priceLabel.setForeground(Color.black);
		infoPanel.add(priceLabel, c);


		//���µ�������
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

		// ��ȡͼƬ
		try
		{
			if (imageName.trim().length() == 0)
			{
				recordingLabel = new JLabel("  ͼƬ������  ");
			}
			else
			{
				recordingIcon = new ImageIcon(getClass().getResource("/images/" + imageName));
				recordingLabel = new JLabel(recordingIcon);
			}
		}
		catch (Exception exc)
		{
			recordingLabel = new JLabel("  ͼƬ������  ");
		}

		// ����ͼƬ��ǩ�ı�ǩ
		recordingLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		recordingLabel.setToolTipText(myProduct.getProductname());

		infoPanel.add(recordingLabel, c);

		container.add(BorderLayout.NORTH, infoPanel);

		JPanel bottomPanel = new JPanel();
		JButton okButton = new JButton("OK");
		bottomPanel.add(okButton);
		JButton purchaseButton = new JButton("����");
		bottomPanel.add(purchaseButton);
		container.add(BorderLayout.SOUTH, bottomPanel);

		// ��Ӽ����¼�
		okButton.addActionListener(new OkButtonActionListener());
		purchaseButton.addActionListener(new PurchaseButtonActionListener());

		this.pack();

		// ��ȡ�����ڵ�λ��
		Point parentLocation = parentFrame.getLocation();
		// ����λ��
		this.setLocation(parentLocation.x + 50, parentLocation.y + 50);
	}

	/**
	 * ����"OK"��ť���ڲ���
	 */
	class OkButtonActionListener implements ActionListener
	{
		// ���ok���ر���ϸ����
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
		}
	}

	/**
	 * ����"����"��ť���ڲ���
	 */
	class PurchaseButtonActionListener implements ActionListener
	{
		// �������ﳵ����
		ShoppingCart shoppingCar = new ShoppingCart();
		public void actionPerformed(ActionEvent event)
		{
			// ����Ʒ���뵽���ﳵ
			shoppingCar.addProduct(myProduct);
			// ���ﳵ��ť��Ϊ�ɼ�
			shoppingButton.setEnabled(true);
			// �ر��������
			setVisible(false);
		}
	}
}