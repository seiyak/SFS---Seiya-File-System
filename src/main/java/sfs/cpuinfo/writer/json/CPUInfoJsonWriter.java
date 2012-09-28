package sfs.cpuinfo.writer.json;

import java.util.HashMap;

import org.json.JSONArray;

import sfs.entry.Entry;
import sfs.writer.json.JsonWriter;

public class CPUInfoJsonWriter extends JsonWriter {

	public CPUInfoJsonWriter() {
		jsonArray = new JSONArray();
		jsonMap = new HashMap<String, String>();
	}

	@Override
	public void writeFrom(Entry entry) {

		if ( entry == null ) {
			jsonArray.put( jsonMap );
			jsonMap = new HashMap<String, String>();
		}
		else {
			jsonMap.put( entry.getKey(), entry.getValue() );
		}
	}

	@Override
	public String get() {
		return jsonArray.toString();
	}
}
