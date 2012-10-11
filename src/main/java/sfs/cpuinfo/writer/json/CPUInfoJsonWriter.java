package sfs.cpuinfo.writer.json;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import sfs.entry.Entry;
import sfs.writer.json.JsonWriter;

public class CPUInfoJsonWriter extends JsonWriter {

	public CPUInfoJsonWriter() {
		jsonArray = new JSONArray();
		jsonMap = new HashMap<String, String>();
	}

	public void writeFrom(Entry entry) {

		if ( entry == null ) {
			jsonArray.put( jsonMap );
			jsonMap = new HashMap<String, String>();
		}
		else {
			jsonMap.put( entry.getKey(), entry.getValue() );
		}
	}

	public String get() {
		
		return new JSONObject(getJsonAsMap()).toString();
	}

	@Override
	public Map<String, Object> getJsonAsMap() {

		Map<String, Object> cpuInfoJson = new HashMap<String, Object>();
		cpuInfoJson.put( "cpuInfo", jsonArray );

		return cpuInfoJson;
	}
}
