package sfs.header.http.separator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SeparatorTest {

	private Separator separator1;
	private Separator separator2;
	private Separator separator3;

	@Before
	public void setUp() throws Exception {

		separator1 = new Colon().add( new Slash() ).add( new Slash() );
		separator2 = new WhiteSpace().add( new Colon() ).add( new Colon() ).add( new Slash() );
		separator3 = new Slash().add( new Slash() ).add( new Slash() ).add( new Colon() );
	}

	@Test
	public void testToString1() {
		String res1 = separator1.toString();
		assertTrue( "expecting res1== '://' but found res1==" + res1, res1.equals( "://" ) );
		assertTrue( "expecting separator1.getSeparator() == ':' but found " + separator1.getSeparator(), separator1
				.getSeparator().equals( ":" ) );

		String res2 = separator2.toString();
		assertTrue( "expecting res2== ' ::/' but found res2==" + res2, res2.equals( " ::/" ) );
		assertTrue( "expecting separator2.getSeparator() == ' ' but found " + separator2.getSeparator(), separator2
				.getSeparator().equals( " " ) );

		String res3 = separator3.toString();
		assertTrue( "expecting res3== '///:' but found res3==" + res3, res3.equals( "///:" ) );
		assertTrue( "expecting separator3.getSeparator() == '/' but found " + separator3.getSeparator(), separator3
				.getSeparator().equals( "/" ) );
	}

}
