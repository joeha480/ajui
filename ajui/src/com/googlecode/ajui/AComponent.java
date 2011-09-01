package com.googlecode.ajui;

import java.util.Date;
import java.util.List;


public interface AComponent {
	
	public void update();
	public boolean hasUpdates(Date since);
	public boolean hasIdentifer();
	public String getIdentifier();
	public List<? extends AComponent> getChildren();
	public XHTMLTagger getHTML(Context context);

}
