package com.google.code.ajui;

/**
 * Provides a simple XHTML tagger
 * @author Joel Håkansson
 */
public class XHTMLTagger extends XMLTagger {
	/**
	 * Defines the HTTP request method
	 */
	public enum RequestMethod {
		/**
		 * HTTP GET
		 */
		GET,
		/**
		 * HTTP POST
		 */
		POST
		};
	private String cval = "";
	
	public XHTMLTagger(StringBuffer sb) {
		super(sb);
	}
	
	public XHTMLTagger() {
		super();
	}
	
	public XHTMLTagger start(String name) {
		return (XHTMLTagger)super.start(name);
	}
	
	public XHTMLTagger end() {
		return (XHTMLTagger)super.end();
	}
	
	public XHTMLTagger text(String text) {
		return (XHTMLTagger)super.text(text);
	}
	
	public XHTMLTagger attr(String name, String value) {
		return (XHTMLTagger)super.attr(name, value);
	}

	/**
	 * Starts a select tag, storing the current value for setting "selected" when adding option
	 * @param currentValue
	 * @return returns this object
	 */
	public XHTMLTagger startSelect(String currentValue) {
		start("select");
		this.cval = currentValue;
		return this;
	}
	
	/**
	 * Adds a tag with the supplied text content
	 * @param element the element name
	 * @param contents the text contents
	 * @return returns this object
	 */
	public XHTMLTagger tag(String element, String contents) {
		start(element);
		text(contents);
		end();
		return this;
	}
	
	public XHTMLTagger startForm(String action, RequestMethod method) {
		start("form").attr("action", action).attr("method", method.toString().toLowerCase());
		return this;
	}
	
	public XHTMLTagger startForm(String action) {
		return startForm(action, RequestMethod.GET);
	}
	
    public void addOption(String displayName, String val) {
    	start("option").attr("value", val);
    	if (val.equals(cval)) {
    		attr("selected", "selected");
    	}
    	text(displayName);
    	end();
    }

}
