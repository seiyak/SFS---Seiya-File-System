package sfs.response.http;

import sfs.header.http.ending.Ending;
import sfs.header.http.separator.WhiteSpace;
import sfs.response.Response;
import sfs.response.statuscode.StatusCode;

public class ResponseMessage extends Response {

	private static final String HTTP_VERSION = "HTTP/1.1";

	@Override
	protected String doResponse(StatusCode statusCode) {

		return HTTP_VERSION + new WhiteSpace().getSeparator() + statusCode.getNumber()
				+ new WhiteSpace().getSeparator() + statusCode.getString() + Ending.CRLF;
	}

}
