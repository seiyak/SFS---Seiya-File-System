package sfs.server.http.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import sfs.util.http.ViewCreator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class AbstractHandler implements HttpHandler {

	protected final ViewCreator viewCreator;
	protected static Logger log = Logger.getLogger( GreetingHandler.class );

	protected AbstractHandler(ViewCreator viewCreator) {
		this.viewCreator = viewCreator;
	}
	
	protected void getHeaderEntries(HttpExchange exchange){
		for ( Entry<String, List<String>> entry : exchange.getRequestHeaders().entrySet() ) {

			String s = "";
			for ( String entryStr : entry.getValue() ) {
				s += entryStr + " ";
			}

			log.info( "key: " + entry.getKey() + " : " + s );
		}
	}

	/**
	 * Sets header entries on the response header.
	 * 
	 * @param exchange
	 *            Holds response header.
	 * @param headerEntries
	 *            Stores response header keys and values to be added to the response header.
	 */
	protected final void setResponseHeaders(HttpExchange exchange, sfs.entry.Entry[] headerEntries) {

		if ( headerEntries != null && headerEntries.length > 0 ) {
			for ( sfs.entry.Entry entry : headerEntries ) {
				exchange.getResponseHeaders().set( entry.getKey(), entry.getValue() );
			}
		}
	}
	
	protected final void writeResponse(HttpExchange exchange, byte[] output) throws IOException{
		
		exchange.sendResponseHeaders( 200, output.length );
		OutputStream out = exchange.getResponseBody();
		try {
			out.write( output );
		}
		catch ( IOException ex ) {
			// TODO instead of throwing exception here, probably better to write an error message
			// on the page.
			throw ex;
		}
		finally {
			if ( out != null ) {
				out.close();
			}
		}
	}
}
