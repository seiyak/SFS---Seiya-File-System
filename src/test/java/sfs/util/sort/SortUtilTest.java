package sfs.util.sort;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import sfs.structure.StructureNode;
import sfs.structure.tree.MultiNode;

public class SortUtilTest {

	private StructureNode[] nodes;
	private static Logger log = Logger.getLogger( SortUtilTest.class );
	private final int NUMBER_OF_TEST_NODES = 999;

	@Before
	public void setUp() throws Exception {
		nodes = new MultiNode[NUMBER_OF_TEST_NODES];
		setUpMultiNodeArray();
	}

	private void setUpMultiNodeArray() {

		for ( int i = 0; i < NUMBER_OF_TEST_NODES; i++ ) {
			nodes[i] = new MultiNode( null );
		}
	}

	@Test
	public void testMergeSort() {

		SortUtil.mergeSort( nodes );
		checkEachElement( nodes );
	}

	private void checkEachElement(StructureNode[] nodes) {

		int index = nodes[0].getId();
		for ( int i = 1; i < nodes.length; i++ ) {
			assertTrue( "expecting nodes[i].getId() > index but found id==" + nodes[i].getId() + ", index=" + index,
					nodes[i].getId() > index );
			index = nodes[i].getId();
		}
	}
}
