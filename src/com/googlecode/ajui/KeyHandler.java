package com.googlecode.ajui;

enum KeyHandler {
	INSTANCE;
	private long i=0;

	synchronized String nextKey() {
		i++;
		return Long.toString(i, Character.MAX_RADIX);
	}
	
	static KeyHandler getInstance() {
		return INSTANCE;
	}

}