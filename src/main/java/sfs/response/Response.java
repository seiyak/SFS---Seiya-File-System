package sfs.response;

import org.apache.log4j.Logger;

import sfs.response.statuscode.StatusCode;

public abstract class Response {

	private static Logger log = Logger.getLogger( Response.class );

	protected Response() {

	}

	public final String response(StatusCode statusCode) {

		String responseStr = doResponse( statusCode );
		logResponse( responseStr );

		return responseStr;
	}

	private void logResponse(String responseStr) {

		log.info( "response looks like this:\n" + responseStr );
	}

	protected abstract String doResponse(StatusCode statusCode);
}
