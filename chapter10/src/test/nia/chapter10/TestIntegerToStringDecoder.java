package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

public class TestIntegerToStringDecoder {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 3; i++) {
            buf.writeByte(0);
        }
        buf.writeByte(5);
        EmbeddedChannel channel = new EmbeddedChannel(
                new IntegerToStringDecoder()
        );
        channel.writeInbound(buf.readInt());
        String read = (String)channel.readInbound();
        System.out.println(read);
    }


}
