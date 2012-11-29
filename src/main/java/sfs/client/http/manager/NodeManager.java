package sfs.client.http.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.client.http.SingleplexHTTPClient;
import sfs.client.http.manager.periodic.PeriodicLivenessTimer;
import sfs.client.http.manager.periodic.PeriodicTimer;
import sfs.client.http.shortconversation.LivenessConversation;
import sfs.entry.Entry;
import sfs.entry.HostEntry;
import sfs.entry.StatusEntry;
import sfs.structure.Structure;
import sfs.structure.StructureNode;

public class NodeManager<T extends StructureNode> {

	private Structure<T> structure;
	private final Map<String, T> nodeMap;
	private final PeriodicTimer periodicTimer;
	private static Logger log = Logger.getLogger( NodeManager.class );

	public NodeManager() {
		nodeMap = new HashMap<String, T>();
		periodicTimer = new PeriodicLivenessTimer();
	}

	public NodeManager(Structure<T> structure) {
		this.structure = structure;
		nodeMap = new HashMap<String, T>();
		periodicTimer = new PeriodicLivenessTimer();
		addAndSchedulePeriodicTasks( this.structure.getRoot() );
	}

	public boolean setStructure(Structure<T> structure) {
		// TODO not fully implemented yet.
		boolean done = false;

		if ( this.structure == null ) {
			this.structure = structure;
			done = true;
		}
		else {
			done = changeTo( structure );
		}

		return done;
	}

	public boolean isStructureNull() {
		return structure == null ? true : false;
	}

	public boolean changeTo(Structure<T> structure) {

		throw new UnsupportedOperationException( "not implemented yet" );
	}

	public StatusEntry add(T t) {

		T node = null;
		StatusEntry status = null;

		log.debug( "about to add t: " + t + " " + t.getNode() );
		log.debug( "exists ? " + nodeMap.get( t.getNode().getOrigin() ) );
		if ( nodeMap.get( t.getNode().getOrigin() ) == null ) {
			if ( ( node = structure.add( t ) ) != null ) {

				log.debug( "parent is not null, " + node.getNode() );
				nodeMap.put( t.getNode().getOrigin(), t );

				if ( !isParentAlive( node ) ) {
					log.warn( "need to relocate the node to another parent since the parent looks dead." );
				}

				log.debug( "added as a child node, " + t + " parent: " + node.getNode() );
				status = new StatusEntry( createHostEntry( node ), createStatusEntry( "status", "OK", "message",
						"successfully added" ) );
			}
			else {
				log.debug( "parent is null for " + t.getNode() );
				if ( nodeMap.isEmpty() ) {
					nodeMap.put( t.getNode().getOrigin(), t );
					// t is added as the root.
					status = new StatusEntry( null, createStatusEntry( "status", "OK", "message",
							"successfully added as the root" ) );
					addAndSchedulePeriodicTasks(t);
				}
				else {

					status = new StatusEntry( null, createStatusEntry( "status", "FAILED", "message",
							"try to add to non-existing parent, parent is null, " + t.getNode().getOrigin()
									+ ", based on the origin" ) );
				}
			}
		}
		else {
			status = new StatusEntry( null, createStatusEntry( "status", "FAILED", "message",
					"try to add the same node multiple times, " + t.getNode().getOrigin() + ", based on the origin" ) );
		}

		return status;
	}

	public StatusEntry delete(T t) {

		T node = null;
		if ( nodeMap.get( t.getNode().getOrigin() ) != null ) {

			if ( ( node = structure.delete( t ) ) != null ) {
				nodeMap.remove( t.getNode().getOrigin() );

				return new StatusEntry( createHostEntry( node ), createStatusEntry( "status", "OK", "message",
						"successfully deleted" ) );
			}
		}

		return new StatusEntry( null, createStatusEntry( "status", "FAILED", "message",
				"try to delete non-exisitng node, " + t.getNode().getOrigin() + ", based on the origin" ) );
	}

	private Entry[] createStatusEntry(String... status) {

		if ( ( status.length % 2 ) != 0 ) {
			log.warn( "status has odd number. some of them miss key or value" );
			return null;
		}

		Entry[] entries = new Entry[status.length >> 1];
		int entryIndex = 0, statusIndex = 0;
		while ( ( entryIndex < entries.length ) && ( statusIndex < status.length ) ) {

			if ( ( statusIndex % 2 ) == 0 ) {
				if ( entries[entryIndex] == null ) {
					entries[entryIndex] = new Entry();
				}

				entries[entryIndex].setKey( status[statusIndex] );
				statusIndex++;
			}
			else {
				entries[entryIndex++].setValue( status[statusIndex++] );
			}
		}

		return entries;
	}

	public int size() {
		return structure.getSize();
	}

	public void add(String key, String value) {
		throw new UnsupportedOperationException( "not implemented yet" );
	}

	private HostEntry[] createHostEntry(T t) {

		return new HostEntry[] { new HostEntry( t.getNode().getOrigin(), t.getNode().getInternal() ) };
	}

	/**
	 * Checks if the parent node after the specified parameter t has been added to the structure is alive or not.
	 * 
	 * @param t
	 *            Node which has just added to the structure.
	 * @return True if the parent node is connectable over the wire, false otherwise.
	 */
	private boolean isParentAlive(T t) {

		boolean alive = false;

		SingleplexHTTPClient client = new SingleplexHTTPClient( createHostEntry( t )[0], new LivenessConversation() );
		try {
			client.initiate();
			alive = true;
		}
		catch ( IOException ex ) {
			log.error( ex );
			alive = false;
		}

		return alive;
	}

	/**
	 * Adds a periodic task and schedule the task.
	 * 
	 * @param t
	 *            Used to create the other side of the connection. It can be the root of the node structure.
	 */
	private void addAndSchedulePeriodicTasks(T t) {

		log.info( "about to start livenss check for the root: " + t );
		periodicTimer.addPeriodicTask( createHostEntry( t )[0] );
		periodicTimer.schedulePeriodicTasks( 1000, 3000 );
	}

	/**
	 * Wrapper method for periodicTimer.clearAllPeriodicTasks() for convenience.
	 */
	public void clearPeriodicTasks() {
		periodicTimer.clearAllPeriodicTasks();
	}
}
