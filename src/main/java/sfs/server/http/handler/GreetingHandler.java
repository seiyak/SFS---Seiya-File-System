package sfs.server.http.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import sfs.client.http.manager.NodeManager;
import sfs.header.http.HeaderEntry;
import sfs.header.http.RequestHeaderEntry;
import sfs.mime.Mime;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GreetingHandler extends AbstractHandler {

	private final NodeManager nodeManager;
	private static final String GREETING_BACK = "{\"status\": \"OK\",\"message\": \"Welcome to SFS!\"}";
	private static final sfs.entry.Entry[] GREETING_BACK_HEADER_ENTRIES = new sfs.entry.Entry[]{new sfs.entry.Entry(HeaderEntry.CONTENT_TYPE.toString(),Mime.JSON.toString())};
	private static Logger log = Logger.getLogger( GreetingHandler.class );

	public GreetingHandler(NodeManager nodeManager) {
		super( null );
		this.nodeManager = nodeManager;
	}

	public void handle(HttpExchange exchange) throws IOException {

		log.info( "got a connection from: " + exchange.getRemoteAddress().getHostName() + " at: "
				+ exchange.getRemoteAddress().getAddress().getHostAddress() );

		nodeManager.add( exchange.getRequestHeaders().getFirst( RequestHeaderEntry.ORIGIN.toString() ), exchange
				.getRequestHeaders().getFirst( RequestHeaderEntry.GREETING.toString() ) );

		setResponseHeaders( exchange, GREETING_BACK_HEADER_ENTRIES );
		writeResponse( exchange, GREETING_BACK.getBytes() );
	}
}
