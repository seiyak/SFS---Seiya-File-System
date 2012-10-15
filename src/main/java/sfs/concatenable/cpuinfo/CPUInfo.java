package sfs.concatenable.cpuinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import sfs.concatenable.Concatenable;
import sfs.cpuinfo.pattern.CPUInfoPattern;
import sfs.cpuinfo.writer.json.CPUInfoJsonWriter;
import sfs.writer.Writer;

public class CPUInfo extends Concatenable{

	public static final String CAT_COMMAND = "cat";
	public static final String CPUINFO_ARGUMENT = "/proc/cpuinfo";
	private final ProcessBuilder processBuilder;
	private final CPUInfoPattern cpuInfoPattern;
	private final CPUInfoJsonWriter jsonWriter;
	private static Logger log = Logger.getLogger( CPUInfo.class );

	public CPUInfo() {

		processBuilder = new ProcessBuilder( CAT_COMMAND, CPUINFO_ARGUMENT );
		cpuInfoPattern = new CPUInfoPattern();
		jsonWriter = new CPUInfoJsonWriter();
	}

	/**
	 * Gets information about the CPU(s) on the machine. If CPUInfo.writer is not set before calling the method,
	 * it's set to CPUInfoJsonWriter object by default within the method.
	 * 
	 * @return Information about CPU(s).
	 * @throws IOException
	 */
	public String getCPUInfo() throws IOException {

		doGetCPUInfo();

		return jsonWriter.get();
	}

	/**
	 * Gets number of processors on the machine.
	 * 
	 * @return Number of processors.
	 * @throws IOException
	 */
	public int getProcessors() throws IOException {

		doGetCPUInfo();

		return jsonWriter.getProcessors();
	}

	protected final void doGetCPUInfo() throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader( new InputStreamReader( processBuilder.start().getInputStream() ) );
			String str = "";
			while ( ( str = reader.readLine() ) != null ) {
				jsonWriter.writeFrom( cpuInfoPattern.check( str ) );
			}
		}
		catch ( IOException ex ) {
			throw ex;
		}
		finally {
			reader.close();
		}
	}

	@Override
	protected void putJson(JSONObject json) {

		Entry<String, Object> entry = jsonWriter.getEntry();
		try {
			doGetCPUInfo();
			json.put( entry.getKey(), entry.getValue() );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}
		catch ( IOException ex ) {
			log.error( ex );
		}
	}
}
