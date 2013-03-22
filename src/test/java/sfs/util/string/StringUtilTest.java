package sfs.util.string;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import sfs.header.http.ending.Ending;

public class StringUtilTest {

	private static final String TEST_INPUT_FILE_NAME = "EnglishWords.txt";

	@Test
	public void testGetQueryAsMap() {

		Map<String, String> res1 = StringUtil
				.getQueryAsMap( "first=this+is+a+field&second=was+it+clear+%28already%29%3F" );
		Map<String, String> expected1 = new LinkedHashMap<String, String>();
		expected1.put( "first", "this+is+a+field" );
		expected1.put( "second", "was+it+clear+%28already%29%3F" );
		checkEachEntry( res1, expected1 );

		Map<String, String> res2 = StringUtil.getQueryAsMap( "node=localhost" );
		Map<String, String> expected2 = new LinkedHashMap<String, String>();
		expected2.put( "node", "localhost" );
		checkEachEntry( res2, expected2 );
	}

	private void checkEachEntry(Map<String, String> res, Map<String, String> expected) {

		assertTrue( "expecting res.size()==expected.size(), but found res.size()==" + res.size() + " expected.size()=="
				+ expected.size(), res.size() == expected.size() );

		Iterator<Entry<String, String>> resItr = res.entrySet().iterator();
		Iterator<Entry<String, String>> expectedItr = expected.entrySet().iterator();

		while ( resItr.hasNext() ) {

			Entry<String, String> resEntry = resItr.next();
			Entry<String, String> expectedEntry = expectedItr.next();

			assertTrue(
					"expecting resEntry.getKey()==expectedEntry.getKey() but found resEntry.getKey()=="
							+ resEntry.getKey() + " expectedEntry.getKey()==" + expectedEntry.getKey(), resEntry
							.getKey().equals( expectedEntry.getKey() ) );
			assertTrue( "expecting resEntry.getValue()==expectedEntry.getValue() but found resEntry.getValue()=="
					+ resEntry.getValue() + " expectedEntry.getValue()==" + expectedEntry.getValue(), resEntry
					.getValue().equals( expectedEntry.getValue() ) );
		}
	}

	@Test
	public void testSearchByBM() {

		String pattern = "example";
		String str = "here is a simple example";
		boolean found = StringUtil.searchByBM( str, pattern );
		assertTrue( "expecting found==true but found " + found, found );

		pattern = "amp";
		found = StringUtil.searchByBM( str, pattern );
		assertTrue( "expecting found==true but found " + found, found );

		pattern = "mple";
		found = StringUtil.searchByBM( str, pattern );
		assertTrue( "expecting found==true but found " + found, found );

		pattern = "pll";
		found = StringUtil.searchByBM( str, pattern );
		assertFalse( "expecting found==false but found " + found, found );
	}

	@Test
	public void testSearchFirstIndexOfByMB() {

		String pattern = "example";
		String str = "here is a simple example";
		int index = StringUtil.searchFirstIndexOfByMB( str, pattern );
		assertTrue( "expecting index==17 but found " + index, index == 17 );

		pattern = "amp";
		index = StringUtil.searchFirstIndexOfByMB( str, pattern );
		assertTrue( "expecting index==19 but found " + index, index == 19 );

		pattern = "mple";
		index = StringUtil.searchFirstIndexOfByMB( str, pattern );
		assertTrue( "expecting index==12 but found " + index, index == 12 );

		pattern = "pll";
		index = StringUtil.searchFirstIndexOfByMB( str, pattern );
		assertTrue( "expecting index==-1 but found " + index, index == -1 );

		str = "abc" + Ending.CRLF + Ending.CRLF;
		pattern = Ending.CRLF.toString() + Ending.CRLF.toString();
		index = StringUtil.searchFirstIndexOfByMB( str, pattern );
		assertTrue( "expecting index == " + 3 + " but found " + index, index == 3 );
		
		str = "ab" + Ending.CRLF + "abc" + Ending.CRLF + Ending.CRLF;
		pattern = Ending.CRLF.toString() + Ending.CRLF.toString();
		index = StringUtil.searchFirstIndexOfByMB( str, pattern );
		assertTrue( "expecting index == " + 7 + " but found " + index, index == 7 );

		str = "AaronAOLAgathaDomino'sonAbelson'sZoomAgamemnon's";
		pattern = "Agamemnon's";
		index = StringUtil.searchFirstIndexOfByMB( str, pattern );
		assertTrue( "expecting index == " + ( str.length() - pattern.length() ) + " but found " + index,
				index == ( str.length() - pattern.length() ) );
	}

	@Test
	public void testSearchLastIndexOfByMB() {

		String pattern = "example";
		String str = "here is a simple example";
		int index = StringUtil.searchLastIndexOfByMB( str, pattern );
		assertTrue( "expecting index==23 but found " + index, index == 23 );

		pattern = "amp";
		index = StringUtil.searchLastIndexOfByMB( str, pattern );
		assertTrue( "expecting index==21 but found " + index, index == 21 );

		pattern = "mple";
		index = StringUtil.searchLastIndexOfByMB( str, pattern );
		assertTrue( "expecting index==15 but found " + index, index == 15 );

		pattern = "pll";
		index = StringUtil.searchLastIndexOfByMB( str, pattern );
		assertTrue( "expecting index==-1 but found " + index, index == -1 );
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSearchFirstIndexOfByMBWithFromIndex() {

		String pattern = "example";
		String str = "here is a simple example";

		int index = StringUtil.searchFirstIndexOfByMB( str, pattern, 18 );
		assertTrue( "expecting index==-1 but found " + index, index == -1 );

		index = StringUtil.searchFirstIndexOfByMB( str, pattern, 17 );
		assertTrue( "expecting index==17 but found " + index, index == 17 );

		index = StringUtil.searchFirstIndexOfByMB( str, pattern, 16 );
		assertTrue( "expecting index==17 but found " + index, index == 17 );

		index = StringUtil.searchFirstIndexOfByMB( str, pattern, 15 );
		assertTrue( "expecting index==17 but found " + index, index == 17 );

		index = StringUtil.searchFirstIndexOfByMB( str, pattern, str.length() );
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSearchLastIndexOfByMBWithFromIndex() {

		String pattern = "example";
		String str = "here is a simple example";

		int index = StringUtil.searchLastIndexOfByMB( str, pattern, 18 );
		assertTrue( "expecting index==-1 but found " + index, index == -1 );

		index = StringUtil.searchLastIndexOfByMB( str, pattern, 17 );
		assertTrue( "expecting index==23 but found " + index, index == 23 );

		index = StringUtil.searchLastIndexOfByMB( str, pattern, 16 );
		assertTrue( "expecting index==23 but found " + index, index == 23 );

		index = StringUtil.searchLastIndexOfByMB( str, pattern, 0 );
		assertTrue( "expecting index==23 but found " + index, index == 23 );

		index = StringUtil.searchLastIndexOfByMB( str, pattern, str.length() + 10 );
	}

	@Test
	public void testStartsWith() {
		String pattern = "example";
		String str = "here is a simple example";

		int index = StringUtil.startsWith( str, pattern );
		assertTrue( "expecting index==-1 but found, " + index, index == -1 );

		pattern = "here";
		index = StringUtil.startsWith( str, pattern );
		assertTrue( "expecting index==3 but found, " + index, index == 3 );

		pattern = "here is another simple example";
		index = StringUtil.startsWith( str, pattern );
		assertTrue( "exepcting index==-1 but found, " + index, index == -1 );
	}

	@Test
	public void testSearchFirstIndexOfByMBAgainstIndexOf() {

		String str = readTestInputFile();
		String pattern = "AOL";
		int index1 = str.indexOf( pattern );
		int index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "Aaron";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "Aden";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "Afros";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "Agamemnon's";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "Agassiz";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "Agatha";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "carpet's";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "larva's";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "risen";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "requisitioning";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "rerouting";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );

		pattern = "zygotes";
		index1 = str.indexOf( pattern );
		index2 = StringUtil.searchFirstIndexOfByMB( str, pattern );
		checkEachResult( str, index1, index2, pattern.length() );
	}

	private void checkEachResult(String str, int index1, int index2, int length) {

		assertTrue( "expecting index1==index2 but found index1==" + index1 + ", index2==" + index2
				+ " str[index1,index1+" + length + "]==" + str.substring( index1, index1 + length )
				+ " str[index2,index2+" + length + "]==" + str.substring( index2, index2 + length ), index1 == index2 );
		assertTrue(
				"expecting str.substring(index1,index1+length).equals(str.substring(inde2,inde2+length) but found false",
				str.substring( index1, index1 + length ).equals( str.substring( index2, index2 + length ) ) );
	}

	private String readTestInputFile() {

		String str2 = "";
		BufferedReader br = null;
		try {
			try {
				br = new BufferedReader( new FileReader( new File( getClass().getClassLoader()
						.getResource( TEST_INPUT_FILE_NAME ).toURI() ) ) );
			}
			catch ( URISyntaxException e1 ) {
				e1.printStackTrace();
			}
			String str = "";
			try {
				while ( ( str = br.readLine() ) != null ) {
					str2 += str;
				}
			}
			catch ( IOException e ) {
				e.printStackTrace();
			}
		}
		catch ( FileNotFoundException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				br.close();
			}
			catch ( IOException e ) {
				e.printStackTrace();
			}
		}

		return str2;
	}
}
