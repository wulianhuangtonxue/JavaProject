package com.ascent.util;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import com.ascent.ui.Client;
public class Server extends JFrame {

    private ServerFrame serverFrame;
    public static int localPort = 8000;     // 默认端口 8000
    static int SerialNum = 0;       // 用户连接数量
    ServerSocket serverSocket;      // 服务器端 Socket
    ArrayList<AcceptRunnable.Client> clients = new ArrayList<>();        // 用户连接对象数组
    Vector<String> clientNames = new Vector<>();       // lstUsers 中存放的数据
    public Server(String[] args) {
        new Server();
    }     //主方法

    public Server() {
        open();
        serverFrame = new ServerFrame();
    }

    //打开服务器
    public void open(){
        try {
            serverSocket = new ServerSocket(localPort);
            Thread acptThrd = new Thread(new AcceptRunnable());
            acptThrd.start();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // 接受用户连接请求的线程关联类
    private class AcceptRunnable implements Runnable {
        public void run() {
            // 持续监听端口，当有新用户连接时 再开启新进程
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    // 新的用户已连接，创建 Client 对象
                    Client client = new Client(socket);
                    Thread clientThread = new Thread(client);
                    clientThread.start();
                    clients.add(client);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
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


            // 更新在线用户数量 lstUsers 信息，并要求所有的用户端同步更新
            public void updateUsers() {
                // clientNames 是 Vector<String>对象，用来存放所有用户的名字
                clientNames.removeAllElements();
                StringBuffer allname = new StringBuffer();
                for (AcceptRunnable.Client client : clients) {
                    clientNames.add(0, client.nickname);
                    allname.insert(0, "|" + client.nickname);
                }
                // 要求所有的用户端同步更新
                for (Client c : clients) {
                    c.out.println(clientNames);
                }
            }
        }
    }

}