package sfs.structure.tree;

import sfs.structure.Node;
import sfs.structure.StructureNode;

public class BinaryNode extends StructureNode {

	private BinaryNode left;
	private BinaryNode right;

	public BinaryNode(Node node) {
		super( node );
	}

	public BinaryNode getLeft() {
		return left;
	}

	public void setLeft(BinaryNode left) {
		this.left = left;
	}

	public BinaryNode getRight() {
		return right;
	}

	public void setRight(BinaryNode right) {
		this.right = right;
	}
}
