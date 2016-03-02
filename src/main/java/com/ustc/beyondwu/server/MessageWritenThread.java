package com.ustc.beyondwu.server;


import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * Created by beyondwu on 2016/2/24.
 */
class BindThread extends Thread {
    NettyServer ntyServer;

    public BindThread(NettyServer server) {
        this.ntyServer = server;
    }

    public void run() {
        try {
            ntyServer.serverInit();
        } catch (SSLException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }
}

public class MessageWritenThread implements Runnable, ServerObserver {
    ServerUI frontServer;
    NettyServer nettyServer;

    public MessageWritenThread(ServerUI server) {
        frontServer = server;
        nettyServer = new NettyServer();
        nettyServer.registerObserver(this);
        new BindThread(nettyServer).start();
        //new Thread(this).start();
    }

    @Override
    public void run() {
        //nettyServer.serverInit();
        while (true) {
            String sendMsg = "";
            try {
                System.out.println("now send msg: " + sendMsg);
                sendMsg = frontServer.getSendBuffer().take();
                System.out.println("after now send msg: " + sendMsg);
                nettyServer.sendMsg(sendMsg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //nettyServer.(sendMsg);
        }
    }

    @Override
    public void update() {//can delegate
        frontServer.getContentArea().setText(nettyServer.getMessages());
    }
}
