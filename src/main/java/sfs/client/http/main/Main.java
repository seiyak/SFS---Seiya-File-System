package sfs.client.http.main;

import java.io.IOException;

import sfs.client.http.AbstractClient;
import sfs.client.http.MultiplexHTTPClient;
import sfs.entry.HostEntry;

public class Main {

	public static void main(String[] args) throws IOException {
		
		if(args.length != 2){
			System.out.println("usage: Main host port");
			System.exit(0);
		}
		
		AbstractClient client = new MultiplexHTTPClient( new HostEntry( args[0], Integer.parseInt( args[1] ) ) );

		client.initiate();
		client.close();
	}
}
