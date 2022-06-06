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
public class ProductDetailsDialog extends JDialog {

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
			JButton shoppingButton) {
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
			Product theProduct, JButton shoppingButton) {

		super(theParentFrame, theTitle, true);

		myProduct = theProduct;
		parentFrame = theParentFrame;
		this.shoppingButton = shoppingButton;

		buildGui();
	}

	/**
	 * ����������ʾ��Ʒ��Ϣ����
	 */
	private void buildGui() {

		Container container = this.getContentPane();

		container.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

		infoPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
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
		try {
			if (imageName.trim().length() == 0) {
				recordingLabel = new JLabel("  ͼƬ������  ");
			} else {
				recordingIcon = new ImageIcon(getClass().getResource("/images/" + imageName));
				recordingLabel = new JLabel(recordingIcon);
			}
		} catch (Exception exc) {
			recordingLabel = new JLabel("  ͼƬ������  ");
		}

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

		okButton.addActionListener(new OkButtonActionListener());
		purchaseButton.addActionListener(new PurchaseButtonActionListener());

		this.pack();

		Point parentLocation = parentFrame.getLocation();
		this.setLocation(parentLocation.x + 50, parentLocation.y + 50);
	}

	/**
	 * ����"OK"��ť���ڲ���
	 */
	class OkButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
		}
	}

	/**
	 * ����"����"��ť���ڲ���
	 */
	class PurchaseButtonActionListener implements ActionListener {
		ShoppingCart shoppingCar = new ShoppingCart();
		public void actionPerformed(ActionEvent event) {
			shoppingCar.addProduct(myProduct);
			shoppingButton.setEnabled(true);
			setVisible(false);
		}
	}
}