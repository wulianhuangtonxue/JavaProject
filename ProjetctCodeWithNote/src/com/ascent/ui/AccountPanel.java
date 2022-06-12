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

    protected JLabel selectionLabel;                // ѡ���ǩ

    protected JComboBox categoryComboBox;           // ��Ͽ�

    protected JPanel topPanel;                      // �������

    protected JList userListBox;                    // �û��б�

    protected JScrollPane userScrollPane;           // �û��ƶ����

    protected JButton exitButton;                   //�ͻ��˰�ť

    protected JPanel bottomPanel;                   //�ײ����

    protected AccountDetailsFrame parentFrame;      //������

    protected ArrayList<User> usersArrayList;

    protected ArrayList<User> ordinaryUsers;

    protected ArrayList<User> controller;

    protected ArrayList<User> allUsers;

    protected UserDataClient myDataClient;       //�û�����



    public AccountPanel(AccountDetailsFrame theParentFrame){
        try{
            // ������
            parentFrame = theParentFrame;
            // �½�һ���û����������
            myDataClient = new UserDataClient();
            // ѡ���ǩ����
            selectionLabel = new JLabel("ѡ������");
            // �½�һ����Ͽ�
            categoryComboBox = new JComboBox();
            // ��������
            categoryComboBox.addItem("�����û�");
            categoryComboBox.addItem("��ͨ�û�");
            categoryComboBox.addItem("����Ա");

            ArrayList<User> categoryArrayList = myDataClient.getTwoKindUsers();

            ordinaryUsers = new ArrayList<User>();
            controller = new ArrayList<User>();
            allUsers = new ArrayList<User>();

            for(User user : categoryArrayList){
                // ����Ȩ���ж�����ͨ���ǹ�����
                if(user.getAuthority() == 0){
                    ordinaryUsers.add(user);
                }
                else
                    controller.add(user);
                allUsers.add(user);
            }


            topPanel = new JPanel();
            userListBox = new JList();
            //���õ�ѡ
            userListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            //�û�����ģ��
            userScrollPane = new JScrollPane(userListBox);

            exitButton = new JButton("�˳�");

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
            JOptionPane.showMessageDialog(this, "�������� " + exc, "��������", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    class ExitActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            parentFrame.exit();
        }
    }

    class GoItemListener implements ItemListener {
        // ��дitem״̬�ı亯��
        public void itemStateChanged(ItemEvent event) {
                userListBox();
        }
    }

    protected void userListBox() {
        // ��ȡ���
        String category = (String) categoryComboBox.getSelectedItem();
        // ֻҪ���ǿ���𣬾ͻ�ȡ��Ӧ�Ĳ�Ʒ����
        if (category.startsWith("�����û�")) {
            usersArrayList = allUsers;
        } else if (category.startsWith("��ͨ�û�")) {
            usersArrayList = ordinaryUsers;
        } else {
            usersArrayList = controller;
        }

        // ��data���黯
        String[] theData = new String[usersArrayList.size()];
        int i = 0;
        for (User user : usersArrayList) {
            // ����Ȩ���ж�����ͨ���ǹ�����
            if (user.getAuthority() == 0)
                theData[i] = user.getUsername()+"  "+user.getPassword() + "  0";
            else
                theData[i] = user.getUsername()+"  "+user.getPassword() + "  1";
            i++;
        }

        // ��ӡ��Ӧ�Ĳ�Ʒ��Ϣ
        System.out.println(usersArrayList);
        // �����б�����
        userListBox.setListData(theData);
        // ����UI��������ʵ���½���
        userListBox.updateUI();
    }
}
