package sfs.usage.cpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;

import sfs.concatenable.cpuinfo.CPUInfo;
import sfs.concatenable.usage.LinuxUsageInfo;
import sfs.header.http.separator.Space;
import sfs.stat.cpu.CPUStat;

public class LinuxCPU extends LinuxUsageInfo implements CPU {

	private CPUStat[] usages;
	private final DecimalFormat decimalFormat;
	private static final String PROC_STAT_ARGUMENT = "/proc/stat";
	private static Logger log = Logger.getLogger( LinuxCPU.class );

	protected LinuxCPU() {
		super( PROC_STAT_ARGUMENT );
		decimalFormat = new java.text.DecimalFormat( "#.#" );
		initializeUsages();
	}

	/**
	 * Initializes internal state of this object.
	 */
	private void initializeUsages() {

		try {
			usages = new CPUStat[new CPUInfo().getProcessors()];
			log.debug( "number of usage slost: " + usages.length );
			getInitialUsages();
		}
		catch ( IOException ex ) {
			log.error( ex );
		}
	}

	/**
	 * Gets the CPU usage when this object is instantiated as the initial usage.
	 * 
	 * @throws IOException
	 */
	private void getInitialUsages() throws IOException {

		BufferedReader reader = getBufferedReader();
		String str = "";
		int count = 0;

		try {
			while ( ( str = reader.readLine() ) != null ) {
				if ( str.startsWith( "intr" ) ) {
					break;
				}

				if ( count > 0 ) {
					extractAndInitializeUsage( usages, str, count );
				}

				count++;
			}
		}
		catch ( IOException ex ) {
			log.error( ex );
		}
		finally {
			reader.close();
		}
	}

	/**
	 * Extracts and puts CPU usage as the initial usage.
	 * 
	 * @param usages
	 *            CPUStat array stores the initial CPU usage.
	 * @param str
	 *            Corresponding CPU usage string from 'cat /proc/stat'.
	 * @param count
	 *            Number of lines starting with 'cpuXXX' from 'cat /proc/stat'.
	 */
	private void extractAndInitializeUsage(CPUStat[] usages, String str, int count) {

		// Skips the first line since the line is aggregation of all the CPU usages.
		String[] each = splitBySpace( str );
		usages[count - 1] = new CPUStat( Double.parseDouble( each[1] ), Double.parseDouble( each[2] ),
				Double.parseDouble( each[3] ), Double.parseDouble( each[4] ) );
	}

	/**
	 * Gets CPU usage as CPUStat array.
	 */
	public CPUStat[] getCPUUsage() {
		BufferedReader reader = getBufferedReader();
		String str = "";
		int count = 0;

		try {
			while ( ( str = reader.readLine() ) != null ) {
				if ( str.startsWith( "intr" ) ) {
					break;
				}

				if ( count > 0 ) {
					extractAndGetUsage( usages, str, count );
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
			catch ( IOException ex ) {
				log.error( ex );
			}
		}

		return copyOf( usages );
	}

	/**
	 * Extracts and calculates CPU usage.
	 * 
	 * @param usages
	 *            CPUStat array stores the initial CPU usage.
	 * @param str
	 *            Corresponding CPU usage string from 'cat /proc/stat'.
	 * @param count
	 *            Number of lines starting with 'cpuXXX' from 'cat /proc/stat'.
	 */
	private void extractAndGetUsage(CPUStat[] usages, String str, int count) {

		String[] each = splitBySpace( str );
		double total = updateUserUsage( each[1], usages, count ) + updateNiceUsage( each[2], usages, count )
				+ updateSystemUsage( each[3], usages, count );
		double percent = total;
		total += updateIdleUsage( each[4], usages, count );
		percent /= total;
		percent *= 100;
		usages[count - 1].setUsage( percent );
	}

	/**
	 * Copies and returns CPUStat array.
	 * 
	 * @param usages
	 *            Array to be copied.
	 * @return Copy of CPUStat array.
	 */
	private CPUStat[] copyOf(CPUStat[] usages) {

		CPUStat[] stats = new CPUStat[usages.length];
		System.arraycopy( usages, 0, stats, 0, stats.length );
		return stats;
	}

	/**
	 * Splits String by a whitespace, ' '.
	 * 
	 * @param str
	 *            String to be split by a whitespace.
	 * @return Array holding each String split by a whitespace.
	 */
	private String[] splitBySpace(String str) {
		return str.split( new Space().getSeparator() );
	}

	private double updateUserUsage(String userUsage, CPUStat[] usages, int count) {

		double diff = Double.parseDouble( userUsage ) - usages[count - 1].getPreviousUser();
		usages[count - 1].setPreviousUser( Double.parseDouble( userUsage ) );
		return diff;
	}

	private double updateNiceUsage(String niceUsage, CPUStat[] usages, int count) {

		double diff = Double.parseDouble( niceUsage ) - usages[count - 1].getPreviousNice();
		usages[count - 1].setPreviousNice( Double.parseDouble( niceUsage ) );
		return diff;
	}

	private double updateSystemUsage(String systemUsage, CPUStat[] usages, int count) {

		double diff = Double.parseDouble( systemUsage ) - usages[count - 1].getPreviousSystem();
		usages[count - 1].setPreviousSystem( Double.parseDouble( systemUsage ) );
		return diff;
	}

	private double updateIdleUsage(String idleUsage, CPUStat[] usages, int count) {

		double diff = Double.parseDouble( idleUsage ) - usages[count - 1].getPreviousIdle();
		usages[count - 1].setPreviousIdle( Double.parseDouble( idleUsage ) );
		return diff;
	}

	/**
	 * Gets formatted CPU usage with '#.#' format which is '100.0' or '9.3' and so on.
	 * 
	 * @return CPUStat array holding the formatted by the format.
	 */
	public CPUStat[] getFormattedCPUUsage() {
		CPUStat[] stats = getCPUUsage();

		for ( int i = 0; i < stats.length; i++ ) {
			try {
				stats[i].setUsage( Double.parseDouble( decimalFormat.format( stats[i].getUsage() ) ) );
			}
			catch ( NumberFormatException ex ) {
				stats[i].setUsage( Double.NaN );
			}
		}

		return stats;
	}
}
