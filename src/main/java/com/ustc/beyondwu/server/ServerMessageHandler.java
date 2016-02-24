package com.ustc.beyondwu.server;

import com.ustc.beyondwu.client.NettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by beyondwu on 2016/2/24.
 */
public class ServerMessageHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Received Message");
        System.out.println(msg.toString());
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
            System.out.println("readed data: " + inputData + tmpBytes.length);
        }
        ByteBuf sendMsg = Unpooled.buffer(1024);
        sendMsg.writeBytes(new String(inputData).getBytes());
        ChannelFuture cf = ctx.writeAndFlush(sendMsg);
        if (!cf.isSuccess()) {
            System.out.println("Send failed: " + cf.cause());
        }
        //ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("Start to Flush!");
        //ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
