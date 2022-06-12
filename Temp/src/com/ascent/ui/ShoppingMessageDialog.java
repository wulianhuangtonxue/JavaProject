package com.ascent.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * 确认购买商品提示窗体
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ShoppingMessageDialog extends JDialog {

	protected Frame parentFrame;

	/**
	 * 带一个参数的构造方法
	 * @param theParentFrame 父窗体
	 */
	public ShoppingMessageDialog(Frame theParentFrame) {
		super(theParentFrame, "购物信息", true);
		parentFrame = theParentFrame;
		shoppingMessage();
	}

	/**
	 * 构建弹出确认信息窗体
	 */
	public void shoppingMessage() {
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
		infoPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 0, 2, 10);

		String str = "您购物信息已经保存，谢谢。";
		JLabel pruductLabel = new JLabel(str);
		pruductLabel.setForeground(Color.black);
		infoPanel.add(pruductLabel, c);

		container.add(BorderLayout.NORTH, infoPanel);

		JPanel bottomPanel = new JPanel();
		JButton okButton = new JButton("OK");
		bottomPanel.add(okButton);

		container.add(BorderLayout.SOUTH, bottomPanel);

		okButton.addActionListener(new OkButtonActionListener());

		setResizable(false);
		this.pack();

		Point parentLocation = parentFrame.getLocation();
		this.setLocation(parentLocation.x + 50, parentLocation.y + 50);
	}

	/**
	 * 处理"OK"按钮的内部类
	 * @author ascent
	 */
	class OkButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
		}
	}
}
