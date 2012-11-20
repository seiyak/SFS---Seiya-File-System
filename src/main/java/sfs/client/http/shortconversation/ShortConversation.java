package sfs.client.http.shortconversation;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import sfs.client.http.Clientable;
import sfs.entry.HostEntry;
import sfs.response.http.ResponseMessage;

public interface ShortConversation {

	public void writeRequest(Clientable clientable, SocketChannel socketChannel, HostEntry hostEntry)
			throws IOException;

	public ResponseMessage readResponse(Clientable clientable, SocketChannel socketChannel, HostEntry hostEntry)
			throws IOException;
}
