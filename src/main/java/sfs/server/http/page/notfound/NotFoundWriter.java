package sfs.server.http.page.notfound;

import sfs.stat.message.MessageStat;

public interface NotFoundWriter {

	public String write(MessageStat messageStat);
}
