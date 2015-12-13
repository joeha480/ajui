package com.googlecode.ajui;

import java.net.Socket;

class TransparentSocketFilter implements SocketFilter {

	@Override
	public boolean accept(Socket s) {
		return true;
	}

}
