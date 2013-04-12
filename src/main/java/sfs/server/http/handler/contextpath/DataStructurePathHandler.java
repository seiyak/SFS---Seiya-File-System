package sfs.server.http.handler.contextpath;

import java.nio.channels.SocketChannel;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import sfs.stat.message.MessageStat;

public class DataStructurePathHandler extends AbstractContextPathHandler {

	private static Logger log = Logger.getLogger( DataStructurePathHandler.class );

	public void handle(SocketChannel socketChannel, MessageStat messageStat) {

		log.debug( "got connection here" );
		for ( Entry<String, String> entry : messageStat.getQueryParameters().entrySet() ) {
			log.debug( "query: " + entry.getKey() + " = " + entry.getValue() );
		}
	}

}
