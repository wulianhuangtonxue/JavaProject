package com.ascent.ui;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.*;

import com.ascent.bean.Product;
import com.ascent.util.ProductDataClient;


public class FindPanel extends JFrame{
    JFrame jf=new JFrame("搜索框测试");
    protected JTextField targetText;
    String[] books={"java", "android", "C++", "C", "C#", "HTML", "js"};
    JPanel layoutPanel=new JPanel();
    protected ProductDataClient productDataClient;
    JTextArea result=new JTextArea(4, 40);
    //JList和JComboBox方法类似
    JList<String> resultList;
    public FindPanel() {
        //设置容器和组件的边框
        result.setBorder(new EtchedBorder());
        layoutPanel.setBorder(new TitledBorder(new EtchedBorder(), "搜索你的药品"));
        //创建JList
        resultList=new JList<>(books);
        resultList.setVisibleRowCount(10);
        resultList.setSelectionInterval(1,2);
        //控件
        targetText = new JTextField(30);
        JButton FButton = new JButton("搜索");
        FButton.setSize(48,24);
        FButton.setLocation(352,0);

        //垂直布局容器
        Box leftVBox = Box.createVerticalBox();
        //使用JScrollPane，才会有滚动条
        leftVBox.add(layoutPanel);
        layoutPanel.add(targetText);
        layoutPanel.add(FButton);
        leftVBox.add(new JScrollPane(resultList));
        targetText.setText("请输入搜索对象");
        //设置窗口
        FButton.addActionListener(new FindActionListener());
        //底部容器
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JLabel("搜索结果："), BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(resultList));
        Box topBox = Box.createHorizontalBox();
        topBox.add(leftVBox);
        //组合在window中
        Box box = Box.createVerticalBox();
        box.add(topBox);
        box.add(bottomPanel);
        jf.add(box);
        jf.setSize(397, 260);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);

    }
    /**
     * 处理"注册"按钮事件监听的内部类.
     */
    class FindActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            boolean bo = false;
            System.out.println("test");
            //resultList.setListData("搜索了");
            /*
            HashMap dataTable = ProductDataClient.getProduct();
            if (dataTable != null) {
                if (dataTable.containsKey(targetText.getText())) {
                    Product productObject = (Product) dataTable.get(targetText.getText());

                    if (productObject.getProductname().equals(targetText.getText())) {
                        bo = true;
                    }
                }
                if (bo) {
                    //ProductDataClient.closeSocKet();
                    resultText.setText(targetText.getText());

                } else {
                    resultText.setText("搜索内容不存在.");
                }
            } else {
                resultText.setText("服务器连接失败,请稍候再试.");
            }*/
        }

    }

    /**
     * 处理"关闭窗口"事件监听的内部类.
     */
    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            dispose();
            //DataClient.closeSocKet();
        }
    }

}
