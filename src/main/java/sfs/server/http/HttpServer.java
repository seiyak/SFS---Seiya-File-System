package sfs.server.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import sfs.server.http.handler.HttpServerHandler;

public class HttpServer {

	// TODO Create a class hierarchy to enable Jetty and other web server apis usable with this class without any
	// modification.
	private final String host;
	private final int port;
	private final int backLog;
	private final String contextPath;
	private com.sun.net.httpserver.HttpServer server;
	private static final int DEFAULT_STOP_DELAY = 0;

	public HttpServer(String host, int port) {

		this.host = host;
		this.port = port;
		this.backLog = 0;
		contextPath = "/";
	}

	public HttpServer(String host, int port, int backLog) {

		this.host = host;
		this.port = port;
		this.backLog = backLog;
		contextPath = "/";
	}

	public HttpServer(String host, int port, int backLog, String contextPath) {

		this.host = host;
		this.port = port;
		this.backLog = backLog;
		this.contextPath = contextPath;
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

	public String getContextPath() {
		return contextPath;
	}

	/**
	 * Starts web server.
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {

		configure();
		server.start();
	}

	/**
	 * Configures HTTP server settings based on the properties.
	 * 
	 * @throws IOException
	 *             When invalid configurations are supplied.
	 */
	private void configure() throws IOException {

		try {
			server = com.sun.net.httpserver.HttpServer.create( new InetSocketAddress( host, port ), backLog );
			server.createContext( contextPath, new HttpServerHandler() );
			server.setExecutor( Executors.newCachedThreadPool() );
		}
		catch ( IOException ex ) {
			closeIfNotNull();
			throw ex;
		}
	}

	/**
	 * Stops web server.
	 */
	public void stop() {
		closeIfNotNull();
	}

	/**
	 * Stops when server property is not null.
	 */
	private void closeIfNotNull() {

		if ( server != null ) {
			server.stop( DEFAULT_STOP_DELAY );
		}
	}
}
