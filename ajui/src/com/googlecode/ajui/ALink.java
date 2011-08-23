package com.googlecode.ajui;


public class ALink extends AbstractComponent<AInlineComponent> implements AInlineComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187309392723303218L;
	
	public ALink(String href) {
		super();
		attrs.put("href", href);
	}

	@Override
	protected String getTagName() {
		return "a";
	}
	
	
}
