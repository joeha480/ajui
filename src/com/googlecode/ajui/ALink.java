package com.googlecode.ajui;

import java.util.Map;

import com.googlecode.ajui.event.Events;
import com.googlecode.ajui.event.GetEvent;
import com.googlecode.ajui.event.GetListener;


public class ALink extends AbstractComponent<AInlineComponent> implements AInlineComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187309392723303218L;
	
	/**
	 * Creates a new internal link
	 */
	public ALink() {
		super();
		attrs.put("href", "?"+
						Events.SENDER_KEY+"="+key+"&"+
						Events.EVENT_KEY+"="+Events.GET_EVENT);
	}
	
	/**
	 * Creates a new external link
	 * @param href the external link
	 */
	public ALink(String href) {
		super();
		attrs.put("href", href);
	}

	@Override
	protected String getTagName() {
		return "a";
	}

	public void addGetListener(GetListener listener) {
		ell.add(GetListener.class, listener);
	}
	
	public void removeGetListener(GetListener listener) {
		ell.remove(GetListener.class, listener);
	}
	
	@Override
	void processEventMatch(String type, Map<String, String> data) {
		if (type.equals(Events.GET_EVENT)) {
			GetListener[] ls = ell.getListeners(GetListener.class);
			for (GetListener l : ls) {
				l.getPerformed(new GetEvent(this));
			}
		} else {
			System.err.println("Event type not supported " + type);
		}
	}
	
}
