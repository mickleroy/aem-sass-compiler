package com.github.mickleroy.aem.sass.util;

import com.adobe.granite.ui.clientlibs.script.ScriptResource;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;

public final class ScriptResourceUtil {

    /**
     * Retrieves the contents of the a ScriptResource as String.
     * @param resource
     * @return
     * @throws IOException
     */
    public static String retrieveContents(ScriptResource resource) throws IOException {
        String src;
        try (Reader in = resource.getReader()) {
            src = IOUtils.toString(in);
            src = src.replace("\r", "");
        }
        return src;
    }
}
