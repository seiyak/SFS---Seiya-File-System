package sfs.usage.cpu;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import sfs.stat.cpu.CPUStat;

public class LinuxCPUTest {

	private LinuxCPU linuxCpu;
	private static Logger log = Logger.getLogger( LinuxCPUTest.class );

	@Before
	public void setUp() throws Exception {
		linuxCpu = new LinuxCPU();
	}

	@Test
	public void testGetCPUUsage() {

		CPUStat[] stats = linuxCpu.getCPUUsage();

		int count = 0;
		while ( count < 10 ) {
			assertNotNull( "expecting stats != null but found null", stats );
			stats = linuxCpu.getFormattedCPUUsage();
			try {
				Thread.sleep( 1000 );
			}
			catch ( InterruptedException e ) {

			}

			count++;
		}
	}
}
