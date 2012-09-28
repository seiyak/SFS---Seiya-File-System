package sfs.entry;

import java.util.HashMap;
import java.util.Map;

public class Entry {

	private final String key;
	private final String value;

	public Entry(String key, String value) {

		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public Map<String, String> getAsMap() {

		Map<String, String> map = new HashMap<String, String>();
		map.put( key, value );

		return map;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof Entry ) {

			Entry entry = (Entry) obj;
			return key.equals( entry.getKey() ) && value.equals( entry.getValue() );
		}

		return false;
	}

	@Override
	public String toString() {
		return "{key=" + key + ", value=" + value + "}";
	}
}
