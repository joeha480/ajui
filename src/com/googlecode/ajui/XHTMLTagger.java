package com.googlecode.ajui;

/**
 * Provides a simple XHTML tagger
 * @author Joel Håkansson
 */
public class XHTMLTagger extends XMLTagger {
	
	public XHTMLTagger(StringBuffer sb) {
		super(sb);
		setShorthandCloseing(false);
	}
	
	public XHTMLTagger() {
		super();
		setShorthandCloseing(false);
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
	
	public XHTMLTagger insert(XHTMLTagger subtree) {
		return (XHTMLTagger)super.insert(subtree);
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
	
	public XHTMLTagger tag(String element, XHTMLTagger contents) {
		start(element);
		insert(contents);
		end();
		return this;
	}

}
