package sfs.server.http.page.notfound;

import sfs.response.http.ResponseMessage;
import sfs.stat.message.MessageStat;

public abstract class AbstractNotFoundWriter implements NotFoundWriter {

	protected final ResponseMessage responseMessage;
	
	protected AbstractNotFoundWriter(){
		responseMessage = new ResponseMessage();
	}
	
	public String write(MessageStat messageStat) {
		return "<html><body>404 Not Found</body></html>";
	}
}
