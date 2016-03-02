package com.ustc.beyondwu.server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by beyondwu on 2016/2/22.
 */
public class ServerUI{
    private JPanel panel1;
    private JTextField textField1;
    private JButton SendButton;


    private JTextArea contentArea;
    private  BlockingQueue<String> sendBuffer;
    private Integer SEND_BUFFER_SIZE = 1024;
    private Integer POOL_SIZE = 2;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Server");
        frame.setContentPane(new ServerUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public ServerUI() {
        MessageWritenThread serverThread =  new MessageWritenThread(this);
        sendBuffer = new LinkedBlockingQueue<String>(SEND_BUFFER_SIZE);
        ExecutorService es = Executors.newFixedThreadPool(POOL_SIZE);
        es.execute(serverThread);
       /*try {
            backendServer.serverInit();
        } catch (SSLException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }*/
        addSendMessageListener();
    }

    private void addSendMessageListener() {
        SendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendBuffer.put(textField1.getText().toString());
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public BlockingQueue<String> getSendBuffer(){
        return this.sendBuffer;
    }

    public JTextArea getContentArea() {
        return contentArea;
    }

}
