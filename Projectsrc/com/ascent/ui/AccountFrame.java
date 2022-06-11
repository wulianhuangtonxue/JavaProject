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
     * 默认构造方法，初始化用户注册窗体
     */
    public AccountFrame() {
        this.setTitle("账号管理");

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel registPanel = new JPanel();

        JLabel searchAccount = new JLabel("管理帐号：");
        JLabel passwordLabel = new JLabel("账号密码：");

        accountText = new JTextField(15);
        password = new JPasswordField(15);
        JButton search = new JButton("搜索");
        JButton exitButton = new JButton("退出");

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
        this.addWindowFocusListener(new WindowFocusListener() {// 设置父窗口
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
                        tip.setText("不是管理者账号.");
                    }
                } else {
                    setSize(260, 150);
                    tip.setText("帐号不存在,或密码错误.");
                }
            } else {
                tip.setText("服务器连接失败,请稍候再试.");
            }
        }

    }
    public void exit() {
        setVisible(false);
        dispose();
    }

}
