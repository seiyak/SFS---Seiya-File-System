package sfs.server.http.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import sfs.header.http.HeaderEntry;
import sfs.header.http.RequestType;
import sfs.header.http.ending.Ending;
import sfs.mime.Mime;
import sfs.util.date.DateUtil;
import sfs.util.http.ViewCreator;
import sfs.util.string.StringUtil;

import com.sun.net.httpserver.HttpExchange;

public class InspectNodeHandler extends AbstractHandler {

	private static Logger log = Logger.getLogger( InspectNodeHandler.class );

	public InspectNodeHandler(ViewCreator viewCreator) {
		super( viewCreator );
	}

	public void handle(HttpExchange exchange) throws IOException {

		inspectNode( exchange );
	}

	private void inspectNode(HttpExchange exchange) throws IOException {

		log.debug( "got inspect request from: " + exchange.getRemoteAddress().getHostName() );

		String[] values = StringUtil.getValuesOnQuery( exchange.getRequestURI().getQuery(), "node", "port" );

		log.debug( "inspect request for node: " + values[0] + " port: " + values[1] );

		doInspectNode( exchange, values );
	}

	private void doInspectNode(HttpExchange exchange, String[] values) throws IOException {

//		// TODO check port + 1 later.
//		NodeClient node = new NodeClient( values[0], Integer.parseInt( values[1] ) + 1, "/usage/cpuMemory" );
//		node.setRequestTypes( new RequestType[] { RequestType.CPU_MEMORY_USAGE } );
//
//		byte[] inspect = getInspected( node );
//
//		setResponseHeaders( exchange, new sfs.entry.Entry[] { new sfs.entry.Entry( HeaderEntry.CONTENT_TYPE.toString(),
//				Mime.JSON.toString() ) } );
//
//		if ( inspect != null ) {
//			writeResponse( exchange, inspect );
//		}else{
//			//TODO write error message
//		}
	}

//	private byte[] getInspected(NodeClient node) throws IOException {
//		log.debug( "about to initiate for the inspect request" );
//		
//////		node.initiate();
//////		String body = getMessageBody( node.read() );
//////		node.close();
////
////		log.debug( "closed the inspect request" );
////		
////		return body.getBytes();
//	}
}
