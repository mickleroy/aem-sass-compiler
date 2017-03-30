package com.github.mickleroy.aem.sass.impl;

import com.adobe.granite.ui.clientlibs.script.CompilerContext;
import com.adobe.granite.ui.clientlibs.script.ScriptCompiler;
import com.adobe.granite.ui.clientlibs.script.ScriptResource;
import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import org.apache.commons.io.IOUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

@Component
@Service(ScriptCompiler.class)
public class SassCompilerImpl implements ScriptCompiler {

    private static final String SCSS_EXTENSION = "scss";
    private static final String CSS_EXTENSION = "css";
    private static final String CSS_MIME_TYPE = "text/css";
    private static final Logger log = LoggerFactory.getLogger(SassCompilerImpl.class);

    private Compiler compiler;
    private Options options;

    @Activate
    public void activate(ComponentContext context) {
        log.info("Activating Sass Compiler");
        compiler = new Compiler();
        options = new Options();
    }

    @Override
    public String getName() {
        return SCSS_EXTENSION;
    }

    @Override
    public boolean handles(String extension) {
        return SCSS_EXTENSION.equals(extension) || ("." + SCSS_EXTENSION).equals(extension);
    }

    @Override
    public String getMimeType() {
        return CSS_MIME_TYPE;
    }

    @Override
    public String getOutputExtension() {
        return CSS_EXTENSION;
    }

    @Override
    public void compile(Collection<ScriptResource> scriptResources, Writer out, CompilerContext ctx) throws IOException {
        long t0 = System.currentTimeMillis();

        for(ScriptResource src : scriptResources) {
            log.info("Found source {}", src.getName());
            String scss = retrieveInputString(src);
            try {
                Output output = compiler.compileString(scss, options);
                out.write(output.getCss());
            } catch (CompilationException e) {
                dumpError(out, src.getName(), e.getErrorMessage());
            }
        }

        long t1 = System.currentTimeMillis();
        log.info("Compiled Scss in {}ms", t1 - t0);
    }

    private static String retrieveInputString(ScriptResource resource) throws IOException {
        String src = "";

        try (Reader in = resource.getReader()) {
            src = IOUtils.toString(in);
            src = src.replace("\r", "");
//            src = src.replace("@import-once", "@import (once)");
        }

        return src;
    }

    private void dumpError(Writer out, String name, String message) throws IOException {
        log.error("failed to compile scss {}: {}", name, message);
        out.write("/*****************************************************\n");
        out.write("SASS compilation failed due an error!\n\n");
        out.write("Input: " + name + "\n");
        out.write("Error:" + message + "\n");
        out.write("*****************************************************/\n");
    }
}
