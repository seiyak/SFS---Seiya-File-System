package sfs.server.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import sfs.client.http.manager.NodeManager;
import sfs.entry.HostEntry;
import sfs.server.http.handler.GreetingHandler;
import sfs.server.http.handler.InspectNodeHandler;
import sfs.server.http.handler.ListActiveNodesHandler;
import sfs.server.http.handler.WelcomeHandler;
import sfs.structure.Node;
import sfs.structure.Structure;
import sfs.structure.StructureNode;
import sfs.util.http.JSoupViewCreator;
import sfs.util.http.ViewCreator;

public class InteractiveServer implements HttpServerable {

	// TODO Create a class hierarchy to enable Jetty and other web server apis usable with this class without any
	// modification.
	private final int backLog;
	private final HostEntry hostEntry;
	private com.sun.net.httpserver.HttpServer server;
	private final ViewCreator viewCreator;
	private final NodeManager<StructureNode> nodeManager;
	private static final int DEFAULT_STOP_DELAY = 0;

	public InteractiveServer(HostEntry hostEntry) {

		this.hostEntry = hostEntry;
		this.backLog = 0;
		viewCreator = new JSoupViewCreator();
		nodeManager = new NodeManager<StructureNode>( this.hostEntry );
	}

	public InteractiveServer(HostEntry hostEntry, int backLog) {

		this.hostEntry = hostEntry;
		this.backLog = backLog;
		viewCreator = new JSoupViewCreator();
		nodeManager = new NodeManager<StructureNode>( this.hostEntry );
	}

	public InteractiveServer(HostEntry hostEntry, int backLog, ViewCreator viewCreator) {

		this.hostEntry = hostEntry;
		this.backLog = backLog;
		this.viewCreator = viewCreator;
		nodeManager = new NodeManager<StructureNode>( this.hostEntry );
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
			server = com.sun.net.httpserver.HttpServer.create(
					new InetSocketAddress( hostEntry.getHost(), hostEntry.getPort() ), backLog );
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
