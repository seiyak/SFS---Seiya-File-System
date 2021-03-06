package sfs.entry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Entry {

	private String key;
	private String value;

	public Entry() {

	}

	public Entry(String key, String value) {

		this.key = key;
		this.value = value;
	}

	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Map<String, String> getAsMap() {

		Map<String, String> map = new HashMap<String, String>();
		map.put( key, value );

		return Collections.unmodifiableMap( map );
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
