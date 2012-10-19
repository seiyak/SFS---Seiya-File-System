package sfs.concatenable.date;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import sfs.concatenable.Concatenable;
import sfs.util.date.DateUtil;

public class Date extends Concatenable {

	private static Logger log = Logger.getLogger( Date.class );

	@Override
	protected void putJson(JSONObject json) {

		try {
			json.put( "timestamp", DateUtil.getTimeInGMT() );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}
	}
}
