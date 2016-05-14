package com.googlecode.ajui;



public class ASelect extends AbstractComponent<AOption> implements AInlineComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6583904736227125917L;
	private int selectedIndex = -1;
	private AOption selectedObject = null;

	public ASelect() {
		super();
	}
	
	public void setSelected(String val) {
		int i = 0;
		for (AOption o : getChildren()) {
			if (o.getValue().equals(val)) {
				selectedIndex = i;
				selectedObject = o;
				o.setIsSelected(true);
			} else {
				o.setIsSelected(false);
			}
			i++;
		}
	}
	
	public String getSelected() {
		return selectedObject.getValue();
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}

	@Override
	protected String getTagName() {
		return "select";
	}

}
