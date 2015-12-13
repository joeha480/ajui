package com.googlecode.ajui;



public class AParagraph extends AbstractComponent<AInlineComponent> implements ABlockComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6583904736227125917L;

	public AParagraph() {
		super();
	}

	@Override
	protected String getTagName() {
		return "p";
	}

}
