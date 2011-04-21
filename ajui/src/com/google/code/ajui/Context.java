package com.google.code.ajui;

import java.util.HashMap;

/**
 * Provides the context of a call to a Content implementation. 
 * @author Joel Håkansson
 */
public interface Context {
	/**
	 * Gets the HTML GET arguments 
	 * @return returns the arguments
	 */
	public HashMap<String, String> getArgs();
	/**
	 * Gets the target of the call, that is to say, the page called
	 * @return returns the target
	 */
	public String getTarget();
	/**
	 * Logs the provided method to the context logger
	 * @param msg the message to log
	 */
	public void log(String msg);
	/**
	 * Closes the application
	 */
	public void close();
}