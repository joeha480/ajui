package com.googlecode.ajui;

import java.net.Socket;
import java.util.regex.Pattern;

class LocalhostSocketFilter implements SocketFilter {
	final static Pattern[] patterns;
	
	static {
		patterns = new Pattern[3];
		patterns[0] = Pattern.compile("127\\.0\\.0\\.1");
		patterns[1] = Pattern.compile("0:0:0:0:0:0:0:1%0");
		patterns[2] = Pattern.compile("::1");
	}

	@Override
	public boolean accept(Socket s) {
		String addr = s.getInetAddress().getHostAddress();
		for (int i = 0; i<patterns.length; i++) {
			if (patterns[i].matcher(addr).matches()) {
				return true;
			}
		}
		return false;
	}

}
