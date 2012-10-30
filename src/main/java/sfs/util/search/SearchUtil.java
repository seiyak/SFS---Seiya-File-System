package sfs.util.search;

import java.util.NoSuchElementException;

import sfs.structure.StructureNode;
import sfs.util.Util;

public class SearchUtil {

	/**
	 * Searches an Object from the specified array using binary search.
	 * 
	 * @param node
	 *            To be searched.
	 * @param nodes
	 *            Where the search is taken place.
	 * @return Found object.
	 */
	public static <T extends StructureNode> T binarySearch(T node, T[] nodes) {

		if ( Util.isNull( node ) || Util.isNullOrEmpty( nodes ) ) {
			throw new IllegalArgumentException( "the parameter node and/or nodes are null." );
		}

		if ( nodes.length == 1 ) {
			if ( node.getId() == nodes[0].getId() ) {
				return nodes[0];
			}

			throw new NoSuchElementException( "could not find the target by id, " + node.getId()
					+ " in the specified array" );
		}

		int low = 0, mid = 0, hi = nodes.length;

		while ( low != hi ) {

			mid = ( low + hi ) / 2;

			if ( nodes[mid].getId() == node.getId() ) {
				return nodes[mid];
			}
			else if ( nodes[mid].getId() > node.getId() ) {
				hi = mid;
			}
			else if ( nodes[mid].getId() < node.getId() ) {
				low = mid + 1;
			}
		}

		throw new NoSuchElementException( "could not find the target by id, " + node.getId()
				+ " in the specified array" );
	}

	/**
	 * Searches an Object from the specified array using binary search and returns the index.
	 * 
	 * @param node
	 *            To be searched.
	 * @param nodes
	 *            Where the search is taken place.
	 * @return Index associated with the found object.
	 */
	public static <T extends StructureNode> int binarySearchIndex(T node, T[] nodes) {

		if ( Util.isNull( node ) || Util.isNullOrEmpty( nodes ) ) {
			throw new IllegalArgumentException( "the parameter node and/or nodes are null." );
		}

		if ( nodes.length == 1 ) {
			if ( node.getId() == nodes[0].getId() ) {
				return 0;
			}

			throw new NoSuchElementException( "could not find the target by id, " + node.getId()
					+ " in the specified array" );
		}

		int low = 0, mid = 0, hi = nodes.length;

		while ( low != hi ) {

			mid = ( low + hi ) / 2;

			if ( nodes[mid].getId() == node.getId() ) {
				return mid;
			}
			else if ( nodes[mid].getId() > node.getId() ) {
				hi = mid;
			}
			else if ( nodes[mid].getId() < node.getId() ) {
				low = mid + 1;
			}
		}

		throw new NoSuchElementException( "could not find the target by id, " + node.getId()
				+ " in the specified array" );
	}
}
