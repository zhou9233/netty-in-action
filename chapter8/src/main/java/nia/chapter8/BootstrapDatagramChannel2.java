package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * Listing 8.8 Using Bootstrap with DatagramChannel
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
public class BootstrapDatagramChannel2 {

    public static void main(String[] args) {
        BootstrapDatagramChannel2 bootstrapDatagramChannel = new
                BootstrapDatagramChannel2();
        bootstrapDatagramChannel.bootstrap();
    }

    /**
     * Listing 8.8 Using Bootstrap with DatagramChannel
     */
    public void bootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        //设置EventLoopGroup,其提供了用以处理Channel事件的EventLoop
        bootstrap.group(new OioEventLoopGroup()).channel(
                //创建一个Bootstrap的实例以创建和绑定新的数据包Channel
            OioDatagramChannel.class).handler(
            new SimpleChannelInboundHandler<DatagramPacket>() {
                @Override
                public void channelRead0(ChannelHandlerContext ctx,
                    DatagramPacket msg) throws Exception {
                    // Do something with the packet
                }
                @Override
                public void channelActive(ChannelHandlerContext ctx) {
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",
                            CharsetUtil.UTF_8));//当被通知Channel是活跃的时候，发送一条消息
                }
            }
        );
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(9000));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture)
               throws Exception {
               if (channelFuture.isSuccess()) {
                   System.out.println("Channel bound");
               } else {
                   System.err.println("Bind attempt failed");
                   channelFuture.cause().printStackTrace();
               }
            }
        });
    }
}
