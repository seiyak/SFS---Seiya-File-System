package sfs.structure.tree;

public abstract class AbstractTree<T> implements TreeStructure<T> {

	private T root;

	protected AbstractTree() {

	}

	protected AbstractTree(T root) {
		this.root = root;
	}

	public void setRoot(T root) {
		this.root = root;
	}

	protected T getRoot() {
		return root;
	}
}
