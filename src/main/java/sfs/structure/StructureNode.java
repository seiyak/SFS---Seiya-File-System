package sfs.structure;

import java.util.Random;

public abstract class StructureNode {

	private final int id;
	private final Node node;
	private final Random random;

	protected StructureNode(Node node) {
		this.node = node;
		this.random = new Random();
		id = generateId();
	}

	private int generateId() {

		return random.nextInt( Integer.MAX_VALUE ) + 1;
	}

	public int getId() {
		return id;
	}

	public Node getNode() {
		return node;
	}
}
