package sfs.server.http.handler;

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
}
