package sfs.server.http.handler.contextpath;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import sfs.entry.HTTPHeaderEntry;
import sfs.response.statuscode.StatusCode;
import sfs.stat.message.MessageStat;
import sfs.util.http.JSoupViewCreator;
import sfs.util.http.ViewCreator;

public class WelcomePathHandler extends AbstractContextPathHandler {

	private ViewCreator viewCreator;
	private final String PAGE_LOCATION = "/home/seiyak/Documents/SFS---Seiya-File-System/src/test/resources/Index.html";
	private static Logger log = Logger.getLogger( WelcomePathHandler.class );

	public WelcomePathHandler() {
		viewCreator = new JSoupViewCreator();
	}

	public void setViewCreator(ViewCreator viewCreator) {
		this.viewCreator = viewCreator;
	}

	public void handle(SocketChannel socketChannel, MessageStat messageStat) {

		try {
			socketChannel.write( ByteBuffer.wrap( responseMessage.createMessage( StatusCode._200,
					new HTTPHeaderEntry[] {},
					new String( viewCreator.create( new File( PAGE_LOCATION ).toURI().toURL() ) ) ).getBytes() ) );
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
