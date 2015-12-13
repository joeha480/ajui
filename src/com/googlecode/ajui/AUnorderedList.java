package com.googlecode.ajui;



public class AUnorderedList extends AbstractComponent<AListItem> implements ABlockComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6583904736227125917L;

	public AUnorderedList() {
		super();
	}

	@Override
	protected String getTagName() {
		return "ul";
	}

}
