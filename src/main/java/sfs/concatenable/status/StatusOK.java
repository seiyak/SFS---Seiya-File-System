package sfs.concatenable.status;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusOK extends Status {

	private static Logger log = Logger.getLogger( StatusOK.class );

	@Override
	protected void putJson(JSONObject json) {

		try {
			json.put( STATUS_KEY, "OK" );
		}
		catch ( JSONException ex ) {

		}
	}

}
