package sfs.request;

import org.apache.log4j.Logger;

import sfs.verb.http.Verb;

public abstract class Request {

	private static Logger log = Logger.getLogger( Request.class );

	protected Request() {

	}

	public final String request(Verb verb, String resource) {

		String requestStr = doRequest( verb, resource );
		logRequest( requestStr );

		return requestStr;
	}

	private void logRequest(String requestStr) {

		log.info( "request looks like this:\n" + requestStr );
	}

	protected abstract String doRequest(Verb verb, String resource);
}
