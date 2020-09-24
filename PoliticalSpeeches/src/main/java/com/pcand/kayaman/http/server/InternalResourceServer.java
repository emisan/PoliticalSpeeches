package com.pcand.kayaman.http.server;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

public class InternalResourceServer {

	// only needed for dev/test if we want access resource from classpath:src/main/resources
	// this has to be modified if we want to address servers at a URL who serve real resources
	//
	@Value("${csv.resource.url1}")
	private String csvAtRequestedParamUrl1;
	
	@Value("${csv.resource.url2}")
	private String csvAtRequestedParamUrl2;

	private ResourcePatternResolver resourcePatternResolver;
	
	public InternalResourceServer() {
		/*
		 *  We need to declare explicitly the default constructor of this class
		 *  in order to initialize instance member "resourcePatternResolver" with @PostConstruct during boot up of spring application
		 *  initializing the ClassLoader of this class to the resolver
		 *  
		 *  The ResourcePatternResolver is used in other components to reach Files from default src/main/resources folder. 
		 */
	}
	
	@PostConstruct
	public void init() {
		
		Assert.notNull(csvAtRequestedParamUrl1, "resource url1 to csv file must not be null!");
		Assert.notNull(csvAtRequestedParamUrl2, "resource url2 to csv file must not be null!");

		resourcePatternResolver = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
	}
	
	public Resource getResourceFromURL(String url) {
		return resourcePatternResolver.getResource(url);
	}
	
	public String postCSVResourceAtUrl1() {
		return csvAtRequestedParamUrl1;
	}
	
	public String postCSVResourceAtUrl2() {
		return csvAtRequestedParamUrl2;
	}
	
}
