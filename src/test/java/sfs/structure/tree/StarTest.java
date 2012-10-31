package sfs.structure.tree;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StarTest {

	private Star<MultiNode> root;

	@Before
	public void setUp() throws Exception {

		root = new Star<MultiNode>( new MultiNode( null ) );
	}

	@Test
	public void testAddChild() {

		for ( int i = 0; i < 100; i++ ) {
			root.add( new MultiNode( null ) );
		}
		assertTrue( "expecting child size == 100 but found " + root.getSize(), root.getSize() == 100 );
		assertTrue( "expecting true but found false", root.delete( 99 ) );
		assertTrue( "expecting child size == 99 but found " + root.getSize(), root.getSize() == 99 );
	}

	@Test
	public void testFind() {
		for ( int i = 0; i < 1000; i++ ) {
			root.add( new MultiNode( null ) );
		}

		assertTrue( "expecting root.find()== true but found false", root.find( root.get( 888 ) ) );
		assertFalse( "expecting root.find()==false but found true", root.find( new MultiNode( null ) ) );
		assertTrue( "expecting child size == 1000 but found " + root.getSize(), root.getSize() == 1000 );

	}

}
