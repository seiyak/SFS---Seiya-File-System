package sfs.client.http.shortconversation;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.client.http.Clientable;
import sfs.entry.HostEntry;
import sfs.header.http.separator.Colon;
import sfs.request.http.RequestMessage;
import sfs.response.http.ResponseMessage;
import sfs.util.header.http.HeaderUtil;
import sfs.util.ipaddress.LocalIPAddress;
import sfs.util.string.StringUtil;
import sfs.verb.http.Verb;

public class LivenessConversation implements ShortConversation {

	private Map<String, String> localAddressMap;
	private static final String LIVENESS_PATH = "/liveness";
	private static Logger log = Logger.getLogger( LivenessConversation.class );

	public LivenessConversation() {

	}

	public LivenessConversation(Map<String, String> localAddressMap) {
		this.localAddressMap = localAddressMap;
	}

	public void setLocalAddressMap(Map<String, String> localAddressMap) {
		this.localAddressMap = localAddressMap;
	}

	/**
	 * Copied from AbstractClient.getServerHost(String serverAddress, int port).Gets server and port combined with ':'.
	 * 
	 * @return Concatenation of serverAddress and port properties.
	 */
	private String getServerHost(String serverAddress, int port) {

		return serverAddress + new Colon().getSeparator() + port;
	}

	public void writeRequest(Clientable clientable, SocketChannel socketChannel, HostEntry hostEntry)
			throws IOException {

		if ( localAddressMap == null ) {
			log.warn( "localAddressMap is null, call LocalIPAddress.getLocalIPAddress() method" );
			localAddressMap = LocalIPAddress.getLocalIPAddress();
		}

		clientable.write( socketChannel, new RequestMessage().createMessage( Verb.GET, StringUtil
				.getContextPath( LIVENESS_PATH ), HeaderUtil.getRequestLivenessHeader( hostEntry,
				getServerHost( hostEntry.getHost(), hostEntry.getPort() ), localAddressMap.get( "v4" ) ) ) );
	}

	public ResponseMessage readResponse(Clientable clientable, SocketChannel socketChannel, HostEntry hostEntry)
			throws IOException {

		return new ResponseMessage().extractMessage( clientable.read( socketChannel ) );
	}
}
