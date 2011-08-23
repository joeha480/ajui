package com.googlecode.ajui;



public class APre extends AbstractComponent<AInlineComponent> implements ABlockComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6583904736227125917L;

	public APre() {
		super();
	}

	@Override
	protected String getTagName() {
		return "pre";
	}

}
