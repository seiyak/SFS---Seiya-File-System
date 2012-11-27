package sfs.concatenable.liveness;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class LivenessTrue extends Liveness {

	private static Logger log = Logger.getLogger( LivenessTrue.class );

	@Override
	protected void putJson(JSONObject json) {

		try {
			json.put( LIVENESS_KEY, "true" );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}
	}
}
