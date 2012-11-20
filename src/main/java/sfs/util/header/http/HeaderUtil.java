package sfs.util.header.http;

import sfs.entry.HTTPHeaderEntry;
import sfs.entry.HostEntry;
import sfs.header.http.HeaderEntry;
import sfs.header.http.RequestHeaderEntry;
import sfs.header.http.ResponseHeaderEntry;
import sfs.mime.Mime;
import sfs.util.date.DateUtil;

public class HeaderUtil {

	public static HTTPHeaderEntry[] getRequestLivenessHeader(HostEntry hostEntry, String serverHost, String ipAddress) {

		return new HTTPHeaderEntry[] { new HTTPHeaderEntry( RequestHeaderEntry.ACCEPT, Mime.HTML ),
				new HTTPHeaderEntry( RequestHeaderEntry.HOST, serverHost ),
				new HTTPHeaderEntry( RequestHeaderEntry.LIVENESS, "false" ),
				new HTTPHeaderEntry( RequestHeaderEntry.ORIGIN, ipAddress ),
				new HTTPHeaderEntry( HeaderEntry.DATE, DateUtil.getTimeInGMT() ) };
	}

	public static HTTPHeaderEntry[] getResponseLivenessHeader() {

		return new HTTPHeaderEntry[] { new HTTPHeaderEntry( HeaderEntry.CONTENT_TYPE, Mime.JSON ),
				new HTTPHeaderEntry( ResponseHeaderEntry.CONTENT_LENGTH, 0 ),
				new HTTPHeaderEntry( ResponseHeaderEntry.LIVENESS_BACK, "true" ),
				new HTTPHeaderEntry( HeaderEntry.DATE, DateUtil.getTimeInGMT() ) };
	}

	public static HTTPHeaderEntry[] getResponseLivenessHeader(int contentLength) {

		return new HTTPHeaderEntry[] { new HTTPHeaderEntry( HeaderEntry.CONTENT_TYPE, Mime.JSON ),
				new HTTPHeaderEntry( ResponseHeaderEntry.CONTENT_LENGTH, contentLength ),
				new HTTPHeaderEntry( ResponseHeaderEntry.LIVENESS_BACK, "true" ),
				new HTTPHeaderEntry( HeaderEntry.DATE, DateUtil.getTimeInGMT() ) };
	}

	public static HTTPHeaderEntry[] getRequestGreetingHeader(String greeting, HostEntry hostEntry, String serverHost,
			String ipAddress) {
		return new HTTPHeaderEntry[] { new HTTPHeaderEntry( RequestHeaderEntry.ACCEPT, Mime.HTML ),
				new HTTPHeaderEntry( RequestHeaderEntry.HOST, serverHost ),
				new HTTPHeaderEntry( RequestHeaderEntry.GREETING, greeting ),
				new HTTPHeaderEntry( RequestHeaderEntry.ORIGIN, ipAddress ),
				new HTTPHeaderEntry( HeaderEntry.DATE, DateUtil.getTimeInGMT() ) };

	}
}
