package com.ustc.beyondwu.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by beyondwu on 2016/2/22.
 */
public class ClientUI implements ClientObserver {
    private JPanel panel1;
    private JPanel MessagePanel;
    private JTextField ServerIP;
    private JTextField ServerPort;
    private JButton ConnectBtn;
    private JTextField msgInput;
    private JButton SendButton;
    private JTextArea msgContent;
    private NettyClient client;
    private String errorMessage;


    public ClientUI() {
        client = new NettyClient();
        client.register(this);
        client.clientInit();
        msgContent.setLineWrap(true);
        msgContent.setWrapStyleWord(true);
        addConnectListener();
        addSendMsgListener();
    }

    public void addConnectListener() {
        ConnectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serverIP = ServerIP.getText();
                String serverPort = ServerPort.getText();
                Integer port;
                if (serverIP.isEmpty() || serverPort.isEmpty()) {
                    msgContent.setText(ServerIP.getText());
                    msgContent.setText("The IP or Port is Empty");
                } else {
                    try {
                        port = Integer.valueOf(serverPort);
                        client.connServer(serverIP, port);
                    } catch (NullPointerException e1) {
                        e1.printStackTrace();
                        msgContent.setText("Connect Server Error");
                    }catch (Exception e2){
                        e2.printStackTrace();
                        msgContent.setText("Invalid Port");
                    }

                }

            }
        });
    }

    public void addSendMsgListener() {
        SendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMsg(msgInput.getText());
            }
        });
    }

    @Override
    public void update() {
        msgContent.setText(client.getMessages());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("客户端");
        frame.setContentPane(new ClientUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
