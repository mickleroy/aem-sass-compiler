package com.github.mickleroy.aem.sass.impl;

import org.apache.jackrabbit.util.Base64;
import org.apache.jackrabbit.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Binary;
import javax.jcr.Property;
import javax.jcr.Session;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Disclaimer: This is decompiled code from com.adobe.granite.ui.clientlibs.script.Utils provided in the
 * JAR: com.adobe.granite.ui.clientlibs-1.0.14-CQ610-B0001.jar
 *
 * Using the original class resulted in a java.lang.VerifyError at runtime.
 */
final class Utils {

    private static final Logger log = LoggerFactory.getLogger(com.adobe.granite.ui.clientlibs.script.Utils.class);
    private static Pattern SCHEME_START = Pattern.compile("^[^:/]+:[^/]*/.*");
    private static Pattern URL_PATTERN = Pattern.compile("url\\(\\s*(['\"]?)([^'\")]*)(['\"]?\\s*)\\)");

    private Utils() {
    }

    public static String rewriteUrlsInCss(String libPath, String filePath, String css) {
        return rewriteUrlsInCss(Text.explode(libPath, 47), Text.explode(filePath, 47), css, null, 0L);
    }

    public static String rewriteUrlsInCss(String[] libPathSegs, String[] filePathSegs, String css, Session session, long maxDataUriSize) {
        Matcher m = URL_PATTERN.matcher(css);

        StringBuffer result;
        String url;
        for(result = new StringBuffer(); m.find(); m.appendReplacement(result, "url($1" + url + "$3)")) {
            url = m.group(2);
            if (url.startsWith("absolute:")) {
                url = url.substring(9);
            } else if (!url.endsWith(".htc") && !url.startsWith("//")) {
                url = resolveUrl(libPathSegs, filePathSegs, url);
                if (maxDataUriSize > 0L && session != null && !SCHEME_START.matcher(url).matches()) {
                    Binary bin = null;
                    String path = "/" + Text.implode(libPathSegs, "/") + "/../" + url + "/jcr:content/jcr:data";

                    try {
                        if (session.propertyExists(path)) {
                            Property p = session.getProperty(path);
                            if (p.getLength() < maxDataUriSize) {
                                bin = p.getBinary();
                                StringWriter w = new StringWriter();
                                Base64.encode(bin.getStream(), w);
                                url = "data:" + p.getParent().getProperty("{http://www.jcp.org/jcr/1.0}mimeType").getString() + ";base64," + w.toString();
                            }
                        }
                    } catch (Exception var16) {
                        log.warn("Error while encoding data uri of {}: {}", path, var16.toString());
                    } finally {
                        if (bin != null) {
                            bin.dispose();
                        }

                    }
                }
            }
        }

        m.appendTail(result);
        return result.toString();
    }

    private static String resolveUrl(String[] libPath, String[] filePath, String url) {
        if (url.length() != 0 && !SCHEME_START.matcher(url).matches()) {
            LinkedList<String> file = new LinkedList();
            if (!url.startsWith("/")) {
                file.addAll(Arrays.asList(filePath));
                file.removeLast();
            }

            boolean warned = false;
            String[] arr$ = Text.explode(url, 47);
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String seg = arr$[i$];
                if ("..".equals(seg)) {
                    if (file.isEmpty()) {
                        if (!warned) {
                            log.warn("/{}: url('{}') invalid. too many '..'", Text.implode(filePath, "/"), url);
                            warned = true;
                        }
                    } else {
                        file.removeLast();
                    }
                } else if (!".".equals(seg)) {
                    file.add(seg);
                }
            }

            int i;
            for(i = 0; i < libPath.length - 1 && libPath[i].matches(file.getFirst()); ++i) {
                file.removeFirst();
            }

            while(i++ < libPath.length - 1) {
                file.addFirst("..");
            }

            StringBuilder ret = new StringBuilder();
            String delim = "";

            for(Iterator i$ = file.iterator(); i$.hasNext(); delim = "/") {
                String seg = (String)i$.next();
                ret.append(delim).append(seg);
            }

            if (log.isDebugEnabled()) {
                log.debug("resolving lib=/{}, file=/{}, url={} -> {}", new Object[]{Text.implode(libPath, "/"), Text.implode(filePath, "/"), url, ret});
            }

            return ret.toString();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("resolving lib=/{}, file=/{}, url={} (ignored)", new Object[]{Text.implode(libPath, "/"), Text.implode(filePath, "/"), url});
            }

            return url;
        }
    }
}
