package sfs.client.http.manager;

import sfs.entry.StatusEntryable;
import sfs.structure.StructureNode;

public interface NodeManagerable<T extends StructureNode> {

	public StatusEntryable add(T t);

	public StatusEntryable delete(T t);

	public StatusEntryable rotate(T t);
}
