package sfs.util.http;

import java.net.URL;

public interface ViewCreator {

	public static final String HTML_RESOUCE = "SFS.html";
	public static final String JAVASCRIPT_RESOURCE = "SFS.js";

	public byte[] create(String data, String node, URL url);
}
