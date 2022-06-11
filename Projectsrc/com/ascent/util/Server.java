package com.ascent.util;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author : ThinkStu
 * @since : 2022/4/27, 7:35 PM, 周三
 **/
public class Server extends JFrame {
    // TODO 该图形界面拥有三块区域，分别位于上、中、下 （up、middle、down）。
    private JPanel panUp = new JPanel();
    private JPanel panMid = new JPanel();
    private JPanel panDown = new JPanel();

    // panUp 区域的子节点定义，标签、输入框、按钮
    private JLabel lblLocalPort = new JLabel("本机服务器监听端口:");
    protected JButton butStart = new JButton("启动服务器");
    protected JTextField tfLocalPort = new JTextField(25);

    // panMid 区域的子节点定义，显示框 以及 滚动条
    protected JTextArea taMsg = new JTextArea(25, 25);
    JScrollPane scroll = new JScrollPane(taMsg);

    // panDown 区域的子节点定义，lstUsers在线用户界面
    JList lstUsers = new JList();

    // TODO 以下是存放数据的变量
    public static int localPort = 8000;     // 默认端口 8000
    static int SerialNum = 0;       // 用户连接数量
    ServerSocket serverSocket;      // 服务器端 Socket
    ArrayList<AcceptRunnable.Client> clients = new ArrayList<>();        // 用户连接对象数组
    Vector<String> clientNames = new Vector<>();       // lstUsers 中存放的数据

    // TODO 主方法
    public Server(String[] args) {
        new Server();
    }

    // TODO 构造方法
    public Server() {
        init();
    }

    // TODO 初始化方法：初始化图形界面布局
    private void init() {
        // panUp 区域初始化：流式区域
        panUp.setLayout(new FlowLayout());
        panUp.add(lblLocalPort);
        panUp.add(tfLocalPort);
        panUp.add(butStart);
        tfLocalPort.setText(String.valueOf(localPort));
        butStart.addActionListener(new startServerHandler());   // 注册 "启动服务器" 按钮点击事件

        // panMid 区域初始化
        panMid.setBorder(new TitledBorder("监听消息"));
        taMsg.setEditable(false);
        panMid.add(scroll);

        // panDown 区域初始化
        panDown.setBorder(new TitledBorder("在线用户"));
        panDown.add(lstUsers);
        lstUsers.setVisibleRowCount(10);

        // 图形界面的总体初始化 + 启动图形界面
        this.setTitle("服务器端");
        this.add(panUp, BorderLayout.NORTH);
        this.add(panMid, BorderLayout.CENTER);
        this.add(panDown, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(600, 400));
        this.pack();
        this.setVisible(true);
    }

    // TODO “启动服务器”按钮的动作事件监听处理类
    private class startServerHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // 当点击按钮时，获取端口设置并启动新进程、监听端口
                localPort = Integer.parseInt(tfLocalPort.getText());
                serverSocket = new ServerSocket(localPort);
                Thread acptThrd = new Thread(new AcceptRunnable());
                acptThrd.start();
                taMsg.append("**** 服务器（端口" + localPort + "）已启动 ****\n");
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    // TODO 接受用户连接请求的线程关联类
    private class AcceptRunnable implements Runnable {
        public void run() {
            // 持续监听端口，当有新用户连接时 再开启新进程
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    // 新的用户已连接，创建 Client 对象
                    Client client = new Client(socket);
                    taMsg.append("——客户【" + client.nickname + "】加入\n");
                    Thread clientThread = new Thread(client);
                    clientThread.start();
                    clients.add(client);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }

        // TODO 服务器存放用户对象的客户类（主要编程）。每当有新的用户连接时，该类都会被调用
        // TODO 该类继承自 Runnable，内部含有 run()方法
        private class Client implements Runnable {
            private Socket socket;      // 用来保存用户的连接对象
            private BufferedReader in;   // IO 流
            private PrintStream out;
            private String nickname;        // 保存用户昵称

            // Client类的构建方法。当有 新用户 连接时会被调用
            public Client(Socket socket) throws Exception {
                this.socket = socket;
                InputStream is = socket.getInputStream();
                in = new BufferedReader(new InputStreamReader(is));
                OutputStream os = socket.getOutputStream();
                out = new PrintStream(os);
                nickname = in.readLine();     // 获取用户昵称
                for (Client c : clients) {   // 将新用户的登录消息发给所有用户
                    c.out.println("——客户【" + nickname + "】加入\n");
                }
            }

            //客户类线程运行方法   
            public void run() {
                try {
                    while (true) {
                        String usermsg   = in.readLine();   //读用户发来消息
                        String secondMsg = usermsg.substring(usermsg.lastIndexOf(":") + 1);   // 字符串辅助对象

                        // 如果用户发过来的消息不为空
                        if (usermsg != null && usermsg.length() > 0) {
                            // 如果消息是 bye，则断开与此用户的连接 并 告知所有用户当前信息，跳出循环终止当前进程
                            if (secondMsg.equals("bye")) {
                                clients.remove(this);
                                for (Client c : clients) {
                                    c.out.println(usermsg);
                                }
                                taMsg.append("——客户离开：" + nickname + "\n");
                                // 更新在线用户数量 lstUsers的界面信息
                                updateUsers();
                                break;
                            }

                            /**
                             * 每当有新用户连接时，服务器就会接收到 USERS 请求
                             * 当服务器接收到此请求时，就会要求现在所有用户更新 在线用户数量 的列表
                             * */
                            if (usermsg.equals("USERS")) {
                                updateUsers();
                                continue;
                            }

                            // 当用户发出的消息都不是以上两者时，消息才会被正常发送
                            for (Client c : clients) {
                                c.out.println(usermsg);
                            }

                        }
                    }
                    socket.close();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }

            // TODO 更新在线用户数量 lstUsers 信息，并要求所有的用户端同步更新
            public void updateUsers() {
                // clientNames 是 Vector<String>对象，用来存放所有用户的名字
                clientNames.removeAllElements();
                StringBuffer allname = new StringBuffer();
                for (AcceptRunnable.Client client : clients) {
                    clientNames.add(0, client.nickname);
                    allname.insert(0, "|" + client.nickname);
                }
                panDown.setBorder(new TitledBorder("在线用户(" +clientNames.size() + "个)"));
                // 要求所有的用户端同步更新
                for (Client c : clients) {
                    c.out.println(clientNames);
                }
                lstUsers.setListData(clientNames);
            }
        }
    }


}