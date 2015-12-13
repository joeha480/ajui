package com.googlecode.ajui;

import java.util.Date;
import java.util.List;

public abstract class AbstractElement implements AComponent {

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasUpdates(Date since) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasIdentifer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends AComponent> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XHTMLTagger getHTML(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
