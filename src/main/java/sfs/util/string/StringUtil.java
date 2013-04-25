package sfs.util.string;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

			if ( query.charAt( current ) == '?' ) {
				previous = current + 1;
			}

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

		if (previous > 0 && query.charAt( previous - 1 ) == '=' ) {
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

	/**
	 * Searches the specified pattern within the specified string and returns true if and only if the pattern is found,
	 * false otherwise. This method implements The Boyer-Moore Fast String Searching Algorithm.
	 * 
	 * @param str
	 *            String where the pattern is searched.
	 * @param pattern
	 *            To be searched within the string.
	 * @return True if the pattern is found, false otherwise.
	 */
	public static boolean searchByBM(String str, String pattern) {

		Map<Character, Integer> shiftMapFromLast = calculateShift( pattern.toCharArray() );

		return doSearch( str.toCharArray(), pattern.toCharArray(), shiftMapFromLast );
	}

	/**
	 * Searches the specified pattern within the specified string and returns the first index of the occurrence if and
	 * only if the pattern is found,
	 * false otherwise. This method implements The Boyer-Moore Fast String Searching Algorithm.
	 * 
	 * @param str
	 *            String where the pattern is searched.
	 * @param pattern
	 *            To be searched within the string.
	 * @return The first index of the occurrence if the pattern is found, -1 otherwise.
	 */
	public static int searchFirstIndexOfByMB(String str, String pattern) {

		Map<Character, Integer> shiftMapFromLast = calculateShift( pattern.toCharArray() );

		int index = doSearchIndexOf( str.toCharArray(), pattern.toCharArray(), shiftMapFromLast );
		return index >= 0 ? ( index - ( pattern.length() - 1 ) ) : -1;
	}

	/**
	 * Searches the specified pattern within the specified string and returns the first index of the occurrence if and
	 * only if the pattern is found,
	 * false otherwise. This method implements The Boyer-Moore Fast String Searching Algorithm.
	 * 
	 * @param str
	 *            String where the pattern is searched.
	 * @param pattern
	 *            To be searched within the string.
	 * @param from
	 *            Starting index.
	 * @return The first index of the occurrence if the pattern is found, -1 otherwise.
	 */
	public static int searchFirstIndexOfByMB(String str, String pattern, int from) {

		if ( str.length() <= from ) {
			throw new IllegalArgumentException( "invalid from, " + from + " index is specified for str, " + str );
		}

		Map<Character, Integer> shiftMapFromLast = calculateShift( pattern.toCharArray() );
		int index = doSearchIndexOf( Arrays.copyOfRange( str.toCharArray(), from, str.length() ),
				pattern.toCharArray(), shiftMapFromLast );
		return index >= 0 ? ( from + ( index - ( pattern.length() - 1 ) ) ) : -1;
	}

	/**
	 * Searches the specified pattern within the specified string and returns the last index of the occurrence if and
	 * only if the pattern is found,
	 * false otherwise. This method implements The Boyer-Moore Fast String Searching Algorithm.
	 * 
	 * @param str
	 *            String where the pattern is searched.
	 * @param pattern
	 *            To be searched within the string.
	 * @return The last index of the occurrence if the pattern is found, -1 otherwise.
	 */
	public static int searchLastIndexOfByMB(String str, String pattern) {

		Map<Character, Integer> shiftMapFromLast = calculateShift( pattern.toCharArray() );

		int index = doSearchIndexOf( str.toCharArray(), pattern.toCharArray(), shiftMapFromLast );

		return index < str.length() ? index : -1;
	}

	/**
	 * Searches the specified pattern within the specified string and returns the last index of the occurrence if and
	 * only if the pattern is found,
	 * false otherwise. This method implements The Boyer-Moore Fast String Searching Algorithm.
	 * 
	 * @param str
	 *            String where the pattern is searched.
	 * @param pattern
	 *            To be searched within the string.
	 * @param from
	 *            Starting index.
	 * @return The last index of the occurrence if the pattern is found, -1 otherwise.
	 */
	public static int searchLastIndexOfByMB(String str, String pattern, int from) {

		if ( str.length() <= from ) {
			throw new IllegalArgumentException( "invalid from, " + from + " index is specified for str, " + str );
		}

		Map<Character, Integer> shiftMapFromLast = calculateShift( pattern.toCharArray() );
		int index = doSearchIndexOf( Arrays.copyOfRange( str.toCharArray(), from, str.length() ),
				pattern.toCharArray(), shiftMapFromLast );
		return index >= 0 ? ( from + index ) : -1;
	}

	/**
	 * Searches the specified pattern to see the parameter, str starts with that.
	 * 
	 * @param str
	 *            Search is taken place.
	 * @param pattern
	 *            Used to check if the str starts with.
	 * @return The index, N - 1 which is the length of pattern if the str starts with the pattern, -1 otherwise.
	 */
	public static int startsWith(String str, String pattern) {

		if ( str.length() < pattern.length() ) {
			return -1;
		}

		Map<Character, Integer> shiftMapFromLast = calculateShift( pattern.toCharArray() );

		int index = doSearchIndexOf( str.substring( 0, pattern.length() ).toCharArray(), pattern.toCharArray(),
				shiftMapFromLast );

		return index < str.length() ? index : -1;
	}

	/**
	 * Calculates shifts from the right of the specified char array.
	 * 
	 * @param chars
	 *            Char array as pattern.
	 * @return Map having each char as the keys and number of shifts as the values.
	 */
	private static Map<Character, Integer> calculateShift(char[] chars) {

		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for ( int i = chars.length - 1; i >= 0; i-- ) {

			if ( !map.containsKey( Character.valueOf( chars[i] ) ) ) {
				map.put( Character.valueOf( chars[i] ), ( chars.length - 1 ) - i );
			}
		}

		return map;
	}

	/**
	 * Searches the specified pattern within string.
	 * 
	 * @param strChar
	 *            String as char array.
	 * @param patternChar
	 *            Pattern as char array.
	 * @param shiftMapFromLast
	 *            Map holding shifts for each character within the pattern.
	 * @return True if the pattern is found, false otherwise.
	 */
	private static boolean doSearch(char[] strChar, char[] patternChar, Map<Character, Integer> shiftMapFromLast) {

		int strIndex = patternChar.length - 1;
		int numOfShift = 0;

		while ( strIndex < strChar.length ) {

			numOfShift = findMisMatch( strChar, strIndex, patternChar, shiftMapFromLast );

			if ( numOfShift == 0 ) {
				return true;
			}

			strIndex += numOfShift;
		}

		return false;
	}

	/**
	 * Searches the specified pattern within string and returns the first index of the occurrence.
	 * 
	 * @param strChar
	 *            String as char array.
	 * @param patternChar
	 *            Pattern as char array.
	 * @param shiftMapFromLast
	 *            Map holding shifts for each character within the pattern.
	 * @return The first index of the occurrence if the pattern is found, -1 otherwise.
	 */
	private static int doSearchIndexOf(char[] strChar, char[] patternChar, Map<Character, Integer> shiftMapFromLast) {

		int strIndex = patternChar.length - 1;
		int numOfShift = 0;

		while ( strIndex < strChar.length ) {

			numOfShift = findMisMatch( strChar, strIndex, patternChar, shiftMapFromLast );

			if ( numOfShift == 0 ) {
				return strIndex;
			}

			strIndex += numOfShift;
		}

		return -1;
	}

	/**
	 * 
	 * @param strChar
	 *            String as char array.
	 * @param strIndex
	 *            Current index within the string.
	 * @param patternChar
	 *            Pattern as char array.
	 * @param shiftMapFromLast
	 *            Map holding shifts for each character within the pattern.
	 * @return Number of shifts to be added to the current index as the next index
	 */
	private static int findMisMatch(char[] strChar, int strIndex, char[] patternChar,
			Map<Character, Integer> shiftMapFromLast) {

		int patternIndex = patternChar.length - 1;

		while ( patternIndex >= 0 ) {

			// first character mismatch
			if ( ( patternIndex == patternChar.length - 1 ) && ( strChar[strIndex] != patternChar[patternIndex] ) ) {
				return getPreComputedShift( shiftMapFromLast, strChar[strIndex], patternChar.length - 1 );
			}

			if ( strChar[strIndex] != patternChar[patternIndex] ) {
				return Math.max( 1, ( patternChar.length - 1 ) - patternIndex );
			}

			strIndex--;
			patternIndex--;
		}

		return 0;
	}

	/**
	 * 
	 * Gets the precomputed corresponding shift from the end of pattern.
	 * 
	 * @param shiftMapFromLast
	 *            Map holding the precomputed corresponding shift.
	 * @param targetChar
	 *            Used to search the shift as key.
	 * @param patternLength
	 *            Length of pattern.
	 * @return Corresponding shift.
	 */
	private static int getPreComputedShift(Map<Character, Integer> shiftMapFromLast, char targetChar, int patternLength) {

		if ( shiftMapFromLast.get( Character.valueOf( targetChar ) ) == null ) {

			return patternLength;
		}

		return shiftMapFromLast.get( Character.valueOf( targetChar ) );
	}
}
