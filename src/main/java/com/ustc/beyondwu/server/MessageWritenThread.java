package com.ustc.beyondwu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by beyondwu on 2016/2/24.
 */
public class MessageWritenThread implements Runnable{
        private String message = "";

        public MessageWritenThread() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Ready to chat ");
            while(true) {
                try {
                    message = in.readLine();
                } catch (IOException e) {
                    message = "";
                }
                if(!message.isEmpty()) {
                    message = "[SERVER BROADCAST] " + message + "\r\n";
                    //ChatServerhandler.sendServerMessage(message);
                    message = "";
                }
            }

        }
}
