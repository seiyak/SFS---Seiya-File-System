package sfs.util.http;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSoupViewCreator implements ViewCreator {

	private Document document;
	private static Logger log = Logger.getLogger( JSoupViewCreator.class );

	public byte[] create(URL url) {

		if ( parse( url ) ) {
			// url is detected.
			log.debug( "url, " + url + " is detected." );

			return doCreate();
		}

		return null;
	}

	private boolean parse(URL url) {

		try {
			document = Jsoup.parse( new File( url.toURI() ), null );
			return true;
		}
		catch ( URISyntaxException ex ) {
			log.error( ex );
		}
		catch ( IOException ex ) {
			log.error( ex );
		}

		return false;
	}

	private byte[] doCreate() {
		return document.toString().getBytes();
	}
}
