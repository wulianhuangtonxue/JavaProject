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
 * @since : 2022/4/27, 7:35 PM, ����
 **/
public class Client extends JFrame {      //�ͻ���������
    // TODO ��ͼ�ν���ӵ���Ŀ����򣬷ֱ�λ���ϡ����С��� ��up��Left��middle��down����
    private JPanel panUp = new JPanel();
    private JPanel panLeft = new JPanel();
    private JPanel panMid = new JPanel();
    private JPanel panDown = new JPanel();

    // panUp ������ӽڵ㶨�壬3����ǩ��3�������2����ť
    private JLabel lblLocalPort1 = new JLabel("������IP: ");
    private JLabel lblLocalPort2 = new JLabel("�˿�: ");
    private JLabel lblLocalPort3 = new JLabel("�����ǳ�: ");
    protected JTextField tfLocalPort1 = new JTextField(15);
    protected JTextField tfLocalPort2 = new JTextField(5);
    protected JTextField tfLocalPort3 = new JTextField(5);
    protected JButton butStart = new JButton("���ӷ�����");
    protected JButton butStop = new JButton("�Ͽ�������");
    // TODO

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
    public static String nickname = "Cat";      // Ĭ���û���
    public Socket socket;
    public static String msg;       // ��ű��η��͵���Ϣ
    Vector<String> clientNames = new Vector<>();


    // TODO ������
    public Client(String[] args) {
        new Client();
    }

    // TODO ���췽��
    public Client() {
        init();
    }

    // TODO ��ʼ����������ʼ��ͼ�ν���
    private void init() {
        // panUp �����ʼ������ʽ��壬3����ǩ��3�������2����ť
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
        // TODO �˴�ע�⣺JTextField����� �Ļس��¼�Ĭ�ϴ��ڣ��������
        panDown.setLayout(new FlowLayout());
        panDown.add(lblLocalPort4);
        panDown.add(tfLocalPort4);
        tfLocalPort4.addActionListener(new Client.SendHandler());

        // ͼ�ν���������ʼ�� + ����ͼ�ν���
        this.setTitle("�ͻ���");
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

    // TODO �����ӷ���������ť�Ķ����¼����������ࣺ
    private class linkServerHandlerStart implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // �����"���ӷ�����"��ť֮�󣬸ð�ť�����ã������ظ��������ͬʱ"�Ͽ���������ť"���ָ�ʹ��
            butStart.setEnabled(false);
            butStop.setEnabled(true);
            localIP = tfLocalPort1.getText();
            localPort = Integer.parseInt(tfLocalPort2.getText());
            nickname = tfLocalPort3.getText();
            linkServer();   // ���ӷ�����
            Thread acceptThread = new Thread(new Client.ReceiveRunnable());
            acceptThread.start();
        }
    }

    // TODO ���Ͽ�����������ť�Ķ����¼�����������
    private class linkServerHandlerStop implements ActionListener {
        /**
         * ������ð�ť֮�󣬶Ͽ����������ӡ����ͼ�ν�����������
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            taMsg.setText("");
            clientNames = new Vector<>();
            updateUsers();
            out.println("�����ͻ���" + nickname + "���뿪:bye\n");
            butStart.setEnabled(true);
            butStop.setEnabled(false);
        }
    }

    // TODO ���ӷ������ķ���
    public void linkServer() {
        try {
            socket = new Socket(localIP, localPort);
        } catch (Exception ex) {
            taMsg.append("==== ���ӷ�����ʧ��~ ====");
        }
    }

    // TODO ���շ�������Ϣ���̹߳�����
    private class ReceiveRunnable implements Runnable {
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintStream(socket.getOutputStream());
                out.println(nickname);      // ���û��״����ӷ�����ʱ��Ӧ��������������Լ����û������������������
                taMsg.append("�������ˡ�" + nickname + "���ɹ����ӵ�������......\n");
                out.println("USERS");       // �����������"���ش���"������ ��ǰ�����û� �б�
                while (true) {
                    msg = in.readLine();       // ��ȡ�������˵ķ��͵�����
                    // �� if ���������ǣ����˷��������͹����� ���µ�ǰ�����û��б� ����
                    if (msg.matches(".*\\[.*\\].*")) {
                        clientNames.removeAllElements();
                        String[] split = msg.split(",");
                        for (String single : split) {
                            clientNames.add(single);
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
                        clientNames.remove(nickname);
                        updateUsers();
                        break;       // ��ֹ�߳�
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    // TODO "������Ϣ�ı���" �Ķ����¼�����������
    private class SendHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            out.println("��" + nickname + "��:" + tfLocalPort4.getText());
            tfLocalPort4.setText("");       // �����»س�������Ϣ֮�������Ӧ�ñ����
        }
    }

    // TODO ���ڹرյĶ����¼�����������
    // ���û���� "x" �뿪����ʱ��Ҳ������������� bye ����Ŀ����Ϊ��ͬ���������ݡ�
    private class WindowHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            cutServer();
        }
    }

    private void cutServer() {
        out.println("�����ͻ���" + nickname + "���뿪:bye");
    }

    // TODO ���� "�����û��б�" �ķ���
    public void updateUsers() {
        panMid.setBorder(new TitledBorder("�����û�(" + clientNames.size() + "��)"));
        lstUsers.setListData(clientNames);
    }


}
