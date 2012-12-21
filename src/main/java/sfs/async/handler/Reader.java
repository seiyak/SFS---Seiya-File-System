package sfs.async.handler;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import sfs.stat.message.MessageStat;

public interface Reader {

	public MessageStat read(SocketChannel channel) throws IOException;
}
