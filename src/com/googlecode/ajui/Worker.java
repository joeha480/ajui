package com.googlecode.ajui;


import static com.googlecode.ajui.MimeTypes.MIME_TYPES;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.Vector;


class Worker implements HttpConstants, Runnable {
    final static int BUF_SIZE = 2048;
    final static String ENCODING = "utf-8";
    final static byte[] st = new byte[]{'<', '?', 'a', 'j', 'u', 'i', ' '};
    static final byte[] EOL = {(byte)'\r', (byte)'\n' };
    
    boolean stopped;
    /* buffer to use for requests */
    byte[] buf;
    /* Socket to client we're handling */
    private Socket s;
    private final BrowserUI ui;
    
    // constructor
    Worker(BrowserUI ui) {
    	this.ui = ui;
        buf = new byte[BUF_SIZE];
        s = null;
        stopped=false;
    }

    synchronized void setSocket(Socket s) {
        this.s = s;
        notify();
    }
    
    synchronized void stop() {
    	stopped=true;
    	notify();
    }

    @Override
    public synchronized void run() {
        while(!stopped) {
            if (s == null) {
                /* nothing to do */
                try {
                    wait();
                } catch (InterruptedException e) {
                    /* should not happen */
                    continue;
                }
                if (stopped) {
                	return;
                }
            }
            try {
                handleClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
            /* go back in wait queue if there's fewer
             * than numHandler connections.
             */
            s = null;
            Vector<Worker> pool = ui.threads;
            if (pool==null) {
            	return;
            }
            synchronized (pool) {
                if (pool.size() >= ui.workers) {
                    // too many threads, exit this one
                    return;
                } else {
                    pool.addElement(this);
                }
            }
        }
    }

    // Supported urls
    // http://localhost:[port]/ -> about
    // http://localhost:[port]/embosser-application.html -> app
    
    void handleClient() throws IOException {
        InputStream is = new BufferedInputStream(s.getInputStream());
        PrintStream ps = new PrintStream(s.getOutputStream(), false,  ENCODING); //$NON-NLS-1$
        /* we will only block in read for this many milliseconds
         * before we fail with java.io.InterruptedIOException,
         * at which point we will abandon the connection.
         */
        s.setSoTimeout(ui.timeout);
        s.setTcpNoDelay(true);
        
        /* zero out the buffer from last time */
        for (int i = 0; i < BUF_SIZE; i++) {
            buf[i] = 0;
        }
        try {
            /* We only support HTTP GET/HEAD, and don't
             * support any fancy HTTP options,
             * so we're only interested really in
             * the first line.
             */
            int nread = 0, r = 0;

outerloop:
            while (nread < BUF_SIZE) {
                r = is.read(buf, nread, BUF_SIZE - nread);
                if (r == -1) {
                    /* EOF */
                    return;
                }
                int i = nread;
                nread += r;
                for (; i < nread; i++) {
                    if (buf[i] == (byte)'\n' || buf[i] == (byte)'\r') {
                        /* read one line */
                        break outerloop;
                    }
                }
            }

            /* are we doing a GET or just a HEAD */
            boolean doingGet;
            /* beginning of file name */
            int index;
            if (buf[0] == (byte)'G' &&
                buf[1] == (byte)'E' &&
                buf[2] == (byte)'T' &&
                buf[3] == (byte)' ') {
                doingGet = true;
                index = 4;
            } else if (buf[0] == (byte)'H' &&
                       buf[1] == (byte)'E' &&
                       buf[2] == (byte)'A' &&
                       buf[3] == (byte)'D' &&
                       buf[4] == (byte)' ') {
                doingGet = false;
                index = 5;
            } else {
                /* we don't support this method */
                ps.print("HTTP/1.0 " + HTTP_BAD_METHOD + //$NON-NLS-1$
                           " unsupported method type: "); //$NON-NLS-1$
                ps.write(buf, 0, 5);
                ps.write(EOL);
                ps.flush();
                s.close();
                return;
            }

            int i = 0;
            /* find the file name, from:
             * GET /foo/bar.html HTTP/1.0
             * extract "/foo/bar.html"
             */
            for (i = index; i < nread; i++) {
                if (buf[i] == (byte)' ') {
                    break;
                }
            }
            String urlStr = (new String(buf, index, i-index));
            Context context = ui.getContext(urlStr);
            String target = context.getTarget();
            
            ui.log("Target: " + target); //$NON-NLS-1$
        	ui.log("Args:"); //$NON-NLS-1$
        	for (String f : context.getArgs().keySet()) {
        		ui.log(" " + f + " " + context.getArgs().get(f)); //$NON-NLS-1$ //$NON-NLS-2$
        	}
            if (doingGet) {
            	URL resource = ui.getResourceURL(target);
            	if (resource!=null) {
	            	InputStream iss = resource.openStream();
	        		if (iss!=null) {
	        			sendStream(context, new BufferedInputStream(iss), ps);
	        		} else {
	        			send404(target, ps);
	        		}
            	} else {
            		send404(target, ps);
            	}
            }
        } finally {
            s.close();
            is.close();
            ps.close();
        }
    }

    boolean printHeaders(File targ, PrintStream ps) throws IOException {
        boolean ret = false;
        int rCode = 0;
        if (!targ.exists()) {
            rCode = HTTP_NOT_FOUND;
            ps.print("HTTP/1.0 " + HTTP_NOT_FOUND + " not found"); //$NON-NLS-1$ //$NON-NLS-2$
            ps.write(EOL);
            ret = false;
        }  else {
            rCode = HTTP_OK;
            ps.print("HTTP/1.0 " + HTTP_OK+" OK"); //$NON-NLS-1$ //$NON-NLS-2$
            ps.write(EOL);
            ret = true;
        }
        ui.log("From " +s.getInetAddress().getHostAddress()+": GET " + //$NON-NLS-1$ //$NON-NLS-2$
            targ.getAbsolutePath()+"-->"+rCode); //$NON-NLS-1$
        ps.print("Server: Simple java"); //$NON-NLS-1$
        ps.write(EOL);
        ps.print("Date: " + (new Date())); //$NON-NLS-1$
        ps.write(EOL);
        if (ret) {
            if (!targ.isDirectory()) {
                ps.print("Content-length: "+targ.length()); //$NON-NLS-1$
                ps.write(EOL);
                ps.print("Last Modified: " + (new //$NON-NLS-1$
                              Date(targ.lastModified())));
                ps.write(EOL);
                String name = targ.getName();
                int ind = name.lastIndexOf('.');
                String ct = null;
                if (ind > 0) {
                    ct = (String) MIME_TYPES.get(name.substring(ind));
                }
                if (ct == null) {
                    ct = "unknown/unknown"; //$NON-NLS-1$
                }
                ps.print("Content-type: " + ct); //$NON-NLS-1$
                ps.write(EOL);
            } else {
                ps.print("Content-type: text/html"); //$NON-NLS-1$
                ps.write(EOL);
            }
        }
        return ret;
    }
    
    boolean sendString(String targ, String content, PrintStream ps) throws IOException {
        boolean ret = true;
        int rCode = 0;
        rCode = HTTP_OK;
        ps.print("HTTP/1.0 " + HTTP_OK+" OK"); //$NON-NLS-1$ //$NON-NLS-2$
        ps.write(EOL);
        ret = true;
        ui.log("From " +s.getInetAddress().getHostAddress()+": GET " + targ+"-->"+rCode); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ps.print("Server: Simple java"); //$NON-NLS-1$
        ps.write(EOL);
        ps.print("Date: " + (new Date())); //$NON-NLS-1$
        ps.write(EOL);
        ps.print("Content-length: "+content.getBytes(ENCODING).length); //$NON-NLS-1$
        ps.write(EOL);
        ps.print("Last Modified: " + (new Date())); //$NON-NLS-1$
        ps.write(EOL);
        String ct = null;
        ct = (String) MIME_TYPES.get(".html"); //$NON-NLS-1$
        ps.print("Content-type: " + ct); //$NON-NLS-1$
        ps.write(EOL);
        ps.write(EOL);
        ps.print(content);
        return ret;
    }

    void send404(String targ, PrintStream ps) throws IOException {
        ps.print("HTTP/1.0 " + HTTP_NOT_FOUND + " not found"); //$NON-NLS-1$ //$NON-NLS-2$
        ps.write(EOL);
        ui.log("From " +s.getInetAddress().getHostAddress()+": GET " + //$NON-NLS-1$ //$NON-NLS-2$
                targ+"-->"+HTTP_NOT_FOUND); //$NON-NLS-1$
        ps.print("Server: Simple java"); //$NON-NLS-1$
        ps.write(EOL);
        ps.print("Date: " + (new Date())); //$NON-NLS-1$
        ps.write(EOL);
        sendErrorMessage(ps);
    }
    
    void sendErrorMessage(PrintStream ps) throws IOException {
        ps.write(EOL);
        ps.write(EOL);
        ps.println(Messages.getString("Worker.error-not-found")+ //$NON-NLS-1$
                   Messages.getString("Worker.error-resource-not-found")); //$NON-NLS-1$    	
    }
    
    void send404(File targ, PrintStream ps) throws IOException {
    	sendErrorMessage(ps);
    }

    void sendFile(Context context, File targ, PrintStream ps) throws IOException {
        InputStream is = null;
        if (targ.isDirectory()) {
            ps.write(EOL);
            listDirectory(targ, ps);
            return;
        } else {
            is = new BufferedInputStream(new FileInputStream(targ.getAbsolutePath()));
            sendStream(context, is, ps);
        }
    }
    
    void sendStream(Context context, InputStream is, PrintStream ps) throws IOException {
    	int rCode = 0;
        rCode = HTTP_OK;
        ps.print("HTTP/1.0 " + HTTP_OK+" OK"); //$NON-NLS-1$ //$NON-NLS-2$
        ps.write(EOL);
        ui.log("From " +s.getInetAddress().getHostAddress()+": GET " + context.getTarget()+"-->"+rCode); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ps.print("Server: Simple java"); //$NON-NLS-1$
        ps.write(EOL);
        ps.print("Date: " + (new Date())); //$NON-NLS-1$
        ps.write(EOL);
        int ind = context.getTarget().lastIndexOf('.');
        String ct = null;
        String sufix = "";
        if (ind > 0) {
        	sufix = context.getTarget().substring(ind);
        	ct = (String) MIME_TYPES.get(sufix);
        }
        ps.print("Content-type: " + ct); //$NON-NLS-1$
        ps.write(EOL);
        ps.write(EOL);
        try {
            int n;
            int r;
            int j;
            
            if (".html".equals(sufix) || ".xml".equals(sufix)) {
            	n = 0;
            	j = 0;
            	while (true) {
            		r=is.read();
            		if (r==-1) {
            			ps.write(buf, 0, n);
            			break;
            		}
            		if (j==st.length) {
            			// empty buffer
            			ps.write(buf, 0, n);
            			n=0;
            			j=0;
            			
            			StringBuffer sb = new StringBuffer();
            			sb.append((char)r);
            			int prv = -1;
            			while (true) {
                    		r=is.read();
                    		if (r==-1) {
                    			ps.write(buf, 0, n);
                    			break;
                    		}
                    		if (r=='>' && prv=='?') {
                    			break;
                    		} else {
                    			prv = r;
                    			sb.append((char)r);
                    		}
            			}
            			String str = sb.substring(0, sb.length()-1);
            			String[] sspl = str.split(" ");
            			str = sspl[0];
            			String key = "";
            			if (sspl.length>=2) {
            				key = sspl[1];
            			}
            			Reader reader = null;
            			Content c = null;
            			if ((c=ui.getContents(str))!=null) {
            				reader = c.getContent(key, context);
            			} else {
            				try {
								c = (Content)Class.forName(str).newInstance();
								ui.registerContents(c);
								//ui.contents.put(str, c);
								reader = c.getContent(key, context);
							} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace(ps);
							}
            			}
            			if (reader != null) {
            				char[] cbuf = new char[BUF_SIZE];
            				int x;
            				while ((x = reader.read(cbuf)) > 0) {
            					if (x==BUF_SIZE) {
            						ps.print(cbuf);
            					} else {
            						for (int y =0; y<x; y++) {
            							ps.print(cbuf[y]);
            						}
            					}
            	            }
            				reader.close();
            			}
            		} else if (r==st[j]) {
            			j++;
            		} else {
            			for (int i=0; i<j; i++) {
            				buf[n]=st[i];
            				n++;
            			}
                		buf[n]=(byte)r;
                		n++;
            			j=0;
            		}
            		if (n>BUF_SIZE/2) {
            			ps.write(buf, 0, n);
            			n=0;
            			j=0;
            		}
            	}
            } else {
	            while ((n = is.read(buf)) > 0) {
	                ps.write(buf, 0, n);
	            }
            }
        } finally {
            is.close();
        }
    }

    void listDirectory(File dir, PrintStream ps) throws IOException {
    	ps.println("<TITLE>");
        ps.println(Messages.getString("Worker.directory-listing")); //$NON-NLS-1$
        ps.println("</TITLE><P>\n<A HREF=\"..\">");
        ps.println(Messages.getString("Worker.parent-directory")); //$NON-NLS-1$
        ps.println("</A><BR>\n");
        String[] list = dir.list();
        for (int i = 0; list != null && i < list.length; i++) {
            File f = new File(dir, list[i]);
            if (f.isDirectory()) {
                ps.println("<A HREF=\""+list[i]+"/\">"+list[i]+"/</A><BR>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            } else {
                ps.println("<A HREF=\""+list[i]+"\">"+list[i]+"</A><BR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }
        ps.println("<P><HR><BR><I>" + (new Date()) + "</I>"); //$NON-NLS-1$ //$NON-NLS-2$
    }

}