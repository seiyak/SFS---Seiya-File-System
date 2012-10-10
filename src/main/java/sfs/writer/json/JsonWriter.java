package sfs.writer.json;

import java.util.Map;

import org.json.JSONArray;

import sfs.entry.Entry;
import sfs.writer.Writer;

public class JsonWriter implements Writer {

	protected JSONArray jsonArray;
	protected Map<String, String> jsonMap;
	protected Map<String,JSONArray> cpuInfoJson;

	public void writeFrom(Entry entry) {
		// TODO Auto-generated method stub
	}

	public String get() {
		// TODO Auto-generated method stub
		return null;
	}

}
