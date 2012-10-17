package sfs.client.http;

import java.io.IOException;

import org.apache.log4j.Logger;

import sfs.header.http.HTTPHeader;
import sfs.header.http.RequestHeaderEntry;
import sfs.header.http.RequestType;
import sfs.header.http.separator.Comma;
import sfs.mime.Mime;
import sfs.request.http.RequestMessage;
import sfs.util.string.StringUtil;
import sfs.verb.http.Verb;

public class NodeClient extends HttpClient {

	private String requestContextPath;
	private RequestType[] requestTypes;
	private static Logger log = Logger.getLogger( NodeClient.class );

	public NodeClient(String serverAddress, int port) {
		super( serverAddress, port );
	}

	public NodeClient(String serverAddress, int port, String contextPath) {
		super( serverAddress, port );
		this.requestContextPath = contextPath;
	}

	public NodeClient(String serverAddress, int port, int maxTrial) {

		super( serverAddress, port, maxTrial );
	}

	public NodeClient(String serverAddress, int port, int maxTrial, String contextPath) {

		super( serverAddress, port, maxTrial );
		this.requestContextPath = contextPath;
	}

	public void setContextPath(String contextPath) {
		this.requestContextPath = contextPath;
	}

	public String getContextPath() {

		return requestContextPath;
	}

	@Override
	protected void doInitiate() throws IOException {

		sendRequest( requestContextPath );
	}

	public void setRequestTypes(RequestType[] requestTypes) {
		this.requestTypes = requestTypes;
	}

	private void sendRequest(String requestContextPath) throws IOException {

		RequestMessage requestMessage = new RequestMessage();
		String str = requestMessage.request( Verb.GET, StringUtil.getContextPath( requestContextPath ) );

		HTTPHeader header = new HTTPHeader();
		header.put( RequestHeaderEntry.ACCEPT, Mime.HTML );
		header.put( RequestHeaderEntry.HOST, getServerHost() );
		header.put( RequestHeaderEntry.TYPE, formatRequestTypes( requestTypes ) );
		header.put( RequestHeaderEntry.ORIGIN, getLocalAddressMap().get( "v4" ) );

		str += header.format();

		log.debug( "about to send a request with: " + str );
		write( str );
	}

	/**
	 * Formats RequestType array and returns as String separated by comma.
	 * 
	 * @param requestTypes
	 *            Request that this client makes to the server.
	 * @return String representation of the types separated by comma.
	 */
	private String formatRequestTypes(RequestType[] requestTypes) {

		// TODO need a way to predict the request type.
		if ( requestTypes == null ) {
			return "";
		}

		String res = "";

		for ( int i = 0; i < requestTypes.length; i++ ) {

			if ( i < ( requestTypes.length - 1 ) ) {
				res += requestTypes[i].toString() + new Comma().getSeparator();
			}
			else {
				res = requestTypes[i].toString();
			}
		}

		return res;
	}
}
