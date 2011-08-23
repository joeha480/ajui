package com.googlecode.ajui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractComponent<T extends AComponent> extends ArrayList<T> implements AComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1238120276336545065L;
	protected List<String> classes;
	protected Map<String, String> attrs;
	protected String id;
	
	public AbstractComponent() {
		this.classes = new ArrayList<String>();
		this.id = null;
		this.attrs = new HashMap<String, String>();
	}

	protected abstract String getTagName();


	/**
	 * @return the classes
	 */
	public List<String> getClasses() {
		return classes;
	}

	/**
	 * @param classes the classes to set
	 */
	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String addAttribute(String key, String value) {
		return attrs.put(key, value);
	}
	
	public void setClass(String value) {
		classes.add(value);
	}

	public String removeAttribute(String key) {
		return attrs.remove(key);
	}

	@Override
	public XHTMLTagger getHTML(Context context) {
		XHTMLTagger tagger = new XHTMLTagger();
		tagger.start(getTagName());
		StringBuilder classes = new StringBuilder();
		boolean first = true;
		for (String key : attrs.keySet()) {
			tagger.attr(key, attrs.get(key));
		}
		for (String s : getClasses()) {
			if (!first) {
				classes.append(" ");
			} else {
				first = false;
			}
			classes.append(s);
		}
		if (getId()!=null && !getId().equals("")) {
			tagger.attr("id", getId());
		}
		if (classes.length()>0) {
			tagger.attr("class", classes.toString());
		}
		for (AComponent c : this) {
			tagger.insert(c.getHTML(context));
		}
		tagger.end();
		return tagger;
	}

}
