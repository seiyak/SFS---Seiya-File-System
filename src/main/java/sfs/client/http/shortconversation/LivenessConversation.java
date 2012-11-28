package sfs.client.http.shortconversation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.entry.HostEntry;
import sfs.header.http.separator.Colon;
import sfs.request.http.RequestMessage;
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

	/**
	 * Writes liveness request onto the specified socket channel.
	 * 
	 * @param socketChannel
	 *            Used to write a liveness request.
	 * @param hostEntry
	 *            Host to be sent the liveness request.
	 */
	public void writeRequest(SocketChannel socketChannel, HostEntry hostEntry) throws IOException {

		if ( localAddressMap == null ) {
			log.warn( "localAddressMap is null, call LocalIPAddress.getLocalIPAddress() method" );
			localAddressMap = LocalIPAddress.getLocalIPAddress();
		}

		socketChannel.write( ByteBuffer.wrap( new RequestMessage().createMessage(
				Verb.GET,
				StringUtil.getContextPath( LIVENESS_PATH ),
				HeaderUtil.getRequestLivenessHeader( hostEntry,
						getServerHost( hostEntry.getHost(), hostEntry.getPort() ), localAddressMap.get( "v4" ) ) )
				.getBytes() ) );
	}
}
