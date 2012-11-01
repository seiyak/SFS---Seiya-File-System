package sfs.server.http.handler;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import sfs.header.http.HeaderEntry;
import sfs.mime.Mime;
import sfs.response.statuscode.StatusCode;
import sfs.util.http.ViewCreator;

public class WelcomeHandler extends AbstractHandler {

	public WelcomeHandler(ViewCreator viewCreator) {
		super( viewCreator );
	}

	public void handle(HttpExchange exchange) throws IOException {

		setResponseHeaders( exchange, new sfs.entry.Entry[] { new sfs.entry.Entry( HeaderEntry.CONTENT_TYPE.toString(),
				Mime.HTML.toString() ) } );
		writeResponse( exchange, StatusCode._200.getNumber(),
				viewCreator.create( getClass().getClassLoader().getResource( ViewCreator.HTML_RESOUCE ) ) );

	}

}
