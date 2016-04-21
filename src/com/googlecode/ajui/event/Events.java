package com.googlecode.ajui.event;

public interface Events {

	/**
	 * Prefix used for request arguments that should be passed to the event system
	 */
	final static String EVENT_PREFIX = "ajui-";
	/**
	 * The event type
	 */
	final static String EVENT_KEY = EVENT_PREFIX + "event";
	/**
	 * The component that triggered the event
	 */
	final static String SENDER_KEY = EVENT_PREFIX + "sender";
	final static String GET_EVENT = "get";
}
