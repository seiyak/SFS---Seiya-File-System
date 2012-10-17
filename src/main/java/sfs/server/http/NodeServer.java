package sfs.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import sfs.concatenable.usage.cpu.CPUUsage;
import sfs.concatenable.usage.memory.MemoryUsage;
import sfs.header.http.HTTPHeader;
import sfs.header.http.RequestHeaderEntry;
import sfs.header.http.RequestType;
import sfs.header.http.ResponseHeaderEntry;
import sfs.header.http.separator.Colon;
import sfs.header.http.separator.WhiteSpace;
import sfs.mime.Mime;
import sfs.response.http.ResponseMessage;
import sfs.response.statuscode.StatusCode;
import sfs.usage.cpu.CPU;
import sfs.usage.cpu.LinuxCPU;

public class NodeServer implements Runnable {

	private ServerSocket server;
	private final String host;
	private final int port;
	private final int backLog;
	private final CPU cpu;
	private static Logger log = Logger.getLogger( NodeServer.class );

	public NodeServer(String host, int port, int backLog) {

		this.host = host;
		this.port = port;
		this.backLog = backLog;
		cpu = new LinuxCPU();
		
		initialize();
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getBackLog() {
		return backLog;
	}

	private void initialize() {
		try {
			server = new ServerSocket( port, backLog );
		}
		catch ( IOException ex ) {
			log.error( ex );
		}
	}

	public void start() {

		while ( true ) {
			BufferedReader reader = null;
			try {
				log.info( "listening client at port, " + port );
				Socket socket = server.accept();
				reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
				String str = "";
				while ( ( str = reader.readLine() ) != null ) {
					if ( str.startsWith( RequestHeaderEntry.TYPE.toString() ) ) {
						handleRequest( str.split( new Colon().add( new WhiteSpace() ).toString() )[1],
								socket.getOutputStream() );
						break;
					}
				}
			}
			catch ( IOException ex ) {
				log.error( ex );
			}
			finally {
				if ( reader != null ) {
					try {
						reader.close();
					}
					catch ( IOException ex ) {
						log.error( ex );
					}
				}
			}
		}
	}

	private void handleRequest(String requestType, OutputStream out) throws IOException {

		try {
			if ( requestType.equals( RequestType.CPU_MEMORY_USAGE.toString() ) ) {
				sendCPUMemoryUsage( out );
			}
		}
		catch ( IOException ex ) {
			throw ex;
		}
		finally {
			out.close();
		}
	}

	private void sendCPUMemoryUsage(OutputStream out) throws IOException {

		ResponseMessage responseMessage = new ResponseMessage();
		String str = responseMessage.response( StatusCode._200 );

		HTTPHeader header = new HTTPHeader();
		header.put( ResponseHeaderEntry.CONTENT_TYPE, Mime.JSON );
		header.put( ResponseHeaderEntry.DATE, getTimeInGMT() );

		String usage = new MemoryUsage().add( new CPUUsage(cpu) ).getJson();

		header.put( ResponseHeaderEntry.CONTENT_LENGTH, usage.length() );

		str += header.format();
		str += usage;

		write( str, out );
	}

	/**
	 * From http://stackoverflow.com/questions/308683/how-can-i-get-the-current-date-and-time-in-utc-or-gmt-in-java
	 * 
	 * @return
	 */
	private String getTimeInGMT() {
		Calendar c = Calendar.getInstance();

		TimeZone z = c.getTimeZone();
		int offset = z.getRawOffset();
		if ( z.inDaylightTime( new Date() ) ) {
			offset = offset + z.getDSTSavings();
		}
		int offsetHrs = offset / 1000 / 60 / 60;
		int offsetMins = offset / 1000 / 60 % 60;

		c.add( Calendar.HOUR_OF_DAY, ( -offsetHrs ) );
		c.add( Calendar.MINUTE, ( -offsetMins ) );

		return c.getTime().toString();

	}

	private void write(String content, OutputStream out) throws IOException {

		try {
			log.debug( "about to respond with: " + content );
			out.write( content.getBytes() );
		}
		finally {
			out.close();
		}
	}

	public void stop() throws IOException {
		if ( server != null ) {
			server.close();
		}
	}

	public void run() {

		start();
	}
}
