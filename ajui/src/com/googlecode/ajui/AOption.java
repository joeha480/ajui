package com.googlecode.ajui;

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
	
}