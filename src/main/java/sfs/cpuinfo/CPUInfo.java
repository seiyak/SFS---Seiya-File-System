package sfs.cpuinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sfs.cpuinfo.pattern.CPUInfoPattern;
import sfs.cpuinfo.writer.json.CPUInfoJsonWriter;
import sfs.writer.Writer;

public class CPUInfo {

	public static final String CAT_COMMAND = "cat";
	public static final String CPUINFO_ARGUMENT = "/proc/cpuinfo";
	private final ProcessBuilder processBuilder;
	private final CPUInfoPattern cpuInfoPattern;
	private Writer writer;

	public CPUInfo() {

		processBuilder = new ProcessBuilder( CAT_COMMAND, CPUINFO_ARGUMENT );
		cpuInfoPattern = new CPUInfoPattern();
	}

	public CPUInfo(Writer writer) {

		this.writer = writer;
		processBuilder = new ProcessBuilder( CAT_COMMAND, CPUINFO_ARGUMENT );
		cpuInfoPattern = new CPUInfoPattern();
	}

	/**
	 * Gets information about the CPU(s) on the machine. If CPUInfo.writer is not set before calling the method,
	 * it's set to CPUInfoJsonWriter object by default within the method.
	 * 
	 * @return Information about CPU(s).
	 * @throws IOException
	 */
	public String getCPUInfo() throws IOException {

		if ( writer == null ) {
			writer = new CPUInfoJsonWriter();
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader( new InputStreamReader( processBuilder.start().getInputStream() ) );
			String str = "";
			while ( ( str = reader.readLine() ) != null ) {
				writer.writeFrom( cpuInfoPattern.check( str ) );
			}
		}
		catch ( IOException ex ) {
			throw ex;
		}
		finally {
			reader.close();
		}

		return writer.get();
	}
}
