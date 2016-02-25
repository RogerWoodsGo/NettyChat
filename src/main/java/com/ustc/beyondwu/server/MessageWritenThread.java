package com.ustc.beyondwu.server;

/**
 * Created by beyondwu on 2016/2/24.
 */
/*public class MessageWritenThread implements Runnable{


        public MessageWritenThread() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            String message;
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
}*/
