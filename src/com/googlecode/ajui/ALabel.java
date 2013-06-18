package com.googlecode.ajui;

import java.util.Date;
import java.util.List;

public class ALabel implements AInlineComponent {
	private String text;
	private Date update;

	public ALabel(String text) {
		setText(text);
	}
	
	public void setText(String text) {
		this.text = text;
		this.update = new Date();
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
		//ArrayList<AComponent> ret = new ArrayList<AComponent>();
		//ret.add(this);
		return null;
	}

	@Override
	public boolean hasUpdates(Date since) {
		return update.after(since);
	}

	@Override
	public boolean hasIdentifer() {
		return false;
	}

	@Override
	public void update() {
		update = new Date();
	}
}
