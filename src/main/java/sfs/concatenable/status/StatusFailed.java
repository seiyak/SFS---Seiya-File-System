package sfs.concatenable.status;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusFailed extends Status {

	private static Logger log = Logger.getLogger( StatusFailed.class );

	@Override
	protected void putJson(JSONObject json) {

		try {
			json.put( STATUS_KEY, "FAILED" );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}
	}

}
