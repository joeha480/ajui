package com.googlecode.ajui;

public class ADefinitionDescription extends AbstractComponent<AInlineComponent> implements ADefinitionListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187309392723303218L;

	
	public ADefinitionDescription() {
		super();
	}
	
	@Override
	protected String getTagName() {
		return "dd";
	}
	
}