package com.ascent.util;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;



public class ServerFrame extends JFrame {      //客户机窗体类
    // 界面上左中下
    private JPanel panUp = new JPanel();
    private JPanel panLeft = new JPanel();
    private JPanel panMid = new JPanel();
    private JPanel panDown = new JPanel();

    // panUp 区域的子节点定义，3个标签、3个输入框、2个按钮
    private JLabel lblLocalPort3 = new JLabel("本人昵称: ");
    protected JTextField tfLocalPort3 = new JTextField(5);
    protected JButton butStart = new JButton("创建聊天");
    protected JButton butStop = new JButton("结束聊天");

    // panLeft 区域的子节点定义，显示框、滚动条
    protected JTextArea taMsg = new JTextArea(25, 25);
    JScrollPane scroll = new JScrollPane(taMsg);

    // panMid 区域的子节点定义，lstUsers在线用户界面
    JList lstUsers = new JList();

    // panDown 区域的子节点定义，标签，输入框
    private JLabel lblLocalPort4 = new JLabel("消息（按回车发送）: ");
    protected JTextField tfLocalPort4 = new JTextField(20);
    /**
     * ===== 变量分割 =====
     * 上面是图形界面变量，下面是存放数据的变量
     */
    BufferedReader in;
    PrintStream out;
    public static int localPort = 8000;     // 默认端口
    public static String localIP = "127.0.0.1";     // 默认服务器IP地址
    public static String nickname = "客服";      // 默认用户名
    public Socket socket;
    public static String msg;       // 存放本次发送的消息
    Vector<String> ServerNames = new Vector<>();


    // 主方法
    public ServerFrame(String[] args) {
        new com.ascent.util.ServerFrame();
    }
    public ServerFrame() {
        init();
        linkServer();
        Thread acceptThread = new Thread(new ServerFrame.ReceiveRunnable());
        acceptThread.start();
    }

    // 初始化界面
    private void init() {
        // panUp 区域初始化：流式面板，3个标签、3个输入框，2个按钮
        panUp.setLayout(new FlowLayout());
        panUp.add(lblLocalPort3);
        panUp.add(tfLocalPort3);
        tfLocalPort3.setText(nickname);
        panUp.add(butStart);
        panUp.add(butStop);
        butStart.addActionListener(new ServerFrame.linkServerHandlerStart());
        butStop.addActionListener(new ServerFrame.linkServerHandlerStop());
        butStop.setEnabled(false);      // 断开服务器按钮的初始状态应该为 不可点击，只有连接服务器之后才能点击

        // 添加 Left
        taMsg.setEditable(false);
        panLeft.add(scroll);
        panLeft.setBorder(new TitledBorder("聊天——消息区"));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // 添加 Middle
        panMid.setBorder(new TitledBorder("在线用户"));
        panMid.add(lstUsers);
        lstUsers.setVisibleRowCount(20);

        // 添加 Down
        panDown.setLayout(new FlowLayout());
        panDown.add(lblLocalPort4);
        panDown.add(tfLocalPort4);
        panDown.setSize(100,220);
        tfLocalPort4.addActionListener(new ServerFrame.SendHandler());

        // 图形界面的总体初始化 + 启动图形界面
        this.setTitle("客服端");
        this.add(panUp, BorderLayout.NORTH);
        this.add(panLeft, BorderLayout.WEST);
        this.add(panMid, BorderLayout.CENTER);
        this.add(panDown, BorderLayout.SOUTH);
        this.addWindowListener(new WindowCloser());
        this.setPreferredSize(new Dimension(800, 600));
        this.pack();
        this.setVisible(true);
    }

    // 连接服务器监控事件
    private class linkServerHandlerStart implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            nickname = tfLocalPort3.getText();
            linkServer();
            Thread acceptThread = new Thread(new ServerFrame.ReceiveRunnable());
            acceptThread.start();
        }
    }

    // 断开服务器监控事件
    private class linkServerHandlerStop implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ServerNames = new Vector<>();
            updateUsers();
            out.println("——客户【" + nickname + "】离开:bye\n");
            butStart.setEnabled(true);
            butStop.setEnabled(false);
        }
    }

    //连接服务器
    public void linkServer() {
        try {
            socket = new Socket(localIP, localPort);
            butStart.setEnabled(false);
            butStop.setEnabled(true);
        } catch (Exception ex) {
            taMsg.append("==== 连接服务器失败~ ====");
        }
    }

    //断开服务器
    public void stopServer(){
        taMsg.setText("");
        ServerNames = new Vector<>();
        updateUsers();
        out.println("——客户【" + nickname + "】离开:bye\n");
        butStart.setEnabled(true);
        butStop.setEnabled(false);
    }

    // 接收消息
    private class ReceiveRunnable implements Runnable {
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintStream(socket.getOutputStream());
                out.println(nickname);      // 当用户首次连接服务器时，向服务器发送自己的用户名、用于服务器区分
                taMsg.append("——本人【" + nickname + "】成功连接到服务器......\n");
                out.println("USERS");       // 向服务器请求当前在线用户列表
                while (true) {
                    msg = in.readLine();       // 读取服务器端的发送的数据
                    // 此 if 语句的作用是：过滤服务器发送过来的 更新当前在线用户列表 请求
                    if (msg.matches(".*\\[.*\\].*")) {
                        ServerNames.removeAllElements();
                        String[] split = msg.split(",");
                        for (String single : split) {
                            ServerNames.add(single);
                        }
                        updateUsers();
                        continue;
                    }

                    // 更新 "聊天——消息区" 信息
                    taMsg.append(msg + "\n");

                    // 此 if 语句作用：与服务器进行握手确认消息。
                    // 当接收到服务器端发送的确认离开请求bye 的时候，用户真正离线
                    msg = msg.substring(msg.lastIndexOf("：") + 1);
                    if (msg.equals(nickname)) {
                        socket.close();
                        ServerNames.remove(nickname);
                        updateUsers();
                        break;       // 终止线程
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    // 发送消息
    private class SendHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            out.println("【" + nickname + "】:" + tfLocalPort4.getText());
            tfLocalPort4.setText("");
        }
    }

    // 关闭窗口
    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            stopServer();
            setVisible(false);
            dispose();
        }
    }

    private void cutServer() {
        out.println("——客户【" + nickname + "】离开:bye");
    }

    // 更新在线用户
    public void updateUsers() {
        panMid.setBorder(new TitledBorder("在线用户(" + ServerNames.size() + "个)"));
        lstUsers.setListData(ServerNames);
    }


}

