package com.googlecode.ajui.event;

import java.util.EventListener;

@FunctionalInterface
public interface GetListener extends EventListener {
	
	public void getPerformed(GetEvent e);
}
