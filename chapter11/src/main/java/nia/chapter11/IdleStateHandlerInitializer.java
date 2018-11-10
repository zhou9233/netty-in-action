package nia.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Listing 11.7 Sending heartbeats
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                //IdleStateHandler将在被触发时发送一个IdleStateEvent事件
                //如果连接超过60秒没有接收或者发送任何消息，那么 IdleStateHandler
                //将会使用一个IdleStateEvent 事件来调用 fireUserEventTriggered()方法
                new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
        //将一个HeartbeatHandler添加到ChannelPipeline中
        pipeline.addLast(new HeartbeatHandler());
    }

    //实现userEventTriggered()方法以发送心跳消息
    public static final class HeartbeatHandler
            extends ChannelInboundHandlerAdapter {
        //发送到远程节点的心跳消息
        private static final ByteBuf HEARTBEAT_SEQUENCE =
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                        "HEARTBEAT", CharsetUtil.ISO_8859_1));
        //HeartbeatHandler 实现了 userEventTriggered()方法，如果这个方法检测到 IdleStateEvent 事件，它将会发送心
        //跳消息，并且添加一个将在发送操作失败时关闭该连接的 ChannelFutureListener
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx,
                                       Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                //发送心跳消息，并在发送失败时关闭该连接
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                        .addListener(
                                ChannelFutureListener.CLOSE_ON_FAILURE);
                //不是IdleStateEvent事件，所以将它传递给下一个ChannelInboundHandler
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
