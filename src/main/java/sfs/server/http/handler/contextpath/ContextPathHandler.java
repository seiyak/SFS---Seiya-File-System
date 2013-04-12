package sfs.server.http.handler.contextpath;

import java.nio.channels.SocketChannel;

import sfs.stat.message.MessageStat;

public interface ContextPathHandler {

	public void handle(SocketChannel socketChannel,MessageStat messageStat);
}
