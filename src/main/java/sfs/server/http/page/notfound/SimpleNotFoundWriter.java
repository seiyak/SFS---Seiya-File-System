package sfs.server.http.page.notfound;

import sfs.entry.HTTPHeaderEntry;
import sfs.header.http.HeaderEntry;
import sfs.header.http.RequestHeaderEntry;
import sfs.mime.Mime;
import sfs.response.statuscode.StatusCode;
import sfs.stat.message.MessageStat;

public class SimpleNotFoundWriter extends AbstractNotFoundWriter {

	@Override
	public String write(MessageStat messageStat) {

		String messageBody = "<html><body><h2>Could not find the resouce that you requested, "
				+ messageStat.getHeader().get( RequestHeaderEntry.CONTEXT_PATH ) + ".</h2></body></html>";
		HTTPHeaderEntry[] entries = new HTTPHeaderEntry[2];
		entries[0] = new HTTPHeaderEntry( HeaderEntry.CONTENT_LENGTH, messageBody.length() );
		entries[1] = new HTTPHeaderEntry( HeaderEntry.CONTENT_TYPE, Mime.HTML );

		String message = responseMessage.createMessage( StatusCode._404, entries, messageBody );

		return message;
	}
}
