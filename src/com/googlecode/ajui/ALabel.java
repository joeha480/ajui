package com.googlecode.ajui;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ALabel implements AInlineComponent {
	private String text;
	private Date update;
	private Date lastUpdated;

	public ALabel(String text) {
		setText(text);
	}
	
	public void setText(String text) {
		this.text = text;
		this.update = new Date();
		needsUpdate();
	}

	@Override
	public XHTMLTagger getHTML(Context context) {
		return new XHTMLTagger().text(text);
	}

	@Override
	public String getIdentifier() {
		return null;
	}

	@Override
	public List<? extends AComponent> getChildren() {
		return null;
	}

	@Override
	@Deprecated
	public boolean hasUpdates(Date since) {
		return update.after(since);
	}

	@Override
	public boolean hasIdentifer() {
		return false;
	}

	@Override
	@Deprecated
	public void update() {
		update = new Date();
	}

	@Override
	public boolean processEvent(String key, String type, Map<String, String> data) {
		return false;
	}
	
	private void needsUpdate() {
		lastUpdated = new Date();
	}

	@Override
	public boolean hasUpdate(Date since) {
		return lastUpdated.after(since);
	}
}
