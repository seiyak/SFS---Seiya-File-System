package sfs.structure.tree;

import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import sfs.structure.Node;
import sfs.structure.StructureNode;
import sfs.util.search.SearchUtil;
import sfs.util.sort.SortUtil;

public class MultiNode extends StructureNode {

	private int currentIndex;
	private int maxOfChildren;
	private int numberOfChildren;
	private MultiNode[] children;
	private static Logger log = Logger.getLogger( MultiNode.class );
	private final int DEFAULT_MAX_NUMBER_OF_CHILDREN = 5;

	public MultiNode(Node node) {
		super( node );

		currentIndex = 0;
		numberOfChildren = 0;
		this.maxOfChildren = DEFAULT_MAX_NUMBER_OF_CHILDREN;
		children = new MultiNode[DEFAULT_MAX_NUMBER_OF_CHILDREN];
	}

	public MultiNode(int numberOfChildren, Node node) {
		super( node );

		currentIndex = 0;
		numberOfChildren = 0;
		this.maxOfChildren = numberOfChildren;
		children = new MultiNode[this.maxOfChildren];
	}

	/**
	 * Gets the current number of children.
	 * 
	 * @return
	 */
	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	/**
	 * Gets children as copied array.
	 * 
	 * @return Copy of children.
	 */
	public MultiNode[] getChildren() {
		return copyChildren( children.length );
	}

	/**
	 * Gets children as array.
	 * 
	 * @return children as array.
	 */
	protected final MultiNode[] getChildNodes() {
		return children;
	}

	/**
	 * Gets current index.
	 * 
	 * @return Current index.
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * Adds the specified object as a child.
	 * 
	 * @param child
	 *            Object to be added.
	 */
	public void addChild(MultiNode child) {

		if ( currentIndex > ( DEFAULT_MAX_NUMBER_OF_CHILDREN - 1 ) ) {
			addMoreChildren();
		}
		numberOfChildren++;
		children[currentIndex++] = child;
	}

	/**
	 * Adds a space for a new object.
	 */
	private void addMoreChildren() {

		MultiNode[] nodes = copyChildren( children.length + 1 );
		children = null;
		children = nodes;
	}

	/**
	 * Copies children array creating space with the specified length.
	 * 
	 * @param length
	 *            Used to allocate a new children array.
	 * @return Newly allocated children array.
	 */
	private MultiNode[] copyChildren(int length) {

		MultiNode[] nodes = new MultiNode[length];
		System.arraycopy( children, 0, nodes, 0, children.length );

		return nodes;
	}

	/**
	 * Deletes the specified object.
	 * 
	 * @param child
	 *            To be deleted.
	 * @return True if delete the object successfully, false otherwise.
	 */
	public boolean deleteChild(MultiNode child) {

		boolean done = false;

		if ( children.length == 1 ) {
			if ( children[0].getId() == child.getId() ) {

				log.debug( "about to remove children[0]" );

				children[0] = null;
				children = nullifyAndCreateArray( DEFAULT_MAX_NUMBER_OF_CHILDREN );
				numberOfChildren--;
				done = true;
			}
		}
		else {
			SortUtil.mergeSort( children );
			int index = -1;
			try {
				index = SearchUtil.binarySearchIndex( child, children );
				children[index] = null;
				shiftAfterDelete( index, children );
				numberOfChildren--;
				done = true;
			}
			catch ( NoSuchElementException ex ) {
				log.error( "specified node id, " + child.getId() + " not found" );
				done = false;
			}
		}

		return done;
	}

	/**
	 * Shifts the specified array.
	 * 
	 * @param index
	 *            Where the shift happens.
	 * @param children
	 *            Used to copy the elements before and after the index.
	 */
	private void shiftAfterDelete(int index, MultiNode[] children) {

		MultiNode[] nodes = createArray( children.length - 1 );
		System.arraycopy( children, 0, nodes, 0, index );
		System.arraycopy( children, index + 1, nodes, index, children.length - index - 1 );

		children = null;
		children = nodes;
	}

	/**
	 * Nullify the children property and creates
	 * 
	 * @param length
	 *            Used to allocate a new array.
	 * @return Newly allocated array.
	 */
	private MultiNode[] nullifyAndCreateArray(int length) {
		children = null;
		return createArray( length );
	}

	/**
	 * Creates an array with the specified length.
	 * 
	 * @param length
	 *            Used to allocate an array.
	 * @return Newly allocated array.
	 */
	private MultiNode[] createArray(int length) {

		if ( length <= 0 ) {
			return new MultiNode[DEFAULT_MAX_NUMBER_OF_CHILDREN];
		}

		return new MultiNode[length];
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof MultiNode ) {

			MultiNode mn = (MultiNode) obj;
			return getId() == mn.getId();
		}

		return false;
	}

	@Override
	public String toString() {
		return "{id=" + getId() + " numberOfChildren=" + numberOfChildren + " node=" + getNode() + "}";
	}
}
