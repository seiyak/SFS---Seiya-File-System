package sfs.client.http;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Readable {
	public String read(SocketChannel channel) throws IOException;
}
