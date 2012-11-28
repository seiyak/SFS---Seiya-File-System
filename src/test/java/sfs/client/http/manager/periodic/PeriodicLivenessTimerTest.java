package sfs.client.http.manager.periodic;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import sfs.client.http.SingleplexHTTPClient;
import sfs.client.http.shortconversation.LivenessConversation;
import sfs.entry.HostEntry;
import sfs.util.ipaddress.LocalIPAddress;

public class PeriodicLivenessTimerTest {

	private PeriodicLivenessTimer periodicTimer;
	private static Logger log = Logger.getLogger( PeriodicLivenessTimerTest.class );

	@Before
	public void setUp() throws Exception {
		periodicTimer = new PeriodicLivenessTimer();
	}

	@Test
	public void testAddPeriodicTask() {

		boolean res = periodicTimer.addPeriodicTask( new HostEntry( "localhost", 60625 ) );
		assertTrue( "expecting res=='true' but found res==" + res, res );
		assertTrue(
				"expecting periodicTimer.getSizeOfPeriodicTasks()==1 but found "
						+ periodicTimer.getSizeOfPeriodicTasks(), periodicTimer.getSizeOfPeriodicTasks() == 1 );
		res = periodicTimer.removePeriodicTask();
		assertTrue( "expecting res=='true' but found res==" + res, res );
		assertTrue(
				"expecting periodicTimer.getSizeOfPeriodicTasks()==0 but found "
						+ periodicTimer.getSizeOfPeriodicTasks(), periodicTimer.getSizeOfPeriodicTasks() == 0 );
		assertTrue(
				"expecting periodcTimer.isPeriodicTasksEmpty()==true but found " + periodicTimer.isPeriodicTasksEmpty(),
				periodicTimer.isPeriodicTasksEmpty() );

		SingleplexHTTPClient task = new SingleplexHTTPClient( new HostEntry( "localhost", 60625 ),
				new LivenessConversation() );
		SingleplexHTTPClient task2 = new SingleplexHTTPClient( new HostEntry( "localhost", 60626 ),
				new LivenessConversation() );
		res = periodicTimer.addPeriodicTask( task );
		assertTrue( "expecting res=='true' but found res==" + res, res );
		res = periodicTimer.addPeriodicTask( task2 );
		assertTrue( "expecting res=='true' but found res==" + res, res );
		assertTrue(
				"expecting periodicTimer.getSizeOfPeriodicTasks()==2 but found "
						+ periodicTimer.getSizeOfPeriodicTasks(), periodicTimer.getSizeOfPeriodicTasks() == 2 );
		periodicTimer.clearAllPeriodicTasks();
		assertTrue(
				"expecting periodicTimer.getSizeOfPeriodicTasks()==0 but found "
						+ periodicTimer.getSizeOfPeriodicTasks(), periodicTimer.getSizeOfPeriodicTasks() == 0 );
		assertTrue(
				"expecting periodcTimer.isPeriodicTasksEmpty()==true but found " + periodicTimer.isPeriodicTasksEmpty(),
				periodicTimer.isPeriodicTasksEmpty() );
	}

	@Test
	public void testSchedulePeriodicTasks() {

		new Thread( new Runnable() {

			public void run() {
				DummyHTTPClient asServer = new DummyHTTPClient( new HostEntry( LocalIPAddress
						.getLocalIPAddress().get( "v4" ), 60625 ) );

				try {
					asServer.initiate();

					try {
						Thread.sleep( 5000 );
					}
					catch ( InterruptedException e ) {
						log.debug( "about to close the server due to the time limit for the test" );
						throw new IOException( "Thread.sleep() expires" );
					}
				}
				catch ( IOException ex ) {
					log.error( ex );
					try {
						asServer.close();
					}
					catch ( IOException e ) {
						log.error( e );
					}
				}
			}

		} ).start();

		periodicTimer.addPeriodicTask( new HostEntry( LocalIPAddress.getLocalIPAddress().get( "v4" ), 60626 ) );
		periodicTimer.schedulePeriodicTasks( 1000, 1000, 3 );
		try {
			Thread.sleep( 6000 );
		}
		catch ( InterruptedException e ) {
			log.info( "about to finish the test" );
		}
	}
}
