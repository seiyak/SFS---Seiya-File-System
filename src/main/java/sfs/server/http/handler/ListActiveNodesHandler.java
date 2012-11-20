package sfs.server.http.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import sfs.client.http.manager.NodeManager;
import sfs.header.http.HeaderEntry;
import sfs.mime.Mime;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ListActiveNodesHandler extends AbstractHandler {

	private final NodeManager nodeManager;
	private static Logger log = Logger.getLogger( ListActiveNodesHandler.class );

	public ListActiveNodesHandler(NodeManager nodeManager) {
		super( null );
		this.nodeManager = nodeManager;
	}

	public void handle(HttpExchange exchange) throws IOException {

		log.debug( "got list active nodes request from "
				+ exchange.getRemoteAddress().getAddress().getCanonicalHostName() );

		setResponseHeaders( exchange, new sfs.entry.Entry[] { new sfs.entry.Entry( HeaderEntry.CONTENT_TYPE.toString(),
				Mime.JSON.toString() ) } );
		//TODO comment out.
		//writeResponse( exchange, nodeManager.getAllNodesAsJson().getBytes() );

	}

}
