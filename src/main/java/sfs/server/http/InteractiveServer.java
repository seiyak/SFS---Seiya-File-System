package sfs.server.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import sfs.client.http.manager.NodeManager;
import sfs.server.http.handler.GreetingHandler;
import sfs.server.http.handler.InspectNodeHandler;
import sfs.server.http.handler.ListActiveNodesHandler;
import sfs.server.http.handler.WelcomeHandler;
import sfs.util.http.JSoupViewCreator;
import sfs.util.http.ViewCreator;

public class InteractiveServer implements HttpServerable {

	// TODO Create a class hierarchy to enable Jetty and other web server apis usable with this class without any
	// modification.
	private final String host;
	private final int port;
	private final int backLog;
	private com.sun.net.httpserver.HttpServer server;
	private final ViewCreator viewCreator;
	private final NodeManager nodeManager;
	private static final int DEFAULT_STOP_DELAY = 0;

	public InteractiveServer(String host, int port) {

		this.host = host;
		this.port = port;
		this.backLog = 0;
		viewCreator = new JSoupViewCreator();
		nodeManager = new NodeManager();
	}

	public InteractiveServer(String host, int port, int backLog) {

		this.host = host;
		this.port = port;
		this.backLog = backLog;
		viewCreator = new JSoupViewCreator();
		nodeManager = new NodeManager();
	}

	public InteractiveServer(String host, int port, int backLog, ViewCreator viewCreator) {

		this.host = host;
		this.port = port;
		this.backLog = backLog;
		this.viewCreator = viewCreator;
		nodeManager = new NodeManager();
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

	public ViewCreator getViewCreator() {
		return viewCreator;
	}

	protected final com.sun.net.httpserver.HttpServer getServer() {

		return server;
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
			setContextPaths();
			server.setExecutor( Executors.newCachedThreadPool() );
		}
		catch ( IOException ex ) {
			closeIfNotNull();
			throw ex;
		}
	}

	/**
	 * Sets context paths.
	 */
	protected void setContextPaths() {
		
		server.createContext( "/sfs", new WelcomeHandler(viewCreator) );
		server.createContext( "/greeting", new GreetingHandler( nodeManager ) );
		server.createContext( "/inspect/node", new InspectNodeHandler( viewCreator ) );
		server.createContext("/list/activeNodes", new ListActiveNodesHandler(nodeManager));
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
