package com.googlecode.ajui;


public class ASpan extends AbstractComponent<AInlineComponent> implements AInlineComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187309392723303218L;

	
	public ASpan() {
		super();
	}


	@Override
	protected String getTagName() {
		return "span";
	}
	
}
