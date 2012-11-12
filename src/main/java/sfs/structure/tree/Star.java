package sfs.structure.tree;

import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import sfs.util.search.SearchUtil;
import sfs.util.sort.SortUtil;

public class Star<T extends MultiNode> extends AbstractTree<T> {

	private static Logger log = Logger.getLogger( Star.class );

	public Star() {

	}
	
	public Star(T root) {
		super( root );
	}

	/**
	 * Adds as a child.
	 */
	public T add(T t) {
		
		try {
			if ( getRoot() == null ) {
				setRoot( t );
				return null;
			}
			else {
				// guarantees addChild() is called for adding children but not root.
				getRoot().addChild( t );
				
			}
		}
		catch ( Exception ex ) {
			log.error( ex );
			return null;
		}

		return getRoot();
	}

	/**
	 * Deletes from child nodes.
	 */
	public T delete(T t) {
		if ( getRoot().deleteChild( t ) ) {
			return getRoot();
		}

		return null;
	}

	/**
	 * Deletes object at the specified index in unsorted array, from child node.
	 * 
	 * @param index
	 *            Used to specify the object to be removed.
	 * @return True if delete the object successfully, false otherwise.
	 */
	public boolean delete(int index) {
		return getRoot().deleteChild( getRoot().getChildNodes()[index] );
	}

	/**
	 * Finds the specified object.
	 */
	public boolean find(T t) {

		MultiNode[] mns = null;
		try {
			mns = getRoot().getChildren();
			SortUtil.mergeSort( mns );
			SearchUtil.binarySearch( t, mns );
		}
		catch ( NoSuchElementException ex ) {
			log.error( ex );
			return false;
		}
		finally {
			mns = null;
		}

		return true;
	}

	/**
	 * Gets the current number of children.
	 */
	public int getSize() {

		return getRoot().getNumberOfChildren();
	}

	/**
	 * Gets the object at the specified index in unsorted array.
	 * 
	 * @param index
	 *            Used to specify the object to get.
	 * @return Object at the specified index.
	 */
	public MultiNode get(int index) {

		return getRoot().getChildNodes()[index];
	}

	public T[] toArray() {
		return (T[]) getRoot().getChildren();
	}
}
