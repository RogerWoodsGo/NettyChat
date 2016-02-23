package com.ustc.beyondwu.server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by beyondwu on 2016/2/22.
 */
public class ServerUI {
    private JPanel panel1;
    private JTextField textField1;
    private JButton SendButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Server");
        frame.setContentPane(new ServerUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public ServerUI() {
        NettyServer backendServer = new NettyServer();
        backendServer.serverInit();
        SendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
