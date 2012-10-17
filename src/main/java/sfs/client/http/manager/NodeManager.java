package sfs.client.http.manager;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NodeManager {

	private final ConcurrentMap<String, String> map;
	private static Logger log = Logger.getLogger( NodeManager.class );

	public NodeManager() {
		map = new ConcurrentHashMap<String, String>();
	}

	public void add(String key, String value) {

		map.putIfAbsent( key, value );
	}

	public String getAllNodesAsJson() {

		JSONArray array = new JSONArray();
		Iterator<String> itr = map.values().iterator();
		while ( itr.hasNext() ) {
			try {
				array.put( new JSONObject( itr.next() ) );
			}
			catch ( JSONException ex ) {
				log.error( ex );
			}
		}

		JSONObject json = new JSONObject();
		try {
			json.put( "cpuInfos", array );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}
		
		return json.toString();
	}
}
