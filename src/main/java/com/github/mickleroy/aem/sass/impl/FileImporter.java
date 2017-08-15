package com.github.mickleroy.aem.sass.impl;


import com.adobe.granite.ui.clientlibs.script.CompilerContext;
import com.adobe.granite.ui.clientlibs.script.ScriptResource;
import com.github.mickleroy.aem.sass.exceptions.ImportFileNotFoundException;
import com.github.mickleroy.aem.sass.util.ScriptResourceUtil;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;

/**
 * This class is used to process "@import" directives within SASS files. It attempts
 * to find matching a resource in the JCR that correspond to the input specified.
 *
 * Supported statements:
 * <code>@import 'file';</code>
 * <code>@import 'file.scss';</code>
 * <code>@import 'partials/file';</code>
 * <code>@import 'partials/file.scss';</code>
 * <code>@import 'file1', 'file2';</code>
 * <code>@import '/etc/designs/clientlibs/file';</code>
 * <code>@import '/etc/designs/clientlibs/file.scss';</code>
 *
 * Skipped statements:
 * <code>@import 'http://foo.bar';</code>
 *
 * Unsupported statements:
 * <code>@import 'url(some-url-import)';</code>
 * <code>@import 'file.css';</code>
 */
public class FileImporter implements Importer {

    /**
     * The compiler context to be used to fetch the contents of imported files
     */
    private CompilerContext context;

    /**
     * The path to the script that initiated the compilation i.e. /etc/designs/acme/clientlibs/main.scss
     */
    private String pathToPrimaryFile;

    /**
     * The path to the file the importer is currently processing i.e. /etc/designs/acme/clientlibs/partials/_base.scss
     */
    private String pathToCurrentFile;

    /**
     * A list of formats to be used to find potential matching resources
     */
    private static final String[] PATH_MATCHERS = {
        "%s%s%s",        // 'file';
        "%s%s%s.scss",   // 'file.scss';
        "%s%s_%s",       // '_file';
        "%s%s_%s.scss",  // '_file.scss';
    };

    private static Logger log = LoggerFactory.getLogger(FileImporter.class);


    public FileImporter(CompilerContext context, String path) {
        this.context = context;
        this.pathToPrimaryFile = path;
        this.pathToCurrentFile = path;
    }

    @Override
    public Collection<Import> apply(String url, Import previous) {
        if(shouldSkipImporter(url)) {
            return null;
        }

        ScriptResource resource = null;
        String resourcePath = null;
        String currentDir  = getCurrentDirectory(previous);
        String pathToFile  = getPathToFile(url);
        String getFileName = getFileName(url);

        // with absolute imports, do not use current directory
        if(url.startsWith("/")) {
            currentDir = "";
        }

        // loop through potential matches
        for(String matcher : PATH_MATCHERS) {
            String pathToImport = String.format(matcher, currentDir, pathToFile, getFileName);
            resource = getResource(pathToImport);

            if(resource != null) {
                resourcePath = pathToImport;
                break;
            }
        }

        // resource couldn't be found, error out
        if(resource == null) {
            throw new ImportFileNotFoundException(url);
        }

        try {
            context.getDependencies().add(resourcePath);
            return Collections.singletonList(new Import(
                    new URI(resource.getName()),
                    new URI(resource.getName()),
                    ScriptResourceUtil.retrieveContents(resource)
            ));
        } catch (URISyntaxException | IOException ex) {
            log.error("Could not process import", ex);
        }

        // skip this import
        return null;
    }

    /**
     * Get the file name of the import (including extension):
     * "file"          --> "file"
     * "file.scss"     --> "file.scss"
     * "partials/file" --> "file"
     * @param url
     * @return
     */
    private String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    /**
     * Get the path to the file to import (including slash):
     * "file"          --> ""
     * "file.scss"     --> ""
     * "partials/file" --> "partials/"
     * "/etc/designs/clientlibs/file.scss" --> "/etc/designs/clientlibs/"
     * @param url
     * @return
     */
    private String getPathToFile(String url) {
        return url.substring(0, url.lastIndexOf("/") + 1);
    }

    /**
     * Checks whether this importer should be skipped and handled by built-in importer.
     * @param url
     * @return
     */
    private boolean shouldSkipImporter(String url) {
        return url.startsWith("http") ||
               url.startsWith("url(") ||
               url.endsWith(".css");
    }

    /**
     * Gets the resource at the specified path.
     * @param path
     * @return
     */
    private ScriptResource getResource(String path) {
        try {
            return context.getResourceProvider().getResource(path);
        } catch (IOException ioEx) {
            log.error("IO Error", ioEx);
        }
        return null;
    }

    /**
     * Gets the directory of the script resource that the importer is currently processing.
     * Must take into account previous @import for nested directives.
     * @return
     */
    private String getCurrentDirectory(Import previous) {
        // if processing from standard in, use path to primary file
        if("stdin".equals(previous.getAbsoluteUri().getPath())) {
            pathToCurrentFile = pathToPrimaryFile;
        } else {
            pathToCurrentFile = previous.getAbsoluteUri().getPath();
        }

        return pathToCurrentFile.substring(0, pathToCurrentFile.lastIndexOf("/") + 1);
    }
}
