package com.googlecode.ajui.comp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.googlecode.ajui.ABlockComponent;
import com.googlecode.ajui.AComponent;
import com.googlecode.ajui.AInput;
import com.googlecode.ajui.AOption;
import com.googlecode.ajui.ASelect;
import com.googlecode.ajui.AbstractComponent;
import com.googlecode.ajui.event.Events;
import com.googlecode.ajui.event.GetEvent;
import com.googlecode.ajui.event.GetListener;

public class ASelectComponent extends AbstractComponent<AComponent> implements ABlockComponent {
	private final static String EVENT_SELECTED = Events.EVENT_PREFIX+"selected";
	private final ASelect select;
	
	public ASelectComponent() {
		addAttribute("method", "get");
		select = new ASelect();
		select.addAttribute("name", EVENT_SELECTED);
		select.addAttribute("onchange", "submit();");
		add(select);
		add(newHidden(Events.SENDER_KEY, key));
		add(newHidden(Events.EVENT_KEY, Events.GET_EVENT));
	}
	
	private AInput newHidden(String name, String value) {
		AInput input = new AInput();
		input.addAttribute("type", "hidden");
		input.addAttribute("name", name);
		input.addAttribute("value", value);
		return input;
	}

	public void addOption(String name, String value) {
		select.add(new AOption(name, value));
	}

	public void setSelected(String value) {
		select.setSelected(value);
	}
	
	public void addGetListener(GetListener listener) {
		ell.add(GetListener.class, listener);
	}
	
	public void removeGetListener(GetListener listener) {
		ell.remove(GetListener.class, listener);
	}
	
	public int getSelectedIndex() {
		return select.getSelectedIndex();
	}
	
	public String getSelected() { 
		return select.getSelected();
	}

	@Override
	protected String getTagName() {
		return "form";
	}
	
	@Override
	protected void processEventMatch(String type, Map<String, String> data) {
		if (type.equals(Events.GET_EVENT)) {
			String v = data.get(EVENT_SELECTED);
			if (v!=null) {
				try {
					v = URLDecoder.decode(v, StandardCharsets.UTF_8.name());
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				select.setSelected(v);
			}
			GetListener[] ls = ell.getListeners(GetListener.class);
			for (GetListener l : ls) {
				l.getPerformed(new GetEvent(this));
			}
		} else {
			System.err.println("Event type not supported " + type);
		}
	}

}