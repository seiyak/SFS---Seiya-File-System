package sfs.concatenable.ip.local;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import sfs.concatenable.Concatenable;
import sfs.util.ipaddress.LocalIPAddress;

public class LocalIPAddressInfo extends Concatenable {

	private static Logger log = Logger.getLogger( LocalIPAddressInfo.class );

	@Override
	protected void putJson(JSONObject json) {

		try {
			json.put( "origin", LocalIPAddress.getLocalIPAddress().get( "v4" ) );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}
	}
}
