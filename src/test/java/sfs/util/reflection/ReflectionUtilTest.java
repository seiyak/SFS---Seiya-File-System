package sfs.util.reflection;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sfs.structure.StructureNode;

public class ReflectionUtilTest {

	@Test
	public void testGetClassFromArray() {

		Class<StructureNode> nodeClass = ReflectionUtil.getTypeFromArray( new StructureNode[] {} );
		assertNotNull( "expecting nodeClass != null but found null", nodeClass );
		assertTrue(
				"expecting nodeClass.getSimpleName().equals('StructureNode') but found " + nodeClass.getSimpleName(),
				nodeClass.getSimpleName().equals( "StructureNode" ) );
		assertTrue( "expecting nodeClass.getCanonicalName().equals('sfs.structure.StructureNode') but found "
				+ nodeClass.getCanonicalName(), nodeClass.getCanonicalName().equals( "sfs.structure.StructureNode" ) );
	}

}
