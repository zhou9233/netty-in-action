package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

import java.nio.Buffer;

public class TestShortToByteEncoder {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 3; i++) {
            buf.writeByte(0);
        }
        buf.writeByte(5);
        EmbeddedChannel channel = new EmbeddedChannel(new ShortToByteEncoder());
        channel.writeOutbound(buf);
        ByteBuf read = (ByteBuf) channel.readOutbound();
        System.out.println();
    }
}
