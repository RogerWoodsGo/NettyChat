package com.ustc.beyondwu.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


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
        String inputData = "";
        try {
            while (bufIn.isReadable()) { // (1)
                bufIn.readBytes(tmpBytes);
                inputData += new String(tmpBytes);
            }
        } finally {
            System.out.println("readed data: " + inputData + tmpBytes.length);
        }
        ByteBuf sendMsg = Unpooled.buffer(1024);
        sendMsg.writeBytes(inputData.getBytes());
        ChannelFuture cf = ctx.writeAndFlush(sendMsg);
        if (!cf.isSuccess()) {
            System.out.println("Send failed: ");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("Start to Flush!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
