package sfs.usage.memory;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import sfs.stat.memory.MemoryStat;

public class LinuxMemoryTest {

	private LinuxMemory linuxMemory;
	private static Logger log = Logger.getLogger( LinuxMemoryTest.class );

	@Before
	public void setUp() throws Exception {

		linuxMemory = new LinuxMemory();
	}

	@Test
	public void test() {
		MemoryStat stat = linuxMemory.getMemoryUsage();

		int count = 0;
		while ( count < 10 ) {
			assertNotNull( "epxecting stat != null but found null", stat );
			log.debug( stat );

			try {
				Thread.sleep( 1000 );
			}
			catch ( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			stat = linuxMemory.getMemoryUsage();
			count++;
		}
	}
}
