package com.googlecode.ajui;


public class AImage extends AbstractComponent<AInlineComponent> implements AInlineComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187309392723303218L;
	
	public AImage(String src) {
		super();
		attrs.put("src", src);
	}

	@Override
	protected String getTagName() {
		return "img";
	}
	
	
}
