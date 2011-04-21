package com.google.code.ajui;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

class ContextImpl implements Context {
	public static String ENCODING = "utf-8";
	private final HashMap<String, String> args;
	private final String target;
	private final BrowserUI ui;

	ContextImpl(BrowserUI ui, String urlStr, String root) {
		this.ui = ui;
		String[] fsplit = urlStr.split("\\?"); //$NON-NLS-1$
        String targetT = fsplit[0];

        if (fsplit.length>1) {
        	args = parseArgs(fsplit[1]);
        } else {
        	args = new HashMap<String, String>();
        }
        if ("/".equals(targetT)) {
        	targetT = root;
        }
        this.target = targetT;
	}

	public HashMap<String, String> getArgs() {
		return args;
	}

	public String getTarget() {
		return target;
	}
	
    private HashMap<String, String> parseArgs(String argStr) {
    	HashMap<String, String> args = new HashMap<String, String>();
       	String[] argSplit = argStr.split("&"); //$NON-NLS-1$
    	for (String s : argSplit) {
    		String[] argVal = s.split("="); //$NON-NLS-1$
    		if (argVal.length>1) {
    			try {
					args.put(argVal[0], URLDecoder.decode(argVal[1], ENCODING));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("Unsupported encoding: '" +  ENCODING + "'", e);
				}
    		}
    		else {
    			args.put(argVal[0], ""); //$NON-NLS-1$
    		}
    	}
    	return args;
    }
/*
	@Override
	public WebUI getUI() {
		return ui;
	}
*/
	@Override
	public void log(String msg) {
		ui.log(msg);
	}

	@Override
	public void close() {
		ui.stopServer();
	}

}
