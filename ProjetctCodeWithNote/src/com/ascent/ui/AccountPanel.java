package com.ascent.ui;

import javax.swing.*;

import com.ascent.util.ProductDataClient;
import com.ascent.util.UserDataClient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.io.*;

public class AccountPanel extends JPanel {

    protected JLabel selectionLabel;

    protected JComboBox categoryComboBox;

    protected JPanel topPanel;

    protected JList userListBox;

    protected JScrollPane userScrollPane;

    protected JButton exitButton;

    protected JPanel bottomPanel;

    protected AccountDetailsFrame parentFrame;

    protected ProductDataClient myDataClient;



    public AccountPanel(AccountDetailsFrame theParentFrame){
        try{
            parentFrame = theParentFrame;
            myDataClient = new ProductDataClient();
            selectionLabel = new JLabel("选择类型");
            categoryComboBox = new JComboBox();
            categoryComboBox.addItem("-------");

            ArrayList categoryArrayList = myDataClient.getCategories();

            Iterator iterator =categoryArrayList.iterator();
            String aCategory;

            while (iterator.hasNext()){
                aCategory = (String) iterator.next();
                categoryComboBox.addItem(aCategory);
            }

            topPanel = new JPanel();
            userListBox = new JList();
            userListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            userScrollPane = new JScrollPane(userListBox);

            exitButton = new JButton("退出");

            bottomPanel = new JPanel();

            this.setLayout(new BorderLayout());

            topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(selectionLabel);
            topPanel.add(categoryComboBox);

            this.add(BorderLayout.NORTH, topPanel);
            this.add(BorderLayout.CENTER, userScrollPane);

            bottomPanel.setLayout(new FlowLayout());
            bottomPanel.add(exitButton);

            this.add(BorderLayout.SOUTH, bottomPanel);

            exitButton.addActionListener(new ExitActionListener());

        }catch (IOException exc){
            JOptionPane.showMessageDialog(this, "网络问题 " + exc, "网络问题", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    class ExitActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            parentFrame.exit();
        }
    }
}
