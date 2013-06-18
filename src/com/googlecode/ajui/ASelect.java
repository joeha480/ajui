package com.googlecode.ajui;



public class ASelect extends AbstractComponent<AOption> implements AInlineComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6583904736227125917L;

	public ASelect() {
		super();
	}
	
	public void setSelected(String val) {
		for (AOption o : this) {
			o.setIsSelected(o.getValue().equals(val));
		}
	}

	@Override
	protected String getTagName() {
		return "select";
	}

}
