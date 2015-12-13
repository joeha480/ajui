package com.googlecode.ajui;



public class AContainer extends AbstractComponent<ABlockComponent> implements ABlockComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6583904736227125917L;

	public AContainer() {
		super();
	}

	@Override
	protected String getTagName() {
		return "div";
	}

}
