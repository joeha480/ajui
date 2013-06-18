package com.googlecode.ajui;

public class ADefinitionTerm extends AbstractComponent<AInlineComponent> implements ADefinitionListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187309392723303218L;

	
	public ADefinitionTerm() {
		super();
	}
	

	@Override
	protected String getTagName() {
		return "dt";
	}
	
}