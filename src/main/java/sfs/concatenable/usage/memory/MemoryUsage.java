package sfs.concatenable.usage.memory;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import sfs.concatenable.Concatenable;
import sfs.usage.memory.LinuxMemory;
import sfs.usage.memory.Memory;

public class MemoryUsage extends Concatenable {

	private Memory memory;
	private static Logger log = Logger.getLogger( MemoryUsage.class );

	public MemoryUsage() {

	}

	public MemoryUsage(Memory memory) {
		this.memory = memory;
	}

	@Override
	protected void putJson(JSONObject json) {

		if ( memory == null ) {
			// TODO detect Memory class based on the OS.
			memory = new LinuxMemory();
		}

		try {
			json.put( "memoryUsage", memory.getMemoryUsageAsMap() );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}
	}

}
