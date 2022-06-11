package com.ascent.ui;
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

/**
 * @author : ThinkStu
 * @since : 2022/4/27, 7:35 PM, 周三
 **/
public class Client extends JFrame {      //客户机窗体类
    // TODO 该图形界面拥有四块区域，分别位于上、左、中、下 （up、Left、middle、down）。
    private JPanel panUp = new JPanel();
    private JPanel panLeft = new JPanel();
    private JPanel panMid = new JPanel();
    private JPanel panDown = new JPanel();

    // panUp 区域的子节点定义，3个标签、3个输入框、2个按钮
    private JLabel lblLocalPort1 = new JLabel("服务器IP: ");
    private JLabel lblLocalPort2 = new JLabel("端口: ");
    private JLabel lblLocalPort3 = new JLabel("本人昵称: ");
    protected JTextField tfLocalPort1 = new JTextField(15);
    protected JTextField tfLocalPort2 = new JTextField(5);
    protected JTextField tfLocalPort3 = new JTextField(5);
    protected JButton butStart = new JButton("连接服务器");
    protected JButton butStop = new JButton("断开服务器");
    // TODO

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
    public static String nickname = "Cat";      // 默认用户名
    public Socket socket;
    public static String msg;       // 存放本次发送的消息
    Vector<String> clientNames = new Vector<>();


    // TODO 主方法
    public Client(String[] args) {
        new Client();
    }

    // TODO 构造方法
    public Client() {
        init();
    }

    // TODO 初始化方法：初始化图形界面
    private void init() {
        // panUp 区域初始化：流式面板，3个标签、3个输入框，2个按钮
        panUp.setLayout(new FlowLayout());
        panUp.add(lblLocalPort1);
        panUp.add(tfLocalPort1);
        panUp.add(lblLocalPort2);
        panUp.add(tfLocalPort2);
        panUp.add(lblLocalPort3);
        panUp.add(tfLocalPort3);
        tfLocalPort1.setText(localIP);
        tfLocalPort2.setText(String.valueOf(localPort));
        tfLocalPort3.setText(nickname);
        panUp.add(butStart);
        panUp.add(butStop);
        butStart.addActionListener(new linkServerHandlerStart());
        butStop.addActionListener(new linkServerHandlerStop());
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
        // TODO 此处注意：JTextField输入框 的回车事件默认存在，无需添加
        panDown.setLayout(new FlowLayout());
        panDown.add(lblLocalPort4);
        panDown.add(tfLocalPort4);
        tfLocalPort4.addActionListener(new Client.SendHandler());

        // 图形界面的总体初始化 + 启动图形界面
        this.setTitle("客户端");
        this.add(panUp, BorderLayout.NORTH);
        this.add(panLeft, BorderLayout.WEST);
        this.add(panMid, BorderLayout.CENTER);
        this.add(panDown, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowHandler());
        this.setPreferredSize(new Dimension(800, 600));
        this.pack();
        this.setVisible(true);
    }

    // TODO “连接服务器”按钮的动作事件监听处理类：
    private class linkServerHandlerStart implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 当点击"连接服务器"按钮之后，该按钮被禁用（不可重复点击）。同时"断开服务器按钮"被恢复使用
            butStart.setEnabled(false);
            butStop.setEnabled(true);
            localIP = tfLocalPort1.getText();
            localPort = Integer.parseInt(tfLocalPort2.getText());
            nickname = tfLocalPort3.getText();
            linkServer();   // 连接服务器
            Thread acceptThread = new Thread(new Client.ReceiveRunnable());
            acceptThread.start();
        }
    }

    // TODO “断开服务器”按钮的动作事件监听处理类
    private class linkServerHandlerStop implements ActionListener {
        /**
         * 当点击该按钮之后，断开服务器连接、清空图形界面所有数据
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            taMsg.setText("");
            clientNames = new Vector<>();
            updateUsers();
            out.println("——客户【" + nickname + "】离开:bye\n");
            butStart.setEnabled(true);
            butStop.setEnabled(false);
        }
    }

    // TODO 连接服务器的方法
    public void linkServer() {
        try {
            socket = new Socket(localIP, localPort);
        } catch (Exception ex) {
            taMsg.append("==== 连接服务器失败~ ====");
        }
    }

    // TODO 接收服务器消息的线程关联类
    private class ReceiveRunnable implements Runnable {
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintStream(socket.getOutputStream());
                out.println(nickname);      // 当用户首次连接服务器时，应该向服务器发送自己的用户名、方便服务器区分
                taMsg.append("——本人【" + nickname + "】成功连接到服务器......\n");
                out.println("USERS");       // 向服务器发送"神秘代码"，请求 当前在线用户 列表
                while (true) {
                    msg = in.readLine();       // 读取服务器端的发送的数据
                    // 此 if 语句的作用是：过滤服务器发送过来的 更新当前在线用户列表 请求
                    if (msg.matches(".*\\[.*\\].*")) {
                        clientNames.removeAllElements();
                        String[] split = msg.split(",");
                        for (String single : split) {
                            clientNames.add(single);
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
                        clientNames.remove(nickname);
                        updateUsers();
                        break;       // 终止线程
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    // TODO "发送消息文本框" 的动作事件监听处理类
    private class SendHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            out.println("【" + nickname + "】:" + tfLocalPort4.getText());
            tfLocalPort4.setText("");       // 当按下回车发送消息之后，输入框应该被清空
        }
    }

    // TODO 窗口关闭的动作事件监听处理类
    // 当用户点击 "x" 离开窗口时，也会向服务器发送 bye 请求，目的是为了同步更新数据。
    private class WindowHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            cutServer();
        }
    }

    private void cutServer() {
        out.println("——客户【" + nickname + "】离开:bye");
    }

    // TODO 更新 "在线用户列表" 的方法
    public void updateUsers() {
        panMid.setBorder(new TitledBorder("在线用户(" + clientNames.size() + "个)"));
        lstUsers.setListData(clientNames);
    }


}
