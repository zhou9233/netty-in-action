package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * Listing 8.7 Using attributes
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class BootstrapClientWithOptionsAndAttrs {

    public static void main(String[] args) {
        BootstrapClientWithOptionsAndAttrs bootstrapClientWithOptionsAndAttrs = new
                BootstrapClientWithOptionsAndAttrs();
        bootstrapClientWithOptionsAndAttrs.bootstrap();
    }

    /**
     * Listing 8.7 Using attributes
     * */
    public void bootstrap() {
        final AttributeKey<Integer> id = AttributeKey.newInstance("ID");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
            .channel(NioSocketChannel.class)
            .handler(
                new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx)
                        throws Exception {
                        Integer idValue = ctx.channel().attr(id).get();
                        // do something with the idValue
                    }

                    @Override
                    protected void channelRead0(
                        ChannelHandlerContext channelHandlerContext,
                        ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }

                    public void channelActive(ChannelHandlerContext ctx) {
                        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",
                                CharsetUtil.UTF_8));//当被通知Channel是活跃的时候，发送一条消息
                    }

                }
            );
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        bootstrap.attr(id, 123456);
        ChannelFuture future = bootstrap.connect(
            new InetSocketAddress("127.0.0.1", 9000));
        future.syncUninterruptibly();
    }
}
