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
    JFrame jf=new JFrame("���������");
    protected JTextField targetText;
    String[] books={"java", "android", "C++", "C", "C#", "HTML", "js"};
    JPanel layoutPanel=new JPanel();
    protected ProductDataClient productDataClient;
    JTextArea result=new JTextArea(4, 40);
    //JList��JComboBox��������
    JList<String> resultList;
    public FindPanel() {
        //��������������ı߿�
        result.setBorder(new EtchedBorder());
        layoutPanel.setBorder(new TitledBorder(new EtchedBorder(), "�������ҩƷ"));
        //����JList
        resultList=new JList<>(books);
        resultList.setVisibleRowCount(10);
        resultList.setSelectionInterval(1,2);
        //�ؼ�
        targetText = new JTextField(30);
        JButton FButton = new JButton("����");
        FButton.setSize(48,24);
        FButton.setLocation(352,0);

        //��ֱ��������
        Box leftVBox = Box.createVerticalBox();
        //ʹ��JScrollPane���Ż��й�����
        leftVBox.add(layoutPanel);
        layoutPanel.add(targetText);
        layoutPanel.add(FButton);
        leftVBox.add(new JScrollPane(resultList));
        targetText.setText("��������������");
        //���ô���
        FButton.addActionListener(new FindActionListener());
        //�ײ�����
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JLabel("���������"), BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(resultList));
        Box topBox = Box.createHorizontalBox();
        topBox.add(leftVBox);
        //�����window��
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
     * ����"ע��"��ť�¼��������ڲ���.
     */
    class FindActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            boolean bo = false;
            System.out.println("test");
            //resultList.setListData("������");
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
                    resultText.setText("�������ݲ�����.");
                }
            } else {
                resultText.setText("����������ʧ��,���Ժ�����.");
            }*/
        }

    }

    /**
     * ����"�رմ���"�¼��������ڲ���.
     */
    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            dispose();
            //DataClient.closeSocKet();
        }
    }

}
