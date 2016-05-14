package com.googlecode.ajui.comp;

import com.googlecode.ajui.ABlockComponent;
import com.googlecode.ajui.AContainer;
import com.googlecode.ajui.ALabel;
import com.googlecode.ajui.ALink;
import com.googlecode.ajui.AParagraph;
import com.googlecode.ajui.ASpan;


public class ATabbedContainer extends AContainer {
	private final AParagraph menu;
	private final AContainer container;
	
	public ATabbedContainer() {
		super();
		menu = new AParagraph();
		container = new AContainer();
		super.add(menu);
		super.add(container);
	}

	@Override
	public boolean add(ABlockComponent e) {
		return addTab("Tab", e);
	}
	
	public boolean addTab(String title, ABlockComponent component) {
		ALink link = new ALink();
		link.add(new ALabel(title));
		link.addGetListener(source->{
			synchronized (container) {
				container.removeAll();
				container.add(component);
			}
		});
		synchronized (container) {
			if (container.getChildren().isEmpty()) {
				container.add(component);
			}			
		}
		ASpan item = new ASpan();
		item.setClass("menu");
		item.add(link);
		return menu.add(item);
	}

	@Override
	public boolean remove(ABlockComponent e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ABlockComponent remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAll() {
		menu.removeAll();
		synchronized (container) {
			container.removeAll();
		}
	}	
	
}
