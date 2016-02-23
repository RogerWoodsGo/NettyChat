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

    private String newMessage;
    private String messages;
    private MessageHandler msgHandler;
    private List<String> historyMessage;
    private List<ClientObserver> observerList;

    public NettyClient() {
        messages = new String();
        historyMessage = new ArrayList<String>();
        observerList = new ArrayList<ClientObserver>();
    }

    public void clientInit() {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        msgHandler = new MessageHandler(this);
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            chanPipeline = ch.pipeline();



                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            chanPipeline.addLast(msgHandler);
                        }
                    });
        }
        catch (Exception e1){
            e1.printStackTrace();
        }
        finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getMessages() {
        Iterator<String> iter = historyMessage.iterator();
        while (iter.hasNext()) {
            messages += iter.next();
            messages += "\r\n";
        }
        return messages;
    }

    public int connServer(String IP, Integer port) {
        // Start the client.
        try {
            chanFuture = bootstrap.connect(IP, port).sync();
            // Wait until the connection is closed.
            chanFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void sendMsg(String msg) {
        //Send to Server
        ByteBuf sendMsg = Unpooled.buffer(BUFFER_SIZE);
        sendMsg.writeBytes(msg.getBytes());
        chanPipeline.writeAndFlush(sendMsg);
        newMessage = clientName + ": " + msg;
        if (historyMessage.size() > HISTORY_LIST_SIZE)
            historyMessage.remove(0);
        historyMessage.add(newMessage);
        notifyAll();
    }

    public void receiveMsg(String msg){
        if (historyMessage.size() > HISTORY_LIST_SIZE)
            historyMessage.remove(0);
        historyMessage.add("server: " + msg);
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
