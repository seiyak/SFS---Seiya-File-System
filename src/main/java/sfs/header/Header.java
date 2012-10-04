package sfs.header;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.header.http.HeaderEntry;

public abstract class Header {

	private final Map<HeaderEntry, Object> header;
	private static Logger log = Logger.getLogger( Header.class );

	protected Header() {

		header = new LinkedHashMap<HeaderEntry, Object>();
	}

	/**
	 * Puts a header entry represented by key and value parameters.
	 * 
	 * @param key
	 *            Name for the header entry.
	 * @param value
	 *            Value for the header entry.
	 * @return Object if the key was associated with another Object, null otherwise.
	 */
	public final Object put(HeaderEntry key, Object value) {

		return header.put( key, value );
	}

	/**
	 * Removes a header entry represented by the key parameter.
	 * 
	 * @param key
	 *            Name for the header entry.
	 * @return Object if the key was associated with another Object, null otherwise.
	 */
	public final Object remove(HeaderEntry key) {

		return header.remove( key );
	}

	/**
	 * Clears all the header entries that have been put.
	 */
	public final void clear() {

		header.clear();
	}

	/**
	 * Gets header property.
	 * 
	 * @return
	 */
	protected Map<HeaderEntry, Object> getHeader() {

		return Collections.unmodifiableMap( header );
	}

	public final String format() {

		String headerEntries = doFormat();
		logHeaderEntries( headerEntries );

		return headerEntries;
	}

	/**
	 * Writes out all the formatted header entries.
	 * 
	 * @param headerEntries
	 *            All the header entries as String.
	 */
	private void logHeaderEntries(String headerEntries) {

		log.info( "header looks like this:\n" + headerEntries );
	}

	protected abstract String doFormat();
}
