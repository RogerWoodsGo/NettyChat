package com.ustc.beyondwu.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by beyondwu on 2016/2/23.
 */
public class ClientMessageHandler extends ChannelInboundHandlerAdapter {

    //private final ByteBuf sendMessage;
    private NettyClient client;

    /**
     * Creates a client-side handler.
     */
    public ClientMessageHandler(NettyClient client) {
//        firstMessage = Unpooled.buffer(1024);
//        for (int i = 0; i < firstMessage.capacity(); i ++) {
//            firstMessage.writeByte((byte) i);
//        }
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // ctx.writeAndFlush(sendMessage);
        System.out.println("Connect Successfully");
       // ctx.writeAndFlush((ByteBuf)Unpooled.buffer(1024).writeBytes(new String("abc").getBytes()));
    }

   @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // ctx.write(msg);
       System.out.println("Get Message");
       ByteBuf bufIn = (ByteBuf) msg;
       byte[] tmpBytes = new byte[bufIn.readableBytes()];
       String inputData = new String();
       try {
           while (bufIn.isReadable()) { // (1)
               bufIn.readBytes(tmpBytes);
               // System.out.println(bufIn.readByte());
               inputData += new String(tmpBytes);
               //System.out.print((char) in.readByte());
               //System.out.flush();

           }
       } finally {
           System.out.println("received data: " + inputData + tmpBytes.length);
       }
       client.updateMsg("server: " + inputData);

    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}

/*
class ChatClientInitializer extends ChannelInitializer<SocketChannel> {
    NettyClient client;
    public ChatClientInitializer(NettyClient client) {
        this.client = client;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

       */
/* pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());*//*


        pipeline.addLast("handler", new ClientMessageHandler(client));
    }
}*/
