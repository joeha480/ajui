package com.googlecode.ajui;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
	private final String javascript;
	private final String xmlTarget;

	public APage() {
		this(null);
	}
	
	public APage(AContainer view) { 
		this.view = view;
		this.title = "";
		this.bodyAttributes = Collections.synchronizedMap(new HashMap<>());
		this.stylePaths = Collections.synchronizedList(new ArrayList<>());
		this.scriptPaths = Collections.synchronizedList(new ArrayList<>());
		try {
			ResourceLocator l = new ResourceLocator();
			InputStream res = l.getResource("resource-files/update.js").openStream();
			int c;
			StringBuilder sb = new StringBuilder();
			while ((c=res.read())>-1) {
				sb.append((char)c);
			}
			this.javascript = "/* <![CDATA[  */ \n" + sb.toString() +
					"\nfunction ping() {"
					+ "get(\""+this.getClass().getCanonicalName()+".xml\");"
					+ "}"
					+ "\n /*  ]]> */";
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.xmlTarget = "/" + this.getClass().getCanonicalName() + ".xml";
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
		if (context.getTarget().equals(xmlTarget)) {
			String type = context.getArgs().get("type");
			if ("update".equals(type)) {
				//get update
				return new StringReader("<xml></xml>");
			} else if ("event".equals(type)) {
				//process events
				processEvent(context.getArgs());
				return new StringReader("<ok>"+new Date()+"</ok>");
			} else {
				return new StringReader("<xml/>");
			}
		} else {
			processEvent(context.getArgs());
			XHTMLTagger sb = new XHTMLTagger();
			sb.start("html").attr("xmlns", "http://www.w3.org/1999/xhtml")
			.start("head")
				.start("meta").attr("http-equiv", "content-type").attr("content", "text/html; charset=UTF-8").end()
				.start("title").text(title).end();
			for (String style : stylePaths) {
				sb.start("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", style).end();
			}
			insertScript(sb);
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
	}
	
	private void insertScript(XHTMLTagger sb) {
		sb.start("script").attr("type", "text/javascript");
		sb.text(javascript, false);
		sb.end();
	}

	@Override
	public void close() {
	}
}
