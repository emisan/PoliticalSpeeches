package com.pcand.kayaman.http.server;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpHost;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.protocol.HttpCoreContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CSVResourceServer is a Apache httpcomponents based HttpServer which sends politicla speech resources in a CSV-File
 * when an HTTP Client does do a request to this server.
 * 
 * The default resource folder of this server is the src/main/resources-folder of the SpringApplication
 * 
 * @author Hayri Emrah Kayaman
 */
public class CSVResourceServer {
	
	private static final int DEFAULT_HTTP_CLIENT_BUFFER = 8 * 1024;
	
	private static DefaultBHttpClientConnection defaultClientConnection = new DefaultBHttpClientConnection(DEFAULT_HTTP_CLIENT_BUFFER);
	private static ConnectionReuseStrategy connectionReuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
	
	private HttpCoreContext httpCoreContext;
	private HttpHost httpHost;

	@Autowired
	private InternalResourceServer internalResourceServer;
	
	public CSVResourceServer() {
		
	}
}
