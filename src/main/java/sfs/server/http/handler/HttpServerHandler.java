package sfs.server.http.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import sfs.header.http.HeaderEntry;
import sfs.mime.Mime;
import sfs.verb.http.Verb;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpServerHandler implements HttpHandler {

	private List<String> currentCpuInfos = Collections.synchronizedList( new ArrayList<String>() );
	private static Logger log = Logger.getLogger( HttpServerHandler.class );

	public void handle(HttpExchange exchange) throws IOException {

		log.info( "got a connection from: " + exchange.getRemoteAddress().getHostName() + " at: "
				+ exchange.getRemoteAddress().getAddress().getHostAddress() );
		doHandle( exchange );
	}

	private void doHandle(HttpExchange exchange) throws IOException {

		if ( exchange.getRequestMethod().equalsIgnoreCase( Verb.GET.getVerb() ) ) {
			// request method is 'GET'
			doGet( exchange );
		}
		else if ( exchange.getRequestMethod().equalsIgnoreCase( Verb.POST.getVerb() ) ) {
			// request method is 'POST'
			// TODO implement for 'POST'
		}
		else if ( exchange.getRequestMethod().equalsIgnoreCase( Verb.DELETE.getVerb() ) ) {
			// request method is 'DELETE'
			// TODO implement for 'DELETE'
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

		for ( Entry<String, List<String>> entry : exchange.getRequestHeaders().entrySet() ) {

			String s = "";
			for ( String entryStr : entry.getValue() ) {
				s += entryStr + " ";
			}

			log.info( "key: " + entry.getKey() + " : " + s );
		}

		String str = exchange.getRequestHeaders().getFirst( HeaderEntry.GREETING.toString() );

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

		exchange.getResponseHeaders().set( HeaderEntry.CONTENT_TYPE.toString(), Mime.HTML.toString() );
		exchange.sendResponseHeaders( 200, cpuInfos.length() );
		OutputStream out = exchange.getResponseBody();

		try {
			out.write( cpuInfos.getBytes() );
		}
		catch ( IOException ex ) {
			throw ex;
		}
		finally {
			if ( out != null ) {
				out.close();
			}
		}
	}

	private void doPost(HttpExchange exchange) {
		// TODO implement this.
	}

	private void doDelete(HttpExchange exchange) {
		// TODO impolement this.
	}
}
