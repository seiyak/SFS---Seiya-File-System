package sfs.concatenable.liveness;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class LivenessFalse extends Liveness {

	private static Logger log = Logger.getLogger( LivenessFalse.class );

	@Override
	protected void putJson(JSONObject json) {

		try {
			json.put( LIVENESS_KEY, "false" );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}

	}

}
