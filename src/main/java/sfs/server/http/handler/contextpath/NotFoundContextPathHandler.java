package sfs.server.http.handler.contextpath;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import sfs.server.http.page.notfound.NotFoundWriter;
import sfs.server.http.page.notfound.SimpleNotFoundWriter;
import sfs.stat.message.MessageStat;

public class NotFoundContextPathHandler extends AbstractContextPathHandler {

	private NotFoundWriter notFoundWriter;
	private static Logger log = Logger.getLogger( NotFoundContextPathHandler.class );

	public NotFoundContextPathHandler() {

	}

	public NotFoundContextPathHandler(NotFoundWriter notFoundWriter) {
		this.notFoundWriter = notFoundWriter;
	}

	public void setNotFoundWriter(NotFoundWriter notFoundWriter) {
		this.notFoundWriter = notFoundWriter;
	}

	public NotFoundWriter getNotFoundWriter() {
		return notFoundWriter;
	}

	public void handle(SocketChannel socketChannel, MessageStat messageStat) {

		try {
			
			if(notFoundWriter == null){
				log.warn( "notFoundWriter is null. about to set SimpleNotFoundWriter instead." );
				notFoundWriter = new SimpleNotFoundWriter();
			}
			
			socketChannel.write( ByteBuffer.wrap( notFoundWriter.write( messageStat ).getBytes() ) );
		}
		catch ( IOException e ) {
			log.error( e );
		}
		log.error( "no exact match found in the context paths. about to it with StringUtil.startsWith() ..." );
	}

}
