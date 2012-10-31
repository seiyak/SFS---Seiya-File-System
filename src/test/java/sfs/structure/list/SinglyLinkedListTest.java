package sfs.structure.list;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SinglyLinkedListTest {

	private SinglyLinkedList<SinglyNode> list;
	private SinglyNode head;
	private final int NUMBER_OF_LOOP = 1000;

	@Before
	public void setUp() throws Exception {

		head = new SinglyNode( null );
		list = new SinglyLinkedList<SinglyNode>( head );
	}

	@Test
	public void testAdd() {

		for ( int i = 0; i < NUMBER_OF_LOOP; i++ ) {
			list.add( new SinglyNode( null ) );
		}
		assertTrue( "expecting size==NUMBER_OF_LOOP but found " + list.getSize(), list.getSize() == NUMBER_OF_LOOP );
		SinglyNode node2 = list.get( 3 );
		SinglyNode node3 = list.get( 4 );
		assertTrue( "expecting true but found false", list.delete( node2 ) );
		SinglyNode node = list.get( 2 );
		assertTrue( "expecting node.getNext().getId()==node3.getId() but found node.getNext().getId()=="
				+ node.getNext().getId() + " node3.getId()==" + node3.getId(), node.getNext().getId() == node3.getId() );
		assertTrue( "expecting size==NUMBER_OF_LOOP - 1 but found " + list.getSize(),
				list.getSize() == NUMBER_OF_LOOP - 1 );
		assertFalse( "expecting list.find(node2)==false buf found true", list.find( node2 ) );
	}
}
