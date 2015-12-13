package com.googlecode.ajui;

public class AListItem extends AbstractComponent<AInlineComponent> implements AInlineComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187309392723303218L;

	
	public AListItem() {
		super();
	}


	@Override
	protected String getTagName() {
		return "li";
	}
	
}