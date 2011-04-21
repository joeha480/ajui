package com.google.code.ajui;

import java.util.Stack;

/**
 * Provides a simple tagger for XML output.
 * @author Joel Håkansson
 */
public class XMLTagger {
	private StringBuffer sb;
	private Stack<String> elements;
	private boolean startOK;
	
	/**
	 * @param sb
	 */
	public XMLTagger(StringBuffer sb) {
		this.sb = sb;
		elements = new Stack<String>();
		startOK = true;
	}
	
	public XMLTagger() {
		this(new StringBuffer());
	}
	
	public XMLTagger start(String name) {
		if (!startOK) {
			sb.append(">");
		}
		elements.push(name);
		startOK = false;
		sb.append("<");
		sb.append(name);
		return this;
	}
	
	public XMLTagger end() {
		String end = elements.pop();
		if (!startOK) {
			sb.append("/>");
			startOK = true;
		} else {
    		sb.append("</");
    		sb.append(end);
    		sb.append(">");
		}
		return this;
	}
	
	public XMLTagger text(String text) {
    	if (!startOK) {
    		sb.append(">");
    		startOK = true;
    	}
		sb.append(escape(text));
		return this;
	}
	
	public XMLTagger attr(String name, String value) {
		if (!startOK) {
			sb.append(" ");
			sb.append(name);
			sb.append("=\"");
			sb.append(value);
			sb.append("\"");
		} else {
			throw new IllegalStateException("Cannot start attribute when contents has been written.");
		}
		return this;
	}
	
	public String getResult() {
		if (elements.size()>0) {
			throw new IllegalStateException("Result is not a valid sub tree");
		}
		return sb.toString();
	}

	private String escape(String text) {
		return text.replaceAll("&", "&amp;").replaceAll("<", "&lt;");
	}

}
