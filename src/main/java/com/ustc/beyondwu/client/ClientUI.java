package com.ustc.beyondwu.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by beyondwu on 2016/2/22.
 */
public class ClientUI {
    private JPanel panel1;
    private JTextField ServerIP;
    private JTextField ServerPort;
    private JButton ConnectBtn;
    private JPanel MessagePanel;
    private JTextField textField1;
    private JButton 发送Button;

    public ClientUI() {
        ConnectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("客户端");
        frame.setContentPane(new ClientUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
