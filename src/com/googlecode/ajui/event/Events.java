package com.googlecode.ajui.event;

public interface Events {

	/**
	 * Prefix used for request arguments that should be passed to the event system
	 */
	static final String EVENT_PREFIX = "ajui-";
	/**
	 * The event type
	 */
	static final String EVENT_KEY = EVENT_PREFIX + "event";
	/**
	 * The component that triggered the event
	 */
	static final String SENDER_KEY = EVENT_PREFIX + "sender";
	static final String GET_EVENT = "get";
}
