package sfs.server.http.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import sfs.client.http.manager.NodeManager;
import sfs.header.http.RequestHeaderEntry;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GreetingHandler implements HttpHandler {

	private final NodeManager nodeManager;
	private static Logger log = Logger.getLogger( GreetingHandler.class );

	public GreetingHandler(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void handle(HttpExchange exchange) throws IOException {

		log.info( "got a connection from: " + exchange.getRemoteAddress().getHostName() + " at: "
				+ exchange.getRemoteAddress().getAddress().getHostAddress() );

		nodeManager.add( exchange.getRequestHeaders().getFirst( RequestHeaderEntry.ORIGIN.toString() ), exchange
				.getRequestHeaders().getFirst( RequestHeaderEntry.GREETING.toString() ) );
	}
}
