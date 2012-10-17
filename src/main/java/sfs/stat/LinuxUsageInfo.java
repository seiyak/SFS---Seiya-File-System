package sfs.stat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public abstract class LinuxUsageInfo {

	protected final ProcessBuilder processBuilder;
	protected final String CAT_COMMAND = "cat";
	private static Logger log = Logger.getLogger( LinuxUsageInfo.class );

	protected LinuxUsageInfo(String argument) {

		processBuilder = new ProcessBuilder( CAT_COMMAND, argument );
	}

	/**
	 * Gets BufferedReader object for the input stream.
	 * 
	 * @return BufferedReader holding the input stream for the command.
	 */
	protected final BufferedReader getBufferedReader() {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader( new InputStreamReader( processBuilder.start().getInputStream() ) );
		}
		catch ( IOException ex ) {
			log.error( ex );
		}

		return reader;
	}
}
