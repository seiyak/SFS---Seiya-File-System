package sfs.request.http;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sfs.entry.HTTPHeaderEntry;
import sfs.header.http.HTTPHeader;
import sfs.header.http.HeaderEntry;
import sfs.header.http.RequestHeaderEntry;
import sfs.header.http.ResponseHeaderEntry;
import sfs.header.http.ending.Ending;
import sfs.header.http.separator.WhiteSpace;
import sfs.request.Request;
import sfs.response.http.ResponseMessage;
import sfs.util.reflection.ReflectionUtil;
import sfs.verb.http.Verb;

public class RequestMessage extends Request {

	private final Map requestMessageMap;
	private String verb;
	private String contextPath;
	private String requestHTTPVersion;
	private final Map<HeaderEntry,String> header;
	private String content;
	private static final String HTTP_VERSION = "HTTP/1.1";

	public RequestMessage(){
		requestMessageMap = ReflectionUtil.getStaticMembers( RequestHeaderEntry.class, false );
		header = new HashMap<HeaderEntry,String>();
	}

	protected Map getRequestMessageMap() {
		return requestMessageMap;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getRequestHTTPVersion() {
		return requestHTTPVersion;
	}

	public void setRequestHTTPVersion(String requestHTTPVersion) {
		this.requestHTTPVersion = requestHTTPVersion;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<HeaderEntry,String> getHeader() {
		return header;
	}
	
	@Override
	protected String doRequest(Verb verb, String resource) {

		return verb.toString() + new WhiteSpace().getSeparator() + resource + new WhiteSpace().getSeparator()
				+ HTTP_VERSION + Ending.CRLF;
	}

	/**
	 * Creates Message with the specified verb, resource and HTTP headers.
	 * @param verb Verb used for the resource.
	 * @param resource Resource where the message to be sent.
	 * @param entries Headers.
	 * @return Message as String.
	 */
	public String createMessage(Verb verb, String resource, HTTPHeaderEntry[] entries) {

		String str = doRequest( verb, resource );
		HTTPHeader header = new HTTPHeader();
		for ( HTTPHeaderEntry entry : entries ) {
			header.put( entry.getKey(), entry.getValue() );
		}

		str += header.format();
		return str;
	}
	
	/**
	 * Copied from sfs.response.http.ResponseMessage.extractMessage().Extracts status line, header and content from the
	 * specified string.
	 * 
	 * @param request
	 *            HTTP request where status line, header and content are extracted.
	 * @return This object.
	 */
	public RequestMessage extractMessage(String request) {

		String[] res = request.split( Ending.CRLF.toString() );
		if ( ( res == null ) || ( res.length == 1 ) ) {
			content = "";
		}
		else {
			int count = 0;
			int current = 0;
			for ( String str : res ) {

				if ( current == 0 ) {
					setStatusCode( str );
					current++;
					continue;
				}
				else {
					if ( str.isEmpty() ) {
						count++;
					}
					else {

						if ( count == 1 ) {
							content = str;
						}
						else {
							extractHeader( str );
						}
					}
				}

				current++;
			}
		}
		return this;
	}

	/**
	 * Sets status line from the specified string.
	 * 
	 * @param statusLine
	 *            String holds the status line.
	 */
	private void setStatusCode(String statusLine) {

		String[] each = statusLine.split( " " );
		verb = each[0];
		contextPath = each[1];
		requestHTTPVersion = each[2];
	}

	/**
	 * Extracts each header entry from the specified string.
	 * 
	 * @param headerStr
	 *            String holds each header key and value.
	 */
	private void extractHeader(String headerStr) {

		String[] each = headerStr.split( ": " );
		header.put( (HeaderEntry) getRequestMessageMap().get( each[0] ), each[1] );
	}
}
