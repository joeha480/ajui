package com.googlecode.ajui;


public class AInput extends AbstractComponent<AInlineComponent> implements AInlineComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187309392723303218L;

	
	public AInput() {
		super();
	}


	@Override
	protected String getTagName() {
		return "input";
	}
	
}
