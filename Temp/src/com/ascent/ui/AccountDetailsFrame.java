package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AccountDetailsFrame extends JFrame{

    protected JTabbedPane tabbedPane;

    protected AccountPanel userPanel;

    public AccountDetailsFrame(){
        setTitle("’À∫≈π‹¿Ì");

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        userPanel = new AccountPanel(this);
        tabbedPane.addTab(BorderLayout.CENTER,tabbedPane);

        setSize(500, 400);
        setLocation(100, 100);
    }

    public void exit() {
        setVisible(false);
        dispose();
    }
}

