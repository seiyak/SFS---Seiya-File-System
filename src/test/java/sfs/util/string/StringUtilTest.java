package sfs.util.string;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

public class StringUtilTest {

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
}
