package com.googlecode.ajui;

import java.net.Socket;

class LoopbackSocketFilter implements SocketFilter {

	@Override
	public boolean accept(Socket s) {
		return s.getInetAddress().isLoopbackAddress();
	}

}
