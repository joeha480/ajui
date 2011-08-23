package com.googlecode.ajui;



public class ADefinitionList extends AbstractComponent<ADefinitionListItem> implements ABlockComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6583904736227125917L;

	public ADefinitionList() {
		super();
	}

	@Override
	protected String getTagName() {
		return "dl";
	}

}
