package com.googlecode.ajui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;

public abstract class AbstractComponent<T extends AComponent> implements AComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1238120276336545065L;
	private List<T> children; 
	protected final String key;
	protected List<String> classes;
	protected Map<String, String> attrs;
	protected String id;
	private Date updated;
	protected EventListenerList ell;
	
	public AbstractComponent() {
		this.children = new ArrayList<>();
		this.key = KeyHandler.getInstance().nextKey();
		this.classes = new ArrayList<>();
		this.id = null;
		this.attrs = new HashMap<>();
		this.updated = new Date();
		this.ell = new EventListenerList();
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
        @Override
	public String getIdentifier() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public synchronized void setIdentifier(String id) {
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
		if (getIdentifier()!=null && !getIdentifier().equals("")) {
			tagger.attr("id", getIdentifier());
		}
		if (classes.length()>0) {
			tagger.attr("class", classes.toString());
		}
		for (AComponent c : children) {
			tagger.insert(c.getHTML(context));
		}
		tagger.end();
		updated = new Date();
		return tagger;
	}
	
	public boolean add(T e) {
		return children.add(e);
	}
	
	public boolean remove(T e) {
		return children.remove(e);
	}
	
	public T remove(int index) {
		return children.remove(index);
	}
	
	public void removeAll() {
		children.clear();
	}
	
	@Override
	public List<T> getChildren() {
		return children;
	}

	@Override
	public boolean hasUpdates(Date since) {
		return updated.after(since);
	}

	@Override
	public boolean hasIdentifer() {
		return id!=null && !"".equals(id);
	}

	@Override
	public void update() {
		updated = new Date();
	}
	
	@Override
	public boolean processEvent(String sender, String type, Map<String, String> data) {
		if (sender.equals(key)) {
			processEventMatch(type, data);
			return true;
		} else {
			for (AComponent c : getChildren()) {
				if (c.processEvent(sender, type, data)) {
					return true;
				}
			}
			return false;
		}
	}
	
	protected void processEventMatch(String type, Map<String, String> data) {
	}

}
