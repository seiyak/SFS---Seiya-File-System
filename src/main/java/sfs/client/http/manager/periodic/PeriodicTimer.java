package sfs.client.http.manager.periodic;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import sfs.client.http.SingleplexHTTPClient;
import sfs.entry.HostEntry;

public abstract class PeriodicTimer extends TimerTask {

	private List<SingleplexHTTPClient> periodicTasks;
	private final Timer periodicTimer;
	private Integer count = 0;
	private static Logger log = Logger.getLogger( PeriodicTimer.class );

	protected PeriodicTimer() {
		periodicTimer = new Timer( "periodicTimer" );
	}

	public abstract boolean addPeriodicTask(HostEntry hostEntry);

	public abstract boolean removePeriodicTask();

	/**
	 * Adds the specified SingleplexHTTPClient object as a periodic task.
	 * 
	 * @param periodicTask
	 *            Added as a periodic task.
	 * @return True if the specified object successfully added, false otherwise.
	 */
	public final boolean addPeriodicTask(SingleplexHTTPClient periodicTask) {

		if ( periodicTasks == null ) {
			periodicTasks = new LinkedList<SingleplexHTTPClient>();
		}

		return periodicTasks.add( periodicTask );
	}

	/**
	 * Removes the specified SingleplexHTTPClient object.
	 * 
	 * @param periodicTask
	 *            To be removed.
	 * @return True if the specified object successfully removed, false otherwise.
	 */
	public final boolean removePeriodicTask(SingleplexHTTPClient periodicTask) {

		return periodicTasks.remove( periodicTask );
	}

	/**
	 * Clears all the periodic tasks.
	 */
	public final void clearAllPeriodicTasks() {
		periodicTimer.cancel();
		periodicTasks.clear();
	}

	/**
	 * Gets the size of periodic tasks.
	 * 
	 * @return Size of periodic tasks.
	 */
	public final int getSizeOfPeriodicTasks() {
		return periodicTasks.size();
	}

	/**
	 * Checks if periodic tasks is empty or not.
	 * 
	 * @return True if periodic tasks has no tasks, false otherwise.
	 */
	public final boolean isPeriodicTasksEmpty() {
		return periodicTasks.isEmpty();
	}

	/**
	 * Starts periodic tasks.
	 */
	private void startPeriodicTasks() {

		for ( SingleplexHTTPClient task : periodicTasks ) {
			try {
				task.initiate();
			}
			catch ( IllegalArgumentException ex ) {
				log.error( ex );
				closeTask( task );
			}
			catch ( IOException ex ) {
				log.error( ex );
				closeTask( task );
			}
		}
	}

	/**
	 * Closes the specified SingleplexHTTPClient object.
	 * 
	 * @param task
	 *            To be closed.
	 */
	private void closeTask(SingleplexHTTPClient task) {
		try {
			task.close();
		}
		catch ( IOException e ) {
			log.error( e );
		}
	}

	/**
	 * Schedules periodic tasks with the specified delay and period.
	 * 
	 * @param initialDelay
	 *            Initial delay.
	 * @param period
	 *            Period.
	 */
	public final void schedulePeriodicTasks(int initialDelay, int period) {
		periodicTimer.schedule( this, initialDelay, period );
	}

	/**
	 * Schedules periodic tasks with the specified delay and period. The scheduled periodic tasks are run at the most
	 * howMany times. Once reached the number, periodic tasks are cancelled.
	 * 
	 * @param initialDelay
	 *            Initial delay.
	 * @param period
	 *            Period.
	 * @param howMany
	 *            How many times to run the periodic tasks.
	 */
	public final void schedulePeriodicTasks(int initialDelay, int period, int howMany) {

		if ( howMany <= 0 ) {
			log.warn( "invalid value is specified for howMany parameter, " + howMany
					+ ". the value should be greater than 0." );
		}
		else {
			synchronized ( count ) {
				log.info( "about to set the time to run periodic tasks to " + howMany + " times and done." );
				count = howMany;
			}
		}

		schedulePeriodicTasks( initialDelay, period );
	}

	/**
	 * Cancels periodic tasks.
	 */
	public final void cancelPeriodicTasks() {
		periodicTimer.cancel();
	}

	@Override
	public void run() {

		startPeriodicTasks();

		synchronized ( count ) {
			if ( ( --count ) == 0 ) {
				log.debug( "about to cancel the periodic timer task because howMany reached to 0." );
				cancelPeriodicTasks();
			}
		}
	}
}
