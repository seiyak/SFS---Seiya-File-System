package sfs.usage.memory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.stat.LinuxUsageInfo;
import sfs.stat.memory.MemoryStat;

public class LinuxMemory extends LinuxUsageInfo implements Memory {

	private final MemoryStat memory;
	private static final String PROC_MEMINFO_ARGUMENT = "/proc/meminfo";
	private static Logger log = Logger.getLogger( LinuxMemory.class );

	public LinuxMemory() {
		super( PROC_MEMINFO_ARGUMENT );
		memory = new MemoryStat();
		doGetMemoryUsage();
	}

	/**
	 * Gets current memory usage calling 'cat /proc/meminfo'.
	 */
	public MemoryStat getMemoryUsage() {
		doGetMemoryUsage();

		return new MemoryStat( memory.getTotalMemory(), memory.getCurrentMemory() );
	}

	private void doGetMemoryUsage() {

		BufferedReader reader = getBufferedReader();
		String str = "";
		int count = 0;
		String[] each = null;
		try {
			while ( ( str = reader.readLine() ) != null ) {
				each = str.split( " " );
				if ( count == 0 ) {
					memory.setTotalMemory( Double.parseDouble( each[each.length - 2] ) );
				}
				else if ( count == 1 ) {
					memory.setCurrentMemory( memory.getTotalMemory() - Double.parseDouble( each[each.length - 2] ) );
					break;
				}
				count++;
			}
		}
		catch ( IOException ex ) {
			log.error( ex );
		}
		finally {
			try {
				reader.close();
			}
			catch ( IOException e ) {
				log.error( e );
			}
		}
	}

	/**
	 * Gets memory usage as Map.
	 */
	public Map<String, String> getMemoryUsageAsMap() {

		doGetMemoryUsage();
		Map<String, String> map = new HashMap<String, String>();
		map.put( "total", String.valueOf( memory.getTotalMemory() ) + " " + MemoryStat.UNIT );
		map.put( "current", String.valueOf( memory.getCurrentMemory() ) + " " + MemoryStat.UNIT );

		return map;
	}
}
