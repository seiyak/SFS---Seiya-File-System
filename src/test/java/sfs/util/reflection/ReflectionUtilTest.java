package sfs.util.reflection;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

	@Test
	public void testGetStaticMembers2() {
		Map statics = ReflectionUtil.getStaticMembers( RequestHeaderEntry.class, false );
		assertFalse( "expecting !statics.isEmpty() but found empty", statics.isEmpty() );
		Iterator<Entry> itr = statics.entrySet().iterator();
		checkEachElement( itr );

		statics.clear();
		statics = ReflectionUtil.getStaticMembers( HeaderEntry.class, false );
		assertFalse( "expecting !statics.isEmpty() but found empty", statics.isEmpty() );
		itr = statics.entrySet().iterator();
		checkEachElement( itr );

		statics.clear();
		statics = ReflectionUtil.getStaticMembers( ResponseHeaderEntry.class, false );
		assertFalse( "expecting !statics.isEmpty() but found empty", statics.isEmpty() );
		itr = statics.entrySet().iterator();
		checkEachElement( itr );
	}

	private void checkEachElement(Iterator<Entry> itr) {
		while ( itr.hasNext() ) {
			Entry entry = itr.next();
			if ( entry.getKey().toString().equals( "Accept" ) ) {
				assertTrue( "expecting entry.getValue()==RequestHeaderEntry.ACCEPT but found " + entry.getValue(),
						entry.getValue().equals( RequestHeaderEntry.ACCEPT ) );
			}
			else if ( entry.getKey().toString().equals( "Greeting" ) ) {
				assertTrue( "expecting entry.getValue()==RequestHeaderEntry.GREETING but found " + entry.getValue(),
						entry.getValue().equals( RequestHeaderEntry.GREETING ) );
			}
			else if ( entry.getKey().toString().equals( "Host" ) ) {
				assertTrue( "expecting entry.getValue()==RequestHeaderEntry.Host but found " + entry.getValue(), entry
						.getValue().equals( RequestHeaderEntry.HOST ) );
			}
			else if ( entry.getKey().toString().equals( "Origin" ) ) {
				assertTrue( "expecting entry.getValue()==RequestHeaderEntry.ORIGIN but found " + entry.getValue(),
						entry.getValue().equals( RequestHeaderEntry.ORIGIN ) );
			}
			else if ( entry.getKey().toString().equals( "Type" ) ) {
				assertTrue( "expecting entry.getValue()==RequestHeaderEntry.TYPE but found " + entry.getValue(), entry
						.getValue().equals( RequestHeaderEntry.TYPE ) );
			}
			else if ( entry.getKey().toString().equals( "Liveness" ) ) {
				assertTrue( "expecting entry.getValue()==RequestHeaderEntry.LIVENESS but found " + entry.getValue(),
						entry.getValue().equals( RequestHeaderEntry.LIVENESS ) );
			}
			else if ( entry.getKey().toString().equals( "Content-Length" ) ) {
				assertTrue(
						"expecting entry.getValue()==ResponseHeaderEntry.CONTENT_LENGTH but found " + entry.getValue(),
						entry.getValue().equals( HeaderEntry.CONTENT_LENGTH ) );
			}
			else if ( entry.getKey().toString().equals( "Liveness-Back" ) ) {
				assertTrue(
						"expecting entry.getValue()==ResponseHeaderEntry.LIVENESS_BACK but found " + entry.getValue(),
						entry.getValue().equals( ResponseHeaderEntry.LIVENESS_BACK ) );
			}
			else if ( entry.getKey().toString().equals( "Content-Type" ) ) {
				assertTrue( "expecting entry.getValue()==HeaderEntry.CONTENT_TYPE but found " + entry.getValue(), entry
						.getValue().equals( HeaderEntry.CONTENT_TYPE ) );
			}
			else if ( entry.getKey().toString().equals( "Date" ) ) {
				assertTrue( "expecting entry.getValue()==HeaderEntry.DATE but found " + entry.getValue(), entry
						.getValue().equals( HeaderEntry.DATE ) );
			}
		}
	}
}
