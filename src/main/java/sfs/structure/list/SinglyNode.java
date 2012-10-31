package sfs.structure.list;

import sfs.structure.Node;
import sfs.structure.StructureNode;

public class SinglyNode extends StructureNode {

	private SinglyNode next;

	public SinglyNode(Node node) {
		super( node );
	}

	public void setNext(SinglyNode next) {
		this.next = next;
	}

	public SinglyNode getNext() {
		return next;
	}
}
