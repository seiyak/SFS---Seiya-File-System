package sfs.client.http;

import java.io.IOException;

public interface Clientable extends Readable, Writable {

	public void initiate() throws IOException;

	public void close() throws IOException;
}
