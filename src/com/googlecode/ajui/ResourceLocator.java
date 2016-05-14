package com.googlecode.ajui;

import java.net.URL;

class ResourceLocator {

	URL getResource(String path) {
		URL url;
	    url = this.getClass().getResource(path);
	    if(null==url) {
	    	String qualifiedPath = this.getClass().getPackage().getName().replace('.','/') + "/";	    	
	    	url = this.getClass().getClassLoader().getResource(qualifiedPath+path);
	    }
	    return url;
	}
}
