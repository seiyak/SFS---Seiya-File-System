package sfs.util.reflection;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import sfs.header.http.HeaderEntry;
import sfs.header.http.RequestHeaderEntry;
import sfs.header.http.ResponseHeaderEntry;
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

	@Test
	public void testGetStaticMembers() {

		Map<String, String> statics = ReflectionUtil.getStaticMembers( RequestHeaderEntry.class );
		assertFalse( "expecting !statics.isEmpty() but found empty", statics.isEmpty() );
		String[] expected = new String[] { "Accept", "Greeting", "Host", "Origin", "Type", "Liveness", "Content-Type",
				"Date" };
		checkEachElement( statics, expected );

		statics.clear();
		expected = null;

		statics = ReflectionUtil.getStaticMembers( ResponseHeaderEntry.class );
		assertFalse( "expecting !statics.isEmpty() but found empty", statics.isEmpty() );
		expected = new String[] { "Content-Length", "Liveness-Back", "Content-Type", "Date" };
		checkEachElement( statics, expected );

		statics.clear();
		expected = null;

		statics = ReflectionUtil.getStaticMembers( HeaderEntry.class );
		assertFalse( "expecting !statics.isEmpty() but found empty", statics.isEmpty() );
		expected = new String[] { "Content-Type", "Date" };
		checkEachElement( statics, expected );
	}
	
	private void checkEachElement(Map<String, String> statics, String[] expected) {
		for ( String exp : expected ) {
			assertTrue( "expecting statics.get('" + exp + "') exists on statics but doesn't exist",
					statics.get( exp ) != null );
		}
	}
}
