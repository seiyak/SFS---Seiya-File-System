package sfs.util.search;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import sfs.structure.StructureNode;
import sfs.structure.tree.MultiNode;
import sfs.util.sort.SortUtil;

public class SearchUtilTest {

	@Test
	public void testBinarySearch() {

		StructureNode[] nodes = generateNodes( 100 );
		SortUtil.mergeSort( nodes );
		StructureNode target1 = nodes[nodes.length - 2];
		StructureNode res1 = SearchUtil.binarySearch( target1, nodes );
		assertTrue( "expecting target1.getId()==res1.getId() but found target1.getId()==" + target1.getId()
				+ " res1.getId()==" + res1.getId(), target1.getId() == res1.getId() );

		nodes = null;
		nodes = generateNodes( 9993 );
		SortUtil.mergeSort( nodes );
		StructureNode target2 = nodes[nodes.length - 3];
		StructureNode res2 = SearchUtil.binarySearch( target2, nodes );
		assertTrue( "expecting target2.getId()==res2.getId() but found target2.getId()==" + target2.getId()
				+ " res2.getId()==" + res2.getId(), target2.getId() == res2.getId() );

		nodes = null;
		nodes = generateNodes( 9991 );
		SortUtil.mergeSort( nodes );
		StructureNode target3 = nodes[nodes.length - 1];
		StructureNode res3 = SearchUtil.binarySearch( target3, nodes );
		assertTrue( "expecting target3.getId()==res3.getId() but found target3.getId()==" + target3.getId()
				+ " res3.getId()==" + res3.getId(), target3.getId() == res3.getId() );

		nodes = null;
		nodes = generateNodes( 2 );
		SortUtil.mergeSort( nodes );
		StructureNode target4 = nodes[0];
		StructureNode res4 = SearchUtil.binarySearch( target4, nodes );
		assertTrue( "expecting target4.getId()==res4.getId() but found target4.getId()==" + target4.getId()
				+ " res4.getId()==" + res4.getId(), target4.getId() == res4.getId() );

	}

	@Test(expected = NoSuchElementException.class)
	public void testBinarySearchWithException1() {

		StructureNode[] nodes = generateNodes( 100 );
		SortUtil.mergeSort( nodes );
		StructureNode target2 = new MultiNode( null );
		SearchUtil.binarySearch( target2, nodes );
	}

	private StructureNode[] generateNodes(int length) {

		StructureNode[] nodes = new StructureNode[length];
		for ( int i = 0; i < nodes.length; i++ ) {
			nodes[i] = new MultiNode( null );
		}

		return nodes;
	}

}
