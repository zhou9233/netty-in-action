package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

/**
 * Listing 8.1 Bootstrapping a client
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
public class BootstrapClient2Shutdown {
    public static void main(String args[]) {
        BootstrapClient2Shutdown client = new BootstrapClient2Shutdown();
        client.bootstrap();
    }

    /**
     * Listing 8.1 Bootstrapping a client
     */
    public void bootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        //创建一个Bootstrap类的实例，以创建和连接新的客户端Channel
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                //指定要使用的Channel实现
                .channel(NioSocketChannel.class)
                //设置用于Channel事件和数据的ChannelInboundHandler
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(
                            ChannelHandlerContext channelHandlerContext,
                            ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) {
                        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",
                                CharsetUtil.UTF_8));//当被通知Channel是活跃的时候，发送一条消息
                    }
                });
        ChannelFuture future =
                //连接到远程主机
                bootstrap.connect(
                        new InetSocketAddress("127.0.0.1", 9000));
        future.syncUninterruptibly();
        /*future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture)
                    throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Connection established");
                } else {
                    System.err.println("Connection attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });*/
        Future<?> future2 = group.shutdownGracefully();
        future2.syncUninterruptibly();

    }
}
