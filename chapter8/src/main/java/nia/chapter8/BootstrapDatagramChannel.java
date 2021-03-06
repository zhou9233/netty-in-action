package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * Listing 8.8 Using Bootstrap with DatagramChannel
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
public class BootstrapDatagramChannel {

    public static void main(String[] args) {
        BootstrapDatagramChannel bootstrapDatagramChannel = new
                BootstrapDatagramChannel();
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
                    System.out.println("receive msg");
                    System.out.println(msg);
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
