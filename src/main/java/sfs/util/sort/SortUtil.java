package sfs.util.sort;

import sfs.structure.StructureNode;
import sfs.util.Util;

public class SortUtil {

	/**
	 * Sorts the specified array using merge sort.
	 * 
	 * @param nodes
	 *            Array to be sorted.
	 */
	public static <T extends StructureNode> void mergeSort(T[] nodes) {

		doMergeSort( nodes );
	}

	/**
	 * Sorts the specified array using merge sort.
	 * 
	 * @param nodes
	 *            Array to be sorted.
	 */
	private static <T extends StructureNode> void doMergeSort(T[] nodes) {

		if ( !Util.isNullOrEmpty( nodes ) ) {
			split( 0, nodes.length - 1, nodes );
		}
	}

	/**
	 * Splits the specified array into two parts.
	 * 
	 * @param lo
	 *            Starting index.
	 * @param hi
	 *            Ending index.
	 * @param nodes
	 *            Array to be sorted.
	 */
	private static <T extends StructureNode> void split(int lo, int hi, T[] nodes) {

		if ( lo < hi ) {

			int mid = ( lo + hi ) / 2;

			split( lo, mid, nodes );
			split( mid + 1, hi, nodes );
			merge( lo, mid, hi, nodes );
		}
	}

	/**
	 * Merge process in the merge sort.
	 * 
	 * @param start
	 *            Starting index.
	 * @param m
	 *            Middle index.
	 * @param end
	 *            Ending index.
	 * @param nodes
	 *            Array to be sorted.
	 */
	private static <T extends StructureNode> void merge(int start, int m, int end, T[] nodes) {

		int i = 0, j = m + 1, k = start;
		Object[] tmp = new Object[( m - start ) + 1];
		System.arraycopy( nodes, start, tmp, 0, tmp.length );

		while ( k < j && j <= end ) {
			if ( ( (T) tmp[i] ).getId() <= nodes[j].getId() ) {
				nodes[k++] = (T) tmp[i++];
			}
			else if ( ( (T) tmp[i] ).getId() >= nodes[j].getId() ) {
				nodes[k++] = nodes[j++];
			}
		}

		while ( k < j ) {
			nodes[k++] = (T) tmp[i++];
		}
	}
}
