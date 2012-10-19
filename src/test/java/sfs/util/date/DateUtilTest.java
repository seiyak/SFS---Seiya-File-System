package sfs.util.date;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Test;

public class DateUtilTest {

	private static Logger log = Logger.getLogger( DateUtilTest.class );

	@Test
	public void test() {
		String res = DateUtil.getTimeInGMT();
		assertTrue( "expecting res in GMT but found " + res, findGMT( res ) );
		log.debug( res );
	}

	private boolean findGMT(String res) {

		String gmt = "GMT";
		int resIndex = 0;
		String sub = "";
		while ( resIndex < res.length() ) {

			if ( res.charAt( resIndex ) == gmt.charAt( 0 ) ) {
				sub = res.substring( resIndex, resIndex + gmt.length() );

				if ( sub.equals( gmt ) ) {
					return true;
				}

				resIndex += ( gmt.length() - 1 );
				if ( ( resIndex + gmt.length() ) > res.length() ) {
					break;
				}
			}

			resIndex++;
		}

		return false;
	}

}
