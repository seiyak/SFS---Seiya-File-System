package sfs.concatenable.port;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import sfs.concatenable.Concatenable;

public class PortInfo extends Concatenable {

	private final int port;
	private final String key;
	private static Logger log = Logger.getLogger( PortInfo.class );

	public PortInfo(int port) {
		this.key = "port";
		this.port = port;
	}
	
	public PortInfo(String key, int port) {
		this.key = key;
		this.port = port;
	}

	@Override
	protected void putJson(JSONObject json) {
		try {
			json.put( key, port );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}
	}
}
