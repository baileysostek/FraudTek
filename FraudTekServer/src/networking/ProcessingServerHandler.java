package networking;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Bailey on 3/30/2018.
 */
public class ProcessingServerHandler
        extends ChannelInboundHandlerAdapter {
    private ByteBuf tmp;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("Handler added");
        tmp = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("Handler removed");
        tmp.release();
        tmp = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RequestData m = (RequestData) msg;
        if (m!=null) {
            System.out.println(m.getIntValue()+":"+m.getStringValue());
            // request processing
            RequestData requestData = m;
            ResponseData responseData = new ResponseData();
            responseData.setIntValue(requestData.getIntValue() * 2);
            ChannelFuture future = ctx.writeAndFlush(responseData);
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
