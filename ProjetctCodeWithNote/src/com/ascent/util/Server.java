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
    public static int localPort = 8000;     // Ĭ�϶˿� 8000
    static int SerialNum = 0;       // �û���������
    ServerSocket serverSocket;      // �������� Socket
    ArrayList<AcceptRunnable.Client> clients = new ArrayList<>();        // �û����Ӷ�������
    Vector<String> clientNames = new Vector<>();       // lstUsers �д�ŵ�����
    public Server(String[] args) {
        new Server();
    }     //������

    public Server() {
        open();
        serverFrame = new ServerFrame();
    }

    //�򿪷�����
    public void open(){
        try {
            serverSocket = new ServerSocket(localPort);
            Thread acptThrd = new Thread(new AcceptRunnable());
            acptThrd.start();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // �����û�����������̹߳�����
    private class AcceptRunnable implements Runnable {
        public void run() {
            // ���������˿ڣ��������û�����ʱ �ٿ����½���
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    // �µ��û������ӣ����� Client ����
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
            private Socket socket;      // ���������û������Ӷ���
            private BufferedReader in;   // IO ��
            private PrintStream out;
            private String nickname;        // �����û��ǳ�

            // Client��Ĺ������������� ���û� ����ʱ�ᱻ����
            public Client(Socket socket) throws Exception {
                this.socket = socket;
                InputStream is = socket.getInputStream();
                in = new BufferedReader(new InputStreamReader(is));
                OutputStream os = socket.getOutputStream();
                out = new PrintStream(os);
                nickname = in.readLine();     // ��ȡ�û��ǳ�
                for (Client c : clients) {   // �����û��ĵ�¼��Ϣ���������û�
                    c.out.println("�����ͻ���" + nickname + "������\n");
                }
            }

            //�ͻ����߳����з���
            public void run() {
                try {
                    while (true) {
                        String usermsg   = in.readLine();   //���û�������Ϣ
                        String secondMsg = usermsg.substring(usermsg.lastIndexOf(":") + 1);   // �ַ�����������

                        // ����û�����������Ϣ��Ϊ��
                        if (usermsg != null && usermsg.length() > 0) {
                            // �����Ϣ�� bye����Ͽ�����û������� �� ��֪�����û���ǰ��Ϣ������ѭ����ֹ��ǰ����
                            if (secondMsg.equals("bye")) {
                                clients.remove(this);
                                for (Client c : clients) {
                                    c.out.println(usermsg);
                                }
                                // ���������û����� lstUsers�Ľ�����Ϣ
                                updateUsers();
                                break;
                            }

                            /**
                             * ÿ�������û�����ʱ���������ͻ���յ� USERS ����
                             * �����������յ�������ʱ���ͻ�Ҫ�����������û����� �����û����� ���б�
                             * */
                            if (usermsg.equals("USERS")) {
                                updateUsers();
                                continue;
                            }

                            // ���û���������Ϣ��������������ʱ����Ϣ�Żᱻ��������
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


            // ���������û����� lstUsers ��Ϣ����Ҫ�����е��û���ͬ������
            public void updateUsers() {
                // clientNames �� Vector<String>����������������û�������
                clientNames.removeAllElements();
                StringBuffer allname = new StringBuffer();
                for (AcceptRunnable.Client client : clients) {
                    clientNames.add(0, client.nickname);
                    allname.insert(0, "|" + client.nickname);
                }
                // Ҫ�����е��û���ͬ������
                for (Client c : clients) {
                    c.out.println(clientNames);
                }
            }
        }
    }

}