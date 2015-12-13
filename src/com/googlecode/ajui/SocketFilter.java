package com.googlecode.ajui;

import java.net.Socket;

interface SocketFilter {

	public boolean accept(Socket s);
}
