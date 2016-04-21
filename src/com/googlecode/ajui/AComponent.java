package com.googlecode.ajui;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface AComponent {
	
	public void update();
	public boolean hasUpdates(Date since);
	public boolean hasIdentifer();
	public String getIdentifier();
	public List<? extends AComponent> getChildren();
	public XHTMLTagger getHTML(Context context);
	public boolean processEvent(String key, String type, Map<String, String> data);

}
