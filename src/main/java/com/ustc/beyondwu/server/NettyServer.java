package com.ustc.beyondwu.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by beyondwu on 2016/2/22.
 */
public class NettyServer {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final Integer HISTORY_LIST_SIZE = 1000;
    private List<ServerObserver> serverObservers;
    private List<String> historyMessages;
    private Channel socketChannel;
    private ChannelHandlerContext channelContext;

    public NettyServer() {
        historyMessages = new ArrayList<String>();
        serverObservers = new ArrayList<ServerObserver>();
    }

    public void registerObserver(ServerObserver observer) {
        serverObservers.add(observer);
    }

    public void sendMsg(String msg) {
        //send
        // historyMessages.add("server: " + msg);
        ByteBuf sendMsg = Unpooled.buffer(1024);
        sendMsg.writeBytes(msg.getBytes());
       /* if(!channelContext.isRemoved()){
            System.out.println("channel is not ready!");
            return;
        }*/
        ChannelFuture cf = channelContext.writeAndFlush(sendMsg);
        if (!cf.isSuccess()) {
            System.out.println("Send failed: " + cf.cause());
        }
       // ServerMessageHandler.sendMessage(sendMsg);
        System.out.println("send Msg: " + msg);
        updateMsg("server: " + msg);
    }

    public void updateMsg(String msg) {
        if (historyMessages.size() > HISTORY_LIST_SIZE)
            historyMessages.remove(0);
        historyMessages.add(msg);
        notifyObserver();
    }

    private void notifyObserver() {
        Iterator<ServerObserver> iter = serverObservers.iterator();
        while (iter.hasNext()) {
            iter.next().update();//获得各Observer
        }
    }

    public String getMessages() {
        String messages;
        Iterator<String> iter = historyMessages.iterator();
        messages = "";
        while (iter.hasNext()) {
            messages += iter.next();
            messages += "\r\n";
        }
        return messages;
    }

    public void serverInit() throws SSLException, CertificateException {
        // Configure SSL.

        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc;

            ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();

        } else {
            sslCtx = null;
        }

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            ServerMessageHandler msgHandler = new ServerMessageHandler();
                            p.addLast(msgHandler);
                            channelContext =  p.context(msgHandler);
                        }
                    });

            // Start the server.
            ChannelFuture f;
            try {
                f = b.bind(5600).sync();
                //socketChannel = f.channel();
                f.channel().closeFuture().sync();
                // Wait until the server socket is closed.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
