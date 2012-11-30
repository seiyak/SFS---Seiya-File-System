package sfs.server.http.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import sfs.client.http.manager.NodeManager;
import sfs.entry.Entry;
import sfs.entry.HostEntry;
import sfs.entry.StatusEntryable;
import sfs.header.http.HeaderEntry;
import sfs.header.http.RequestHeaderEntry;
import sfs.header.http.ResponseHeaderEntry;
import sfs.mime.Mime;
import sfs.response.statuscode.StatusCode;
import sfs.structure.Node;
import sfs.structure.StructureNode;
import sfs.structure.tree.MultiNode;
import sfs.structure.tree.Star;
import sfs.util.json.JSONUtil;

import com.sun.net.httpserver.HttpExchange;

public class GreetingHandler extends AbstractHandler {

	private final NodeManager<StructureNode> nodeManager;
	private final List<sfs.entry.Entry> greetingBackHeaderList;
	private static Logger log = Logger.getLogger( GreetingHandler.class );

	public GreetingHandler(NodeManager<StructureNode> nodeManager) {
		super( null );
		this.nodeManager = nodeManager;
		greetingBackHeaderList = new ArrayList<sfs.entry.Entry>();
		greetingBackHeaderList.add( new sfs.entry.Entry( HeaderEntry.CONTENT_TYPE.toString(), Mime.JSON.toString() ) );
	}

	public void handle(HttpExchange exchange) throws IOException {

		log.info( "got a connection from: " + exchange.getRemoteAddress().getHostName() + " at: "
				+ exchange.getRemoteAddress().getAddress().getHostAddress() );

		if ( nodeManager.isStructureNull() ) {
			// structure is null. Use Star as default.
			nodeManager.setStructure( new Star() );
		}

		Node node = generateNode( exchange.getRequestHeaders().getFirst( RequestHeaderEntry.GREETING.toString() ) );
		StatusEntryable status = nodeManager.add( new MultiNode( node ) );
		String response = createResponseMessage( status.getHostEntries(), status.getStatus() );
		log.debug("about to send to " + exchange.getRemoteAddress().getAddress() + " response: " + response);
		writeResponseBack( exchange, status.getStatus()[0].getValue(), response );
	}

	private String createResponseMessage(HostEntry[] hostEntries, Entry[] entries) {

		return JSONUtil.getJSON( hostEntries, entries );
	}

	private Node generateNode(String greeting) {

		return JSONUtil.get( Node.class, greeting );
	}

	private void writeResponseBack(HttpExchange exchange, String status, String response) throws IOException {

		if ( status.equals( "OK" ) ) {
			greetingBackHeaderList.add(new sfs.entry.Entry(ResponseHeaderEntry.GREETING_BACK.toString(),"true"));
			setResponseHeaders( exchange, greetingBackHeaderList.toArray( new sfs.entry.Entry[greetingBackHeaderList.size()] ));
			writeResponse( exchange, StatusCode._200.getNumber(), response.getBytes() );
		}
		else {
			greetingBackHeaderList.add(new sfs.entry.Entry(ResponseHeaderEntry.GREETING_BACK.toString(),"false"));
			setResponseHeaders( exchange, greetingBackHeaderList.toArray( new sfs.entry.Entry[greetingBackHeaderList.size()] ));
			writeResponse( exchange, StatusCode._400.getNumber(), response.getBytes() );
		}
	}
}
