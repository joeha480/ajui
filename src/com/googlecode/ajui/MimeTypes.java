package com.googlecode.ajui;

class MimeTypes {
	
	private MimeTypes() {}
	
    /* mapping of file extensions to content-types */
    static final java.util.Hashtable<String, String> MIME_TYPES = new java.util.Hashtable<>();

    static {
        fillMap();
    }
    static void setSuffix(String k, String v) {
        MIME_TYPES.put(k, v);
    }

    static void fillMap() {
        setSuffix("", "content/unknown"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".uu", "application/octet-stream"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".exe", "application/octet-stream"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".ps", "application/postscript"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".zip", "application/zip"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".sh", "application/x-shar"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".tar", "application/x-tar"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".snd", "audio/basic"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".au", "audio/basic"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".wav", "audio/x-wav"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".gif", "image/gif"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".jpg", "image/jpeg"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".jpeg", "image/jpeg"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".htm", "text/html"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".html", "text/html"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".text", "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".c", "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".cc", "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".c++", "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".css", "text/css"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".h", "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".pl", "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".txt", "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".java", "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".xml", "text/xml"); //$NON-NLS-1$ //$NON-NLS-2$
        setSuffix(".ico", "image/x-icon"); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
