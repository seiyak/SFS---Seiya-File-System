package sfs.util.json;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import sfs.entry.Entry;
import sfs.entry.HostEntry;
import sfs.response.http.ResponseMessage;
import sfs.util.reflection.MethodPrefix;
import sfs.util.reflection.ReflectionUtil;

public class JSONUtil {

	private static final String SETTER_PREFIX = "set";
	private static Logger log = Logger.getLogger( JSONUtil.class );

	public static String getJSON(Entry... entries) {

		JSONObject json = new JSONObject();
		for ( Entry entry : entries ) {
			try {
				json.put( entry.getKey(), entry.getValue() );
			}
			catch ( JSONException ex ) {
				log.error( ex );
				return "";
			}
		}
		return json.toString();
	}

	public static String getJSON(HostEntry[] hostEntries, Entry... entries) {

		JSONObject json = new JSONObject();
		for ( Entry entry : entries ) {
			try {
				json.put( entry.getKey(), entry.getValue() );
			}
			catch ( JSONException ex ) {
				log.error( ex );
				return "";
			}
		}

		JSONObject jsn = null;
		for ( int i = 0; i < hostEntries.length; i++ ) {
			try {
				jsn = new JSONObject();
				jsn.put( ResponseMessage.KEY_HOST, hostEntries[i].getHost() );
				jsn.put( ResponseMessage.KEY_PORT, hostEntries[i].getPort() );
				jsn.put( ResponseMessage.KEY_MAX_TRIAL, hostEntries[i].getMaxTrial() );
			}
			catch ( JSONException ex ) {
				log.error( ex );
			}
		}

		try {
			json.append( ResponseMessage.KEY_NEXT_HOSTS, jsn );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}

		return json.toString();
	}

	/**
	 * Gets Object type of T from the specified JSON string.
	 * 
	 * @param cls
	 *            Used to create Object type of the class.
	 * @param jsonStr
	 *            JSON representation.
	 * @return Object type of the class.
	 */
	public static <T> T get(Class<T> cls, String jsonStr) {

		JSONObject json = null;
		T t = null;
		try {
			json = new JSONObject( jsonStr );
			t = ReflectionUtil.getInstance( cls );
			Map<String, Method> map = ReflectionUtil.getMethodsAsMap( MethodPrefix.SET, cls );

			if ( t != null ) {
				String key = "";
				for ( Iterator<String> itr = json.keys(); itr.hasNext(); ) {
					key = itr.next();
					char[] chars = key.toCharArray();
					chars[0] = (char) ( ( (int) chars[0] ) - 32 );

					ReflectionUtil.invokeSetter( t, map.get( SETTER_PREFIX + String.copyValueOf( chars ) ),
							json.get( key ) );
				}
			}
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}

		return t;
	}
}
