package com.googlecode.ajui;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Provides a tiny web server which acts as a GUI for a java application.
 * @author Joel Håkansson
 */
public class BrowserUI {
	private final static Logger logger = Logger.getLogger(BrowserUI.class.getCanonicalName());
	private final static String ROOT_PAGE = "/index.html";
    private final static int SERVER_TIMEOUT = 30000;
    private final static int DEFAULT_TIMEOUT = 5000;
    private final static int MIN_TIMEOUT = 1000;
    private final static int MIN_APP_TIMEOUT = 1000;

    private ServerSocket ss;
    private boolean running = false;
    private boolean stop;
    private final SocketFilter socketFilter;
    private final boolean acceptRemoteConnections;

    private final PrintStream log;
    
    /* */
    private final int appTimeout;
    /* the web server's virtual root */
    private final String root;
    /* Where worker threads stand idle */
    Vector<Worker> threads = new Vector<>();
    /* Contents catalog */
    private final Map<String, Content> contents;
    /* timeout on client connections */
    final int timeout;
    /* max # worker threads */
    final int workers = 5;
    
    /**
     * Provides a builder for the BrowserUI
     * @author Joel Håkansson
     */
    public static class Builder {
    	// required params
    	private final String root;
    	// optional params
    	private boolean acceptRemoteConnections;
    	private int appTimeout;
    	private int timeout;
    	private PrintStream log = System.out;
    	
    	/**
    	 * Creates a new builder with the supplied relative path as server root.
    	 * @param root the server root path
    	 */
    	public Builder(String root) {
    		this.root = root;
    		this.acceptRemoteConnections = false;
    		this.appTimeout = SERVER_TIMEOUT;
    		this.timeout = DEFAULT_TIMEOUT;
    	}
    	public Builder acceptRemoteConnections(boolean value) {
        	acceptRemoteConnections = value;
        	return this;
        }
    	public Builder timeout(int value) {
            if (value < MIN_TIMEOUT) {
            	throw new IllegalArgumentException("Timeout too small " + value + "<" + MIN_TIMEOUT);
            }
            this.timeout = value;
    		return this;
    	}
    	/**
    	 * Sets the application timeout. If no interaction has occurred within this time, 
    	 * the application will shut down. It is recommended that the GUI regularly
    	 * contacts the server to keep it alive instead of changing this value. Polling the server
    	 * will ensure that the application exits within a reasonable time after the user
    	 * has closed the browser.
    	 * @param value the timeout, in milliseconds
    	 * @return returns this object
    	 */
    	public Builder applicationTimeout(int value) {
            if (value < MIN_APP_TIMEOUT) {
            	throw new IllegalArgumentException("Timeout too small " + value + "<" + MIN_APP_TIMEOUT);
            }
    		this.appTimeout = value;
    		return this;
    	}
    	public Builder logStream(PrintStream value) {
    		this.log = value;
    		return this;
    	}
    	public BrowserUI build() {
    		return new BrowserUI(this);
    	}
    }

    private BrowserUI(Builder builder) {
    	this.root = builder.root;
    	this.acceptRemoteConnections = builder.acceptRemoteConnections;
    	if (acceptRemoteConnections) {
    		this.socketFilter = new TransparentSocketFilter();
    	} else {
    		this.socketFilter = new LocalhostSocketFilter();
    	}
    	this.timeout = builder.timeout;
    	this.log = builder.log;
    	this.appTimeout = builder.appTimeout;
    	this.contents = Collections.synchronizedMap(new HashMap<String, Content>());
    }

    /* print to the log file */
    void log(String s) {
        synchronized (log) {
            log.println(s);
            log.flush();
        }
    }

    void printProps() {
    	logger.fine("root="+root);
    	logger.fine("workers="+workers);
    	logger.fine("socket timeout="+timeout);
    	logger.fine("application timeout="+appTimeout);
    	logger.fine("accept remote connections=" + acceptRemoteConnections);
    }

    void stopServer() {
    	
		log("Closing server...");
		Runnable r = new Runnable(){

			@Override
			public void run() {
				try {
					//wait a couple of seconds in order for the last resources to be fetched.
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				stop=true;
			}};

		new Thread(r).start();

		
    }
    
   void startServer() throws IOException {
    	if (!running) {
	        int port = 0; // any free port
	        printProps();

	        /* start worker threads */
	        for (int i = 0; i < workers; ++i) {
	            Worker w = new Worker(this);
	            (new Thread(w, "worker #"+i)).start();
	            threads.addElement(w);
	        }
	        
	        ss = new ServerSocket(port);
	        // avoid port 2049 if any free port is used (0)
	        while (port==0 && ss.getLocalPort()==2049) {
	        	ss.close();
	        	// try again
	        	ss = new ServerSocket(0);
	        }
	        
	        ss.setSoTimeout(appTimeout);
	        logger.fine("port=" + ss.getLocalPort());

	        running=true;
	        ServerThread server = new ServerThread(this);
    		(new Thread(server)).start();
    	}
    }
    
    int getPort() {
    	if (running) {
    		return ss.getLocalPort();
    	}
    	return -1;
    }
    
    public synchronized Content getContents(String key) {
    	return contents.get(key);
    }
    
    /**
     * Registers a configured instance for later retrieval 
     * @param c the instance to register
     */
    public synchronized void registerContents(Content c) {
    	String key = c.getClass().getCanonicalName();
    	if (key != null) {
    		if (contents.containsKey(key)) {
    			throw new IllegalStateException("Content already registered");
    		}
    		contents.put(key, c);
    	} else {
    		throw new IllegalArgumentException("Cannot get canonical name for " + c);
    	}
    }
    
    public void display(String page) {
    	if (!running) {
    		try {
    			startServer();
    		} catch (IOException e) {
    			throw new RuntimeException("Failed to start server.", e);
    		}
    	}
        String url = "http://localhost:" + getPort()  + "/" + page;
        log("Launching " + url);
        BareBonesBrowserLaunch.openURL(url);
    }
    
    private class ServerThread implements Runnable {
    	
    	private final BrowserUI ui;
    	public ServerThread(BrowserUI ui) {
    		this.ui = ui;
    	}

		@Override
		public void run() {
			try {
		        stop=false;
		        while (!stop) {
		        	try {
		        		Socket s = ss.accept();
		        		if (!socketFilter.accept(s)) {
		        			System.err.println("Ignored connection attempt from: " + s.getInetAddress());
		        			continue;
		        		}
		        		if (!stop) {
			                Worker w = null;
			                synchronized (threads) {
			                    if (threads.isEmpty()) {
			                        Worker ws = new Worker(ui);
			                        ws.setSocket(s);
			                        (new Thread(ws, "additional worker")).start();
			                    } else {
			                        w = threads.elementAt(0);
			                        threads.removeElementAt(0);
			                        w.setSocket(s);
			                    }
			                }
		        		}
		        	} catch (SocketTimeoutException e) {
		        		log("Socket timeout.");
		        		if (!stop) {
		        			stop=true;
		        		}
		        	}
		        }
		        synchronized (contents) {
		        	for (Content c : contents.values()) {
		        		c.close();
		        	}
		        }
		        synchronized (threads) {
		        	for (Worker w2 : threads) {
		        		w2.stop();
		        	}
				}
		        ss.close(); 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				synchronized (threads) {
					threads = null;
				}
		        running = false;
		        log("Server closed.");				
			}
		}
    }

    Context getContext(String urlStr) {
    	return new ContextImpl(this, urlStr, ROOT_PAGE);
    }
    
    URL getResourceURL(String target) {
    	return ClassLoader.getSystemResource(root + target);
    }

}