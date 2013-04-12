package sfs.server.http.handler.contextpath;

import sfs.response.http.ResponseMessage;

public abstract class AbstractContextPathHandler implements ContextPathHandler {

	protected ResponseMessage responseMessage;

	protected AbstractContextPathHandler() {
		responseMessage = new ResponseMessage();
	}
}
