package sfs.client.http.manager;

import sfs.entry.NullStatusEntry;
import sfs.entry.StatusEntryable;
import sfs.structure.StructureNode;

public class NullNodeManager<T extends StructureNode> implements NodeManagerable<T> {

	public StatusEntryable add(T t) {
		return new NullStatusEntry();
	}

	public StatusEntryable delete(T t) {
		return new NullStatusEntry();
	}

	public StatusEntryable rotate(T t) {
		return new NullStatusEntry();
	}

}
