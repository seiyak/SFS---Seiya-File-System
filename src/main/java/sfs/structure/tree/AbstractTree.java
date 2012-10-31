package sfs.structure.tree;

public abstract class AbstractTree<T> implements TreeStructure<T> {

	private final T root;

	protected AbstractTree(T root) {
		this.root = root;
	}

	protected T getRoot() {
		return root;
	}
}
