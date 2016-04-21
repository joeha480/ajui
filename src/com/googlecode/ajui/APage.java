package com.googlecode.ajui;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.googlecode.ajui.event.Events;

public abstract class APage implements Content {
	private final static Logger logger = Logger.getLogger(APage.class.getCanonicalName());
	
	private AComponent view;
	private String title;
	private final Map<String, String> bodyAttributes;
	private final List<String> stylePaths;
	private final List<String> scriptPaths;

	public APage() {
		this(null);
	}
	
	public APage(AContainer view) { 
		this.view = view;
		this.title = "";
		this.bodyAttributes = Collections.synchronizedMap(new HashMap<>());
		this.stylePaths = Collections.synchronizedList(new ArrayList<>());
		this.scriptPaths = Collections.synchronizedList(new ArrayList<>());
	}
	
	public void setView(AComponent view) {
		this.view = view;
	}
	
	public AComponent getView() {
		return view;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void putBodyAttribute(String key, String value) {
		bodyAttributes.put(key, value);
	}
	
	public String removeBodyAttribute(String key) {
		return bodyAttributes.remove(key);
	}
	
	public void addStylePath(String path) {
		stylePaths.add(path);
	}
	
	public boolean removeStylePath(String path) {
		return stylePaths.remove(path);
	}
	
	public void addScriptPath(String path) {
		scriptPaths.add(path);
	}

	public boolean removeScriptPath(String path) {
		return scriptPaths.remove(path);
	}
	
	private void processEvent(Map<String, String> args) {
		String event = args.remove(Events.EVENT_KEY);
		if (event == null) {
			return;
		} else { 
			String sender = args.remove(Events.SENDER_KEY);
			if (sender==null) {
				return;
			} else {
				Map<String, String> data = new HashMap<>();
				for (String key : args.keySet()) {
					if (key.startsWith(Events.EVENT_PREFIX)) {
						data.put(key, args.get(key));
					}
				}
				view.processEvent(sender, event, data);
				for (String key : data.keySet()) {
					args.remove(key);
				}
			}
		}
	}
	
	@Override
	public Reader getContent(String key, Context context) throws IOException {
		processEvent(context.getArgs());
		XHTMLTagger sb = new XHTMLTagger();
		sb.start("html").attr("xmlns", "http://www.w3.org/1999/xhtml")
		.start("head")
			.start("meta").attr("http-equiv", "content-type").attr("content", "text/html; charset=UTF-8").end()
			.start("title").text(title).end();
		for (String style : stylePaths) {
			sb.start("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", style).end();
		}
		for (String script : scriptPaths) {
			sb.start("script").attr("src", script).end();
		}
		sb.end();
		sb.start("body");
		for (Entry<String, String> entry : bodyAttributes.entrySet()) {
			sb.attr(entry.getKey(), entry.getValue());
		}
		if (view!=null) {
			sb.insert(view.getHTML(context));
		}
    	sb.end();
    	sb.end();
		return new StringReader(sb.getResult());
	}

	@Override
	public void close() {
	}
}
