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



public class ServerFrame extends JFrame {      //�ͻ���������
    // ������������
    private JPanel panUp = new JPanel();
    private JPanel panLeft = new JPanel();
    private JPanel panMid = new JPanel();
    private JPanel panDown = new JPanel();

    // panUp ������ӽڵ㶨�壬3����ǩ��3�������2����ť
    private JLabel lblLocalPort3 = new JLabel("�����ǳ�: ");
    protected JTextField tfLocalPort3 = new JTextField(5);
    protected JButton butStart = new JButton("��������");
    protected JButton butStop = new JButton("��������");

    // panLeft ������ӽڵ㶨�壬��ʾ�򡢹�����
    protected JTextArea taMsg = new JTextArea(25, 25);
    JScrollPane scroll = new JScrollPane(taMsg);

    // panMid ������ӽڵ㶨�壬lstUsers�����û�����
    JList lstUsers = new JList();

    // panDown ������ӽڵ㶨�壬��ǩ�������
    private JLabel lblLocalPort4 = new JLabel("��Ϣ�����س����ͣ�: ");
    protected JTextField tfLocalPort4 = new JTextField(20);
    /**
     * ===== �����ָ� =====
     * ������ͼ�ν�������������Ǵ�����ݵı���
     */
    BufferedReader in;
    PrintStream out;
    public static int localPort = 8000;     // Ĭ�϶˿�
    public static String localIP = "127.0.0.1";     // Ĭ�Ϸ�����IP��ַ
    public static String nickname = "�ͷ�";      // Ĭ���û���
    public Socket socket;
    public static String msg;       // ��ű��η��͵���Ϣ
    Vector<String> ServerNames = new Vector<>();


    // ������
    public ServerFrame(String[] args) {
        new com.ascent.util.ServerFrame();
    }
    public ServerFrame() {
        init();
        linkServer();
        Thread acceptThread = new Thread(new ServerFrame.ReceiveRunnable());
        acceptThread.start();
    }

    // ��ʼ������
    private void init() {
        // panUp �����ʼ������ʽ��壬3����ǩ��3�������2����ť
        panUp.setLayout(new FlowLayout());
        panUp.add(lblLocalPort3);
        panUp.add(tfLocalPort3);
        tfLocalPort3.setText(nickname);
        panUp.add(butStart);
        panUp.add(butStop);
        butStart.addActionListener(new ServerFrame.linkServerHandlerStart());
        butStop.addActionListener(new ServerFrame.linkServerHandlerStop());
        butStop.setEnabled(false);      // �Ͽ���������ť�ĳ�ʼ״̬Ӧ��Ϊ ���ɵ����ֻ�����ӷ�����֮����ܵ��

        // ��� Left
        taMsg.setEditable(false);
        panLeft.add(scroll);
        panLeft.setBorder(new TitledBorder("���졪����Ϣ��"));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // ��� Middle
        panMid.setBorder(new TitledBorder("�����û�"));
        panMid.add(lstUsers);
        lstUsers.setVisibleRowCount(20);

        // ��� Down
        panDown.setLayout(new FlowLayout());
        panDown.add(lblLocalPort4);
        panDown.add(tfLocalPort4);
        panDown.setSize(100,220);
        tfLocalPort4.addActionListener(new ServerFrame.SendHandler());

        // ͼ�ν���������ʼ�� + ����ͼ�ν���
        this.setTitle("�ͷ���");
        this.add(panUp, BorderLayout.NORTH);
        this.add(panLeft, BorderLayout.WEST);
        this.add(panMid, BorderLayout.CENTER);
        this.add(panDown, BorderLayout.SOUTH);
        this.addWindowListener(new WindowCloser());
        this.setPreferredSize(new Dimension(800, 600));
        this.pack();
        this.setVisible(true);
    }

    // ���ӷ���������¼�
    private class linkServerHandlerStart implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            nickname = tfLocalPort3.getText();
            linkServer();
            Thread acceptThread = new Thread(new ServerFrame.ReceiveRunnable());
            acceptThread.start();
        }
    }

    // �Ͽ�����������¼�
    private class linkServerHandlerStop implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ServerNames = new Vector<>();
            updateUsers();
            out.println("�����ͻ���" + nickname + "���뿪:bye\n");
            butStart.setEnabled(true);
            butStop.setEnabled(false);
        }
    }

    //���ӷ�����
    public void linkServer() {
        try {
            socket = new Socket(localIP, localPort);
            butStart.setEnabled(false);
            butStop.setEnabled(true);
        } catch (Exception ex) {
            taMsg.append("==== ���ӷ�����ʧ��~ ====");
        }
    }

    //�Ͽ�������
    public void stopServer(){
        taMsg.setText("");
        ServerNames = new Vector<>();
        updateUsers();
        out.println("�����ͻ���" + nickname + "���뿪:bye\n");
        butStart.setEnabled(true);
        butStop.setEnabled(false);
    }

    // ������Ϣ
    private class ReceiveRunnable implements Runnable {
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintStream(socket.getOutputStream());
                out.println(nickname);      // ���û��״����ӷ�����ʱ��������������Լ����û��������ڷ���������
                taMsg.append("�������ˡ�" + nickname + "���ɹ����ӵ�������......\n");
                out.println("USERS");       // �����������ǰ�����û��б�
                while (true) {
                    msg = in.readLine();       // ��ȡ�������˵ķ��͵�����
                    // �� if ���������ǣ����˷��������͹����� ���µ�ǰ�����û��б� ����
                    if (msg.matches(".*\\[.*\\].*")) {
                        ServerNames.removeAllElements();
                        String[] split = msg.split(",");
                        for (String single : split) {
                            ServerNames.add(single);
                        }
                        updateUsers();
                        continue;
                    }

                    // ���� "���졪����Ϣ��" ��Ϣ
                    taMsg.append(msg + "\n");

                    // �� if ������ã����������������ȷ����Ϣ��
                    // �����յ��������˷��͵�ȷ���뿪����bye ��ʱ���û���������
                    msg = msg.substring(msg.lastIndexOf("��") + 1);
                    if (msg.equals(nickname)) {
                        socket.close();
                        ServerNames.remove(nickname);
                        updateUsers();
                        break;       // ��ֹ�߳�
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    // ������Ϣ
    private class SendHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            out.println("��" + nickname + "��:" + tfLocalPort4.getText());
            tfLocalPort4.setText("");
        }
    }

    // �رմ���
    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            stopServer();
            setVisible(false);
            dispose();
        }
    }

    private void cutServer() {
        out.println("�����ͻ���" + nickname + "���뿪:bye");
    }

    // ���������û�
    public void updateUsers() {
        panMid.setBorder(new TitledBorder("�����û�(" + ServerNames.size() + "��)"));
        lstUsers.setListData(ServerNames);
    }


}

