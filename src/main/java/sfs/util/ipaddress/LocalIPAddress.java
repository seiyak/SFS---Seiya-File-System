package sfs.util.ipaddress;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.header.http.separator.Period;

public class LocalIPAddress {
	
	private static final String VMNET_PREFIX = "vmnet";
	private static final String V4 = "v4";
	private static final String V6 = "v6";
	private static Logger log = Logger.getLogger( LocalIPAddress.class );

	/**
	 * Gets the local IP address. The keys are 'v4' and 'v6' for the corresponding representations.
	 * 
	 * @return Map having the local address represented in IPv4 and IPv6.
	 */
	public static Map<String, String> getLocalIPAddress() {

		String[] addresses = getLocal();

		if ( addresses == null || addresses.length == 0 ) {
			return Collections.EMPTY_MAP;
		}

		return getLocalIPAddressMap( addresses );
	}

	/**
	 * Gets the local IP address.
	 * 
	 * @return String array stores the local IP address in IPv4 and IPv6.
	 */
	private static String[] getLocal() {

		String[] locals = new String[2];
		int index = 0;

		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while ( interfaces.hasMoreElements() ) {
				NetworkInterface networkInterface = interfaces.nextElement();

				if ( ( networkInterface.getName().startsWith( VMNET_PREFIX ) )
						|| ( !networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual() ) )
					continue;
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while ( addresses.hasMoreElements() ) {
					InetAddress address = addresses.nextElement();
					if ( address.isLoopbackAddress() )
						continue;
					locals[index++] = address.getHostAddress();
				}
			}
		}
		catch ( SocketException ex ) {
			log.error( ex );
		}
		catch ( Exception ex ) {
			log.error( ex );
		}

		return locals;
	}

	/**
	 * Gets local address represented in IPv4 and IPv6 as a map.
	 * 
	 * @param localAddresses
	 *            Stores local IP address in IPv4 and IPv6.
	 * @return Map holding the local IP address in those representations.
	 */
	private static Map<String, String> getLocalIPAddressMap(String[] localAddresses) {

		Map<String, String> localMap = new HashMap<String, String>();

		if ( isIPv4( localAddresses[0] ) ) {
			localMap.put( V4, localAddresses[0] );
			localMap.put( V6, localAddresses[1] );
		}
		else {
			localMap.put( V4, localAddresses[1] );
			localMap.put( V6, localAddresses[0] );
		}

		log.debug( "localMap: " + localMap );
		return Collections.unmodifiableMap( localMap );
	}

	/**
	 * Checks if the parameter represents IPv4 address or not.
	 * 
	 * @param local
	 *            Local IP address.
	 * @return True if it's in IPv4, false otherwise which is assumed to be in IPv6.
	 */
	private static boolean isIPv4(String local) {

		return local.contains( new Period().getSeparator() );
	}

}
