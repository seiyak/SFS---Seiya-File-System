package sfs.client.http.shortconversation;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import sfs.entry.HostEntry;

public interface ShortConversation {

	public void writeRequest(SocketChannel socketChannel, HostEntry hostEntry) throws IOException;
}
