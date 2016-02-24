package com.ustc.beyondwu.client;



import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by beyondwu on 2016/2/22.
 */
public class NettyClient {

    private String clientName = "beyondwu";
    public static final int BUFFER_SIZE = 1024;
    public static final int HISTORY_LIST_SIZE = 30;
    private String serverIP;
    private Integer serverPort;
    private ChannelFuture chanFuture;
    private Bootstrap bootstrap;
    private ChannelPipeline chanPipeline;
    private EventLoopGroup group;
    private Channel socketChannel;

    private String newMessage;
    private String messages;
    private ClientMessageHandler msgHandler;
    private List<String> historyMessage;
    private List<ClientObserver> observerList;

    public NettyClient() {
        messages = new String();
        historyMessage = new ArrayList<String>();
        observerList = new ArrayList<ClientObserver>();
    }

    public void clientInit() {

        // Configure the client.
         group = new NioEventLoopGroup();
        msgHandler = new ClientMessageHandler(this);
        try {
            bootstrap = new Bootstrap();
           // msgHandler = new ClientMessageHandler();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            chanPipeline = socketChannel.pipeline();
                            chanPipeline.addLast(msgHandler);
                        }
                    });
           // socketChannel = bootstrap.connect(serverIP, serverPort).sync().channel();

        }
        catch (Exception e1){
            e1.printStackTrace();
        }

    }


    public String getMessages() {
        Iterator<String> iter = historyMessage.iterator();
        messages = "";
        while (iter.hasNext()) {
            messages += iter.next();
            messages += "\r\n";
        }
        System.out.println(messages);
        return messages;
    }

    public int connServer(String IP, Integer port) {
        // Start the client.
        try {
            chanFuture = bootstrap.connect(IP, port).sync();
            socketChannel = chanFuture.channel();

            // Wait until the connection is closed.
            System.out.println("connect");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            // Shut down the event loop to terminate all threads.
            //System.out.println("close");
           // group.shutdownGracefully();
        }
        return 1;
    }

    public void sendMsg(String msg) {
        //Send to Server
        ByteBuf sendMsg = Unpooled.buffer(BUFFER_SIZE);
        sendMsg.writeBytes(msg.getBytes());
        System.out.println("Going to Send: " + msg);
        socketChannel.writeAndFlush(sendMsg);
        newMessage = clientName + ": " + msg;
        updateMsg(newMessage);
    }

    public  void updateMsg(String msg){
        if (historyMessage.size() > HISTORY_LIST_SIZE)
            historyMessage.remove(0);
        historyMessage.add(msg);
        //System.out.println(historyMessage.size());
        notifyObserver();
    }

    public void register(ClientObserver observer) {
        observerList.add(observer);
    }

    public void notifyObserver() {
        Iterator<ClientObserver> iter = observerList.iterator();
        while (iter.hasNext()) {
            iter.next().update();//获得各Observer
        }
    }
}
