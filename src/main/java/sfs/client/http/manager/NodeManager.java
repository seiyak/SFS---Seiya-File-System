package sfs.client.http.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.entry.Entry;
import sfs.entry.HostEntry;
import sfs.entry.StatusEntry;
import sfs.structure.Node;
import sfs.structure.Structure;
import sfs.structure.StructureNode;
import sfs.util.date.DateUtil;

public class NodeManager<T extends StructureNode> {

	private Structure<T> structure;
	private final HostEntry hostEntry;
	private final Map<String, T> nodeMap;
	private static Logger log = Logger.getLogger( NodeManager.class );

	public NodeManager(HostEntry hostEntry) {
		this.hostEntry = hostEntry;
		nodeMap = new HashMap<String,T>();
	}

	public NodeManager(HostEntry hostEntry, Structure<T> structure) {
		this.hostEntry = hostEntry;
		this.structure = structure;
		nodeMap = new HashMap<String,T>();
	}

	public HostEntry gethostEntry(){
		return hostEntry;
	}
	
	public StatusEntry[] setStructure(Structure<T> structure) {

		StatusEntry[] entry = null;

		if ( this.structure == null ) {
			this.structure = structure;
			entry = new StatusEntry[] { new StatusEntry( new HostEntry[] { hostEntry }, createStatusEntry( "status",
					"OK", "message", "welcome to SFS as the root !" ) ) };
		}
		else {
			changeTo( structure );
		}

		return entry;
	}

	public boolean isStructureNull() {
		return structure == null ? true : false;
	}

	public boolean changeTo(Structure<T> structure) {

		throw new UnsupportedOperationException( "not implemented yet" );
	}

	public StatusEntry[] add(T t) {

		T node = null;
		if ( nodeMap.get( t.getNode().getOrigin() ) == null ) {
			if ( ( node = structure.add( t ) ) != null ) {
				nodeMap.put( t.getNode().getOrigin(), t );

				return new StatusEntry[] { new StatusEntry( createHostEntry( node ), createStatusEntry( "status", "OK",
						"message", "successfully added" ) ) };
			}
		}

		return new StatusEntry[] { new StatusEntry( null, createStatusEntry( "status", "FAILED", "message",
				"try to add to non-existing parent, parent is null, " + t.getNode().getOrigin()
						+ ", based on the origin" ) ) };
	}

	public StatusEntry[] delete(T t) {

		T node = null;
		if ( nodeMap.get( t.getNode().getOrigin() ) != null ) {

			if ( ( node = structure.delete( t ) ) != null ) {
				nodeMap.remove( t.getNode().getOrigin() );

				return new StatusEntry[] { new StatusEntry( createHostEntry( node ), createStatusEntry( "status", "OK",
						"message", "successfully deleted" ) ) };
			}
		}

		return new StatusEntry[] { new StatusEntry( null, createStatusEntry( "status", "FAILED", "message",
				"try to delete non-exisitng node, " + t.getNode().getOrigin() + ", based on the origin" ) ) };
	}

	private Entry[] createStatusEntry(String ... status){
		
		if((status.length % 2) != 0){
			log.warn( "status has odd number. some of them miss key or value" );
			return null;
		}
		
		Entry[] entries = new Entry[status.length >> 1];
		int entryIndex = 0, statusIndex = 0;
		while((entryIndex < entries.length) && (statusIndex < status.length)){
			
			if((statusIndex % 2) == 0){
				if(entries[entryIndex] == null){
					entries[entryIndex] = new Entry();
				}
				
				entries[entryIndex].setKey( status[statusIndex] );
				statusIndex++;
			}else{
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

	/**
	 * Creates the root node Node object from the hostEntry property.
	 * The created object will be the root of the node data structure in such as Star.
	 * 
	 * @return Node object created from the hostEntry property.
	 */
	public Node createRootNode() {
		return new Node( DateUtil.getTimeInGMT(), hostEntry.getHost(), hostEntry.getPort(), hostEntry.getPort() );
	}

	private HostEntry[] createHostEntry(T t) {

		return new HostEntry[] { new HostEntry( t.getNode().getOrigin(), t.getNode().getPort() ) };
	}
}
