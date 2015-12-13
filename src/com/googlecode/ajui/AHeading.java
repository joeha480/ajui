package com.googlecode.ajui;



public class AHeading extends AbstractComponent<AInlineComponent> implements ABlockComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6583904736227125917L;
	private final int level;

	public AHeading(int level) {
		super();
		if (level<1 || level>6) {
			throw new IllegalArgumentException("Level must be between 1 and 6");
		}
		this.level = level;
	}

	@Override
	protected String getTagName() {
		return "h"+level;
	}

}
