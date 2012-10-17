package sfs.concatenable.usage.cpu;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import sfs.concatenable.Concatenable;
import sfs.usage.cpu.CPU;
import sfs.usage.cpu.LinuxCPU;

public class CPUUsage extends Concatenable {

	private CPU cpu;
	private static Logger log = Logger.getLogger( CPUUsage.class );

	public CPUUsage() {

	}

	public CPUUsage(CPU cpu) {
		this.cpu = cpu;
	}

	@Override
	public void putJson(JSONObject json) {

		if ( cpu == null ) {
			// TODO detect CPU class based on the OS.
			cpu = new LinuxCPU();
		}

		try {
			json.put( "cpuUsage", cpu.getCPUUsageAsMap() );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}
	}

}
