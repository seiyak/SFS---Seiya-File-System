package sfs.server.http.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import sfs.header.http.HeaderEntry;
import sfs.header.http.RequestHeaderEntry;
import sfs.mime.Mime;
import sfs.util.http.ViewCreator;
import sfs.verb.http.Verb;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GreetingHandler extends AbstractHandler {

	private List<String> currentCpuInfos = Collections.synchronizedList( new ArrayList<String>() );

	public GreetingHandler(ViewCreator viewCreator) {
		super( viewCreator );
	}

	public void handle(HttpExchange exchange) throws IOException {

		log.info( "got a connection from: " + exchange.getRemoteAddress().getHostName() + " at: "
				+ exchange.getRemoteAddress().getAddress().getHostAddress() );
		doHandle( exchange );
	}

	private void doHandle(HttpExchange exchange) throws IOException {

		if ( exchange.getRequestMethod().equalsIgnoreCase( Verb.GET.toString() ) ) {
			// request method is 'GET'
			doGet( exchange );
		}
	}

	/**
	 * Handles 'GET' request.
	 * 
	 * @param exchange
	 *            Holds http request and response as objects.
	 * @throws IOException
	 */
	private void doGet(HttpExchange exchange) throws IOException {

		handleGreeting( exchange );
	}

	/**
	 * Handles the initial greetings from the nodes as the initiation of connections.
	 * 
	 * @param exchange
	 *            Holds http request and response as objects.
	 * @throws IOException
	 */
	private void handleGreeting(HttpExchange exchange) throws IOException {

		getHeaderEntries(exchange);

		String str = exchange.getRequestHeaders().getFirst( RequestHeaderEntry.GREETING.toString() );

		log.debug( "about to handle greeting with: " + str );

		if ( ( str != null ) && !str.isEmpty() ) {
			// receives CPUInfo from a new node.
			currentCpuInfos.add( str );
		}

		iterateCurrentCpuInfos( exchange );
	}

	/**
	 * Iterates and writes cpu informations that the server has received from the already connected nodes.
	 * 
	 * @param exchange
	 *            Holds http request and response as objects.
	 * @throws IOException
	 */
	private void iterateCurrentCpuInfos(HttpExchange exchange) throws IOException {

		String cpuInfos = "";

		synchronized ( currentCpuInfos ) {
			for ( String str : currentCpuInfos ) {
				cpuInfos += str + "\n";
			}
		}

		setResponseHeaders( exchange, new sfs.entry.Entry[] { new sfs.entry.Entry( HeaderEntry.CONTENT_TYPE.toString(),
				Mime.HTML.toString() ) } );
		writeResponse( exchange, viewCreator.create( cpuInfos, getClass()
						.getClassLoader().getResource( ViewCreator.HTML_RESOUCE ) ) );
	}
}
