package com.ascent.ui;

import javax.swing.*;

import com.ascent.bean.Product;
import com.ascent.bean.User;
import com.ascent.util.UserDataClient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.io.*;

public class AccountPanel extends JPanel {

    protected JLabel selectionLabel;                // 选择标签

    protected JComboBox categoryComboBox;           // 组合框

    protected JPanel topPanel;                      // 顶部面板

    protected JList userListBox;                    // 用户列表

    protected JScrollPane userScrollPane;           // 用户移动面板

    protected JButton exitButton;                   //客户端按钮

    protected JPanel bottomPanel;                   //底部面板

    protected AccountDetailsFrame parentFrame;      //父窗口

    protected ArrayList<User> usersArrayList;

    protected ArrayList<User> ordinaryUsers;

    protected ArrayList<User> controller;

    protected ArrayList<User> allUsers;

    protected UserDataClient myDataClient;       //用户数据



    public AccountPanel(AccountDetailsFrame theParentFrame){
        try{
            // 父窗口
            parentFrame = theParentFrame;
            // 新建一个用户数据类对象
            myDataClient = new UserDataClient();
            // 选择标签命名
            selectionLabel = new JLabel("选择类型");
            // 新建一个组合框
            categoryComboBox = new JComboBox();
            // 添加无类别
            categoryComboBox.addItem("所有用户");
            categoryComboBox.addItem("普通用户");
            categoryComboBox.addItem("管理员");

            ArrayList<User> categoryArrayList = myDataClient.getTwoKindUsers();

            ordinaryUsers = new ArrayList<User>();
            controller = new ArrayList<User>();
            allUsers = new ArrayList<User>();

            for(User user : categoryArrayList){
                // 根据权限判断是普通还是管理者
                if(user.getAuthority() == 0){
                    ordinaryUsers.add(user);
                }
                else
                    controller.add(user);
                allUsers.add(user);
            }


            topPanel = new JPanel();
            userListBox = new JList();
            //设置单选
            userListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            //用户滑动模块
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

            categoryComboBox.addItemListener(new GoItemListener());

            userListBox();
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

    class GoItemListener implements ItemListener {
        // 重写item状态改变函数
        public void itemStateChanged(ItemEvent event) {
                userListBox();
        }
    }

    protected void userListBox() {
        // 获取类别
        String category = (String) categoryComboBox.getSelectedItem();
        // 只要不是空类别，就获取对应的产品数据
        if (category.startsWith("所有用户")) {
            usersArrayList = allUsers;
        } else if (category.startsWith("普通用户")) {
            usersArrayList = ordinaryUsers;
        } else {
            usersArrayList = controller;
        }

        // 将data数组化
        String[] theData = new String[usersArrayList.size()];
        int i = 0;
        for (User user : usersArrayList) {
            // 根据权限判断是普通还是管理者
            if (user.getAuthority() == 0)
                theData[i] = user.getUsername()+"  "+user.getPassword() + "  0";
            else
                theData[i] = user.getUsername()+"  "+user.getPassword() + "  1";
            i++;
        }

        // 打印对应的产品信息
        System.out.println(usersArrayList);
        // 设置列表数据
        userListBox.setListData(theData);
        // 更新UI，这里其实更新界面
        userListBox.updateUI();
    }
}
