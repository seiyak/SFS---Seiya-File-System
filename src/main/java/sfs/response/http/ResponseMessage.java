package sfs.response.http;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sfs.entry.HTTPHeaderEntry;
import sfs.entry.HostEntry;
import sfs.header.http.HTTPHeader;
import sfs.header.http.HeaderEntry;
import sfs.header.http.ResponseHeaderEntry;
import sfs.header.http.ending.Ending;
import sfs.header.http.separator.WhiteSpace;
import sfs.response.Response;
import sfs.response.statuscode.StatusCode;
import sfs.util.reflection.ReflectionUtil;

public class ResponseMessage extends Response {

	private final Map responseMessageMap;
	private String responseHTTPVersion;
	private String statusCode;
	private String reasonPhrase;
	private final List<HTTPHeaderEntry> header;
	private String content;
	private static Logger log = Logger.getLogger( ResponseMessage.class );
	private static final String HTTP_VERSION = "HTTP/1.1";
	public static final String KEY_HOST = "host";
	public static final String KEY_PORT = "port";
	public static final String KEY_MAX_TRIAL = "maxTrial";
	public static final String KEY_NEXT_HOSTS = "nextHosts";
	
	public ResponseMessage(){
		responseMessageMap = ReflectionUtil.getStaticMembers( ResponseHeaderEntry.class, false );
		header = new LinkedList<HTTPHeaderEntry>();
	}

	protected Map getResponseMessageMap() {
		return responseMessageMap;
	}

	@Override
	protected String doResponse(StatusCode statusCode) {

		return HTTP_VERSION + new WhiteSpace().getSeparator() + statusCode.getNumber()
				+ new WhiteSpace().getSeparator() + statusCode.getString() + Ending.CRLF;
	}

	public HTTPHeaderEntry[] getHeader() {
		return header.toArray( new HTTPHeaderEntry[header.size()] );
	}

	public String getContent() {
		return content;
	}

	/**
	 * Extracts status line, header and content from the specified string.
	 * @param response HTTP response where status line, header and content are extracted.
	 * @return This object.
	 */
	public ResponseMessage extractMessage(String response) {

		String[] res = response.split( Ending.CRLF.toString() );
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
	 * @param statusLine String holds the status line.
	 */
	private void setStatusCode(String statusLine) {

		String[] each = statusLine.split( " " );
		responseHTTPVersion = each[0];
		statusCode = each[1];
		reasonPhrase = "";
		for(int i = 2;i < each.length;i++){
			
			if(i < (each.length - 1)){
				reasonPhrase += each[i] + " ";
			}else{
				reasonPhrase += each[i];
			}
		}
	}

	/**
	 * Extracts each header entry from the specified string.
	 * 
	 * @param headerStr
	 *            String holds each header key and value.
	 * @return Header key and value as HTTPHeaderEntry.
	 */
	private HTTPHeaderEntry extractHeader(String headerStr) {

		HTTPHeaderEntry headerEntry = null;
		String[] each = headerStr.split( ": " );

		headerEntry = new HTTPHeaderEntry( (HeaderEntry) getResponseMessageMap().get( each[0] ), each[1] );
		header.add( headerEntry );
		return headerEntry;
	}

	public String getResponseHTTPVersion() {
		return responseHTTPVersion;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	/**
	 * Gets a value for the specified key. The content is assume to be in JSON.
	 * 
	 * @param key
	 *            Key in the JSON.
	 * @return Corresponding value for the key.
	 */
	public Object get(String key) {

		JSONObject json = null;
		try {
			json = new JSONObject( content );
			return json.get( key );

		}
		catch ( JSONException ex ) {
			log.error( ex );
			log.error( "json: " + json );
			return null;
		}
	}

	/**
	 * Checks if the content as JSON contains the specified key or not.
	 * 
	 * @param key
	 *            Used to check the content as JSON.
	 * @return True if the content contains the key, false otherwise.
	 */
	public boolean contains(String key) {

		JSONObject json = null;
		try {
			json = new JSONObject( content );
			return json.has( key );
		}
		catch ( JSONException ex ) {
			log.error( ex );
		}

		return false;
	}

	/**
	 * Gets hosts used after the initiation process. These hosts will be child nodes or next node that
	 * the client that calls this method interact from now on.
	 * 
	 * @param key
	 *            Key in the JSON.
	 * @return HostEntry array holding the hosts.
	 * @throws JSONException
	 */
	public HostEntry[] getNextHosts(String key) {

		HostEntry[] hostEntries = null;
		log.debug( "key is: " + key );
		try {
			JSONArray array = (JSONArray) get( key );

			if ( array.length() == 1 && array.get( 0 ) == JSONObject.NULL ) {
				hostEntries = new HostEntry[] { null };
			}
			else {
				hostEntries = new HostEntry[array.length()];
				for ( int i = 0; i < array.length(); i++ ) {
					hostEntries[i] = new HostEntry( (String) array.getJSONObject( i ).get( KEY_HOST ), (Integer) array
							.getJSONObject( i ).get( KEY_PORT ), (Integer) array.getJSONObject( i ).get( KEY_MAX_TRIAL ) );
				}
			}
		}
		catch ( JSONException ex ) {
			log.error( ex );
			hostEntries = null;
			hostEntries = new HostEntry[] { null };
		}

		return hostEntries;
	}

	/**
	 * Creates response message with the specified status code, header entries and message.
	 * 
	 * @param statusCode
	 *            Used to define the status code for the response.
	 * @param entries
	 *            Entries on the response header.
	 * @param message
	 *            Message body.
	 * @return Response message as String.
	 */
	public String createMessage(StatusCode statusCode, HTTPHeaderEntry[] entries, String message) {

		String str = doResponse( statusCode );
		HTTPHeader header = new HTTPHeader();
		for ( HTTPHeaderEntry entry : entries ) {
			header.put( entry.getKey(), entry.getValue() );
		}

		str += header.format();
		str += message;

		return str;
	}

	/**
	 * Gets HTTP header as map.
	 * 
	 * @return Map representation of HTTP header.
	 */
	public Map<String, Object> getHeaderAsMap() {

		Map<String, Object> map = new HashMap<String, Object>();

		if ( header != null && !header.isEmpty() ) {

			for ( HTTPHeaderEntry entry : header ) {
				map.put( entry.getKey().toString(), entry.getValue() );
			}
		}

		return map;
	}
}
