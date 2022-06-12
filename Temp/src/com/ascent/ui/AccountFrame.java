package com.ascent.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.*;

import com.ascent.bean.User;
import com.ascent.util.UserDataClient;
public class AccountFrame extends JFrame{
    private JTextField accountText;

    private JPasswordField password;
    private JLabel tip;

    private JTabbedPane tabbedPane;

    private AccountPanel userPanel;
    private UserDataClient userDataClient;


    /**
     * Ĭ�Ϲ��췽������ʼ���û�ע�ᴰ��
     */
    public AccountFrame() {
        this.setTitle("�˺Ź���");

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel registPanel = new JPanel();

        JLabel searchAccount = new JLabel("�����ʺţ�");
        JLabel passwordLabel = new JLabel("�˺����룺");

        accountText = new JTextField(15);
        password = new JPasswordField(15);
        JButton search = new JButton("����");
        JButton exitButton = new JButton("�˳�");

        registPanel.add(searchAccount);
        registPanel.add(new JScrollPane(accountText));
        registPanel.add(passwordLabel);
        registPanel.add(new JScrollPane(password));
        registPanel.add(search);
        registPanel.add(exitButton);

        setResizable(false);
        setSize(260, 150);
        setLocation(300, 100);

        JPanel tipPanel = new JPanel();

        tip = new JLabel();

        tipPanel.add(tip);

        container.add(BorderLayout.CENTER, registPanel);
        container.add(BorderLayout.NORTH, tip);

        exitButton.addActionListener(new AccountFrame.ExitActionListener());
        search.addActionListener(new AccountFrame.SearchActionListener());
        this.addWindowFocusListener(new WindowFocusListener() {// ���ø�����
            public void windowGainedFocus(WindowEvent e) {
            }
            public void windowLostFocus(WindowEvent e) {
                e.getWindow().toFront();
            }
        });
        try {
            userDataClient = new UserDataClient();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }

    public class SearchActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean bo = false;
            boolean isAdmin = false;
            HashMap userTable = userDataClient.getUsers();
            if (userTable != null) {
                if (userTable.containsKey(accountText.getText())) {
                    User userObject = (User) userTable.get(accountText.getText());
                    char[] chr = password.getPassword();
                    String pwd = new String(chr);
                    if (userObject.getPassword().equals(pwd)) {
                        bo = true;
                    }
                    if(userObject.getAuthority() == 1){
                        isAdmin = true;
                    }
                }
                if (bo) {
                    if(isAdmin){
                        userDataClient.closeSocKet();
                        setVisible(false);
                        dispose();
                        AccountDetailsFrame myFrame =new AccountDetailsFrame();
                        myFrame.setVisible(true);
                    }
                    else{
                        setSize(260, 150);
                        tip.setText("���ǹ������˺�.");
                    }
                } else {
                    setSize(260, 150);
                    tip.setText("�ʺŲ�����,���������.");
                }
            } else {
                tip.setText("����������ʧ��,���Ժ�����.");
            }
        }

    }
    public void exit() {
        setVisible(false);
        dispose();
    }

}
