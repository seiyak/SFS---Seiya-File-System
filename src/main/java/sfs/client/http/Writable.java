package sfs.client.http;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Writable {
	public void write(SocketChannel serverChannel, String message) throws IOException;
}
