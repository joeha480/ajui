package com.googlecode.ajui;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class LocalhostSocketFilter implements SocketFilter {

	@Override
	public boolean accept(Socket s) {
		try {
			for (InetAddress local : InetAddress.getAllByName("localhost")) {
				if (local.equals(s.getInetAddress())) {
					return true;
				}
			}
		} catch (UnknownHostException e) {
			// should not happen
		}
		return false;
	}

}
