package com.googlecode.ajui;

import java.io.IOException;
import java.io.Reader;

/**
 * Provides an interface for creating dynamic content in java, which can be injected into
 * an html page running in the context of the application. To inject content into an 
 * html page, add a processing instruction to the html page:
 *  <pre>&lt;?ajui [qualified name of a class implementing this interface] [key, optional]?&gt;</pre>
 *  for example:
 *  <pre>&lt;?ajui org.example.MyContentImpl?&gt;</pre>
 *  <pre>&lt;?ajui org.example.MyContentImpl getTitle?&gt;</pre>
 * @author Joel Håkansson
 */
public interface Content {

	/**
	 * Gets the content for key using the supplied context.
	 * @param key the key supplied in the processing instruction 
	 * @param context the calling context
	 * @return returns the content in a Reader
	 * @throws IOException if IO fails
	 */
	public Reader getContent(String key, Context context) throws IOException;
	
	/**
	 * Notifies about pending shut down.
	 */
	public void close();
}
