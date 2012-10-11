package sfs.writer.json;

import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import sfs.writer.Writer;

public abstract class JsonWriter implements Writer {

	protected JSONArray jsonArray;
	protected Map<String, String> jsonMap;
	private static Logger log = Logger.getLogger( JsonWriter.class );

	protected JsonWriter(){

	}

	public abstract Map<String, Object> getJsonAsMap();

	public final Map.Entry<String, Object> getEntry() {

		return getJsonAsMap().entrySet().iterator().next();
	}
}
