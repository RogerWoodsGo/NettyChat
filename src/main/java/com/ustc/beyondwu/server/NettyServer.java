package com.ustc.beyondwu.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by beyondwu on 2016/2/22.
 */
public class NettyServer {
    ChannelPipeline chanPipeline;

    public NettyServer() {
    }

    public void serverInit(){
        // Configure SSL.

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
                            chanPipeline= ch.pipeline();
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            chanPipeline.addLast(null);
                        }
                    });

            // Start the server.
            ChannelFuture f = null;
            try {
                f = b.bind("127.0.0.1",5600).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Wait until the server socket is closed.
            try {
                f.channel().closeFuture().sync();
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
