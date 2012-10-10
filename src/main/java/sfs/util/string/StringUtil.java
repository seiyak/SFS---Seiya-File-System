package sfs.util.string;

import java.util.HashMap;
import java.util.Map;

import sfs.header.http.separator.Equal;
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
	 * Gets a Map based on the query string. Expects one simple query string like 'XXX=YYY'.
	 * 
	 * @param query
	 *            Query String like 'XXX=YYY'.
	 * @return Map holding the key and value from the query string.
	 */
	public static Map<String, String> getQueryAsMap(String query) {

		Map<String, String> map = new HashMap<String, String>();
		String[] each = query.split( new Equal().getSeparator() );

		map.put( each[0], each[1] );

		return map;
	}
}
