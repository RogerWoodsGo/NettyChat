package com.ustc.beyondwu.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by beyondwu on 2016/2/23.
 */
public class MessageHandler extends ChannelInboundHandlerAdapter {

    //private final ByteBuf sendMessage;
    private NettyClient client;

    /**
     * Creates a client-side handler.
     */
    public MessageHandler(NettyClient client) {
//        firstMessage = Unpooled.buffer(1024);
//        for (int i = 0; i < firstMessage.capacity(); i ++) {
//            firstMessage.writeByte((byte) i);
//        }
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // ctx.writeAndFlush(sendMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // ctx.write(msg);
        ByteBuf bufIn = (ByteBuf) msg;
        byte[] tmpBytes = new byte[NettyClient.BUFFER_SIZE];
        String inputData = new String();
        try {
            while (bufIn.isReadable()) { // (1)
                bufIn.readBytes(tmpBytes);
                inputData += tmpBytes.toString();
                //System.out.print((char) in.readByte());
                //System.out.flush();

            }
        } finally {
            ReferenceCountUtil.release(msg); // (2)
            client.receiveMsg(inputData);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
