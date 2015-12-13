package com.googlecode.ajui;

import java.util.Date;
import java.util.List;

public class AOption implements AInlineComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187309392723303218L;
	private final String val;
	private final String displayName;
	private boolean isSelected;
	
	public AOption(String displayName, String val) {
		super();
		this.val = val;
		this.displayName = displayName;
		this.isSelected = false;
	}
	
	protected void setIsSelected(boolean value) {
		this.isSelected = value;
	}
	
	protected String getValue() {
		return val;
	}

	@Override
	public XHTMLTagger getHTML(Context context) {
		XHTMLTagger tagger = new XHTMLTagger();
    	tagger.start("option").attr("value", val);
    	if (isSelected) {
    		tagger.attr("selected", "selected");
    	}
    	tagger.text(displayName).end();
		return tagger;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends AComponent> getChildren() {
		return null;
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
	public void update() {
		// TODO Auto-generated method stub
		
	}

	
}