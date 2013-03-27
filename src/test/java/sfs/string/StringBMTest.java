package sfs.string;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sfs.util.string.StringUtil;
import sfs.util.string.StringUtilTest;

public class StringBMTest extends StringUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		profile( readTestInputFile(), "zoologist's" );
	}

	private void profile(String source, String pattern) {
		long t1 = System.currentTimeMillis();
		int index = source.indexOf( pattern );
		long t2 = System.currentTimeMillis();
		System.out.println( "with indexOf(). elapsed time: " + ( t2 - t1 ) + " found pattern: " + pattern + " at "
				+ index );
		StringBM stringBM = new StringBM( source, pattern );
		t1 = System.currentTimeMillis();
		index = stringBM.searchFirstIndexOfByMB();
		t2 = System.currentTimeMillis();
		System.out.println( "with searchFirstIndexOfByMB(). elapsed time: " + ( t2 - t1 ) + " found pattern: "
				+ pattern + " at " + index + " substring: " + source.substring( index, index + pattern.length() )
				+ "\n" );
	}
}
