package sfs.util.string;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import sfs.header.http.separator.Slash;

public class StringUtil {

	/**
	 * Gets contextPath starting with '/'.
	 * 
	 * @param contextPath
	 *            Context path.
	 * @return contextPath without modification if it starts with '/', otherwise '/' put before contextPath.
	 */
	public static String getContextPath(String contextPath) {

		if ( startWithSlash( contextPath ) ) {
			return contextPath;
		}

		return new Slash().getSeparator() + contextPath;
	}

	/**
	 * Checks if contextPath starts with '/'.
	 * 
	 * @param contextPath
	 *            Context path.
	 * @return True if it starts with '/', false otherwise.
	 */
	private static boolean startWithSlash(String contextPath) {

		return contextPath.charAt( 0 ) == new Slash().getSeparator().charAt( 0 );
	}

	/**
	 * Gets a Map based on the query string.
	 * 
	 * @param query
	 *            Query String like 'XXX=YYY'.
	 * @return Map holding the key and value from the query string.
	 */
	public static Map<String, String> getQueryAsMap(String query) {

		if ( ( query == null ) || ( query.length() == 0 ) ) {
			return Collections.EMPTY_MAP;
		}

		Map<String, String> map = new LinkedHashMap<String, String>();

		int current = 0, previous = 0;
		String key = "", value = "";
		while ( current < query.length() ) {

			if ( query.charAt( current ) == '=' ) {
				key = query.substring( previous, current );
				previous = current + 1;
				// found key value separator
			}
			else if ( query.charAt( current ) == '&' ) {
				// found key value pair separatorS
				value = query.substring( previous, current );
				previous = current + 1;

				map.put( key, value );
			}
			current++;
		}

		if ( query.charAt( previous - 1 ) == '=' ) {
			// no '&' found
			map.put( key, query.substring( previous, query.length() ) );
		}

		return Collections.unmodifiableMap( map );
	}

	/**
	 * Gets values for the specified keys.
	 * 
	 * @param query
	 *            Query String containing key value pairs.
	 * @param keys
	 *            Corresponding keys to the values that are returned.
	 * @return String array holds the corresponding values.
	 */
	public static String[] getValuesOnQuery(String query, String... keys) {

		if ( ( keys == null ) || ( keys.length == 0 ) ) {
			return null;
		}

		String[] res = new String[keys.length];

		Map<String, String> map = getQueryAsMap( query );
		for ( int i = 0; i < keys.length; i++ ) {
			res[i] = map.get( keys[i] );
		}

		return res;
	}
}
