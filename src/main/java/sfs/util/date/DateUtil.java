package sfs.util.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	private static final SimpleDateFormat GMT_FORMAT = new SimpleDateFormat( "EEE, MMM d, yyyy hh:mm:ss a z" );

	/**
	 * From http://stackoverflow.com/questions/308683/how-can-i-get-the-current-date-and-time-in-utc-or-gmt-in-java
	 * 
	 * @return
	 */
	public static String getTimeInGMT() {
		Calendar c = Calendar.getInstance();

		TimeZone z = c.getTimeZone();
		int offset = z.getRawOffset();
		if ( z.inDaylightTime( new Date() ) ) {
			offset = offset + z.getDSTSavings();
		}
		int offsetHrs = offset / 1000 / 60 / 60;
		int offsetMins = offset / 1000 / 60 % 60;

		c.add( Calendar.HOUR_OF_DAY, ( -offsetHrs ) );
		c.add( Calendar.MINUTE, ( -offsetMins ) );

		GMT_FORMAT.setTimeZone( TimeZone.getTimeZone( "GMT:00" ) );

		return GMT_FORMAT.format( c.getTime() );
	}
}
