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
 * @since : 2022/4/27, 7:35 PM, ����
 **/
public class Server extends JFrame {
    // TODO ��ͼ�ν���ӵ���������򣬷ֱ�λ���ϡ��С��� ��up��middle��down����
    private JPanel panUp = new JPanel();
    private JPanel panMid = new JPanel();
    private JPanel panDown = new JPanel();

    // panUp ������ӽڵ㶨�壬��ǩ������򡢰�ť
    private JLabel lblLocalPort = new JLabel("���������������˿�:");
    protected JButton butStart = new JButton("����������");
    protected JTextField tfLocalPort = new JTextField(25);

    // panMid ������ӽڵ㶨�壬��ʾ�� �Լ� ������
    protected JTextArea taMsg = new JTextArea(25, 25);
    JScrollPane scroll = new JScrollPane(taMsg);

    // panDown ������ӽڵ㶨�壬lstUsers�����û�����
    JList lstUsers = new JList();

    // TODO �����Ǵ�����ݵı���
    public static int localPort = 8000;     // Ĭ�϶˿� 8000
    static int SerialNum = 0;       // �û���������
    ServerSocket serverSocket;      // �������� Socket
    ArrayList<AcceptRunnable.Client> clients = new ArrayList<>();        // �û����Ӷ�������
    Vector<String> clientNames = new Vector<>();       // lstUsers �д�ŵ�����

    // TODO ������
    public Server(String[] args) {
        new Server();
    }

    // TODO ���췽��
    public Server() {
        init();
    }

    // TODO ��ʼ����������ʼ��ͼ�ν��沼��
    private void init() {
        // panUp �����ʼ������ʽ����
        panUp.setLayout(new FlowLayout());
        panUp.add(lblLocalPort);
        panUp.add(tfLocalPort);
        panUp.add(butStart);
        tfLocalPort.setText(String.valueOf(localPort));
        butStart.addActionListener(new startServerHandler());   // ע�� "����������" ��ť����¼�

        // panMid �����ʼ��
        panMid.setBorder(new TitledBorder("������Ϣ"));
        taMsg.setEditable(false);
        panMid.add(scroll);

        // panDown �����ʼ��
        panDown.setBorder(new TitledBorder("�����û�"));
        panDown.add(lstUsers);
        lstUsers.setVisibleRowCount(10);

        // ͼ�ν���������ʼ�� + ����ͼ�ν���
        this.setTitle("��������");
        this.add(panUp, BorderLayout.NORTH);
        this.add(panMid, BorderLayout.CENTER);
        this.add(panDown, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(600, 400));
        this.pack();
        this.setVisible(true);
    }

    // TODO ����������������ť�Ķ����¼�����������
    private class startServerHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // �������ťʱ����ȡ�˿����ò������½��̡������˿�
                localPort = Integer.parseInt(tfLocalPort.getText());
                serverSocket = new ServerSocket(localPort);
                Thread acptThrd = new Thread(new AcceptRunnable());
                acptThrd.start();
                taMsg.append("**** ���������˿�" + localPort + "�������� ****\n");
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    // TODO �����û�����������̹߳�����
    private class AcceptRunnable implements Runnable {
        public void run() {
            // ���������˿ڣ��������û�����ʱ �ٿ����½���
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    // �µ��û������ӣ����� Client ����
                    Client client = new Client(socket);
                    taMsg.append("�����ͻ���" + client.nickname + "������\n");
                    Thread clientThread = new Thread(client);
                    clientThread.start();
                    clients.add(client);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }

        // TODO ����������û�����Ŀͻ��ࣨ��Ҫ��̣���ÿ�����µ��û�����ʱ�����඼�ᱻ����
        // TODO ����̳��� Runnable���ڲ����� run()����
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
                                taMsg.append("�����ͻ��뿪��" + nickname + "\n");
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

            // TODO ���������û����� lstUsers ��Ϣ����Ҫ�����е��û���ͬ������
            public void updateUsers() {
                // clientNames �� Vector<String>����������������û�������
                clientNames.removeAllElements();
                StringBuffer allname = new StringBuffer();
                for (AcceptRunnable.Client client : clients) {
                    clientNames.add(0, client.nickname);
                    allname.insert(0, "|" + client.nickname);
                }
                panDown.setBorder(new TitledBorder("�����û�(" +clientNames.size() + "��)"));
                // Ҫ�����е��û���ͬ������
                for (Client c : clients) {
                    c.out.println(clientNames);
                }
                lstUsers.setListData(clientNames);
            }
        }
    }


}