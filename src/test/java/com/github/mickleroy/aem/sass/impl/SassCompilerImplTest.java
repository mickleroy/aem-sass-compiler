package com.github.mickleroy.aem.sass.impl;


import com.adobe.granite.ui.clientlibs.script.CompilerContext;
import com.adobe.granite.ui.clientlibs.script.ScriptResource;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.service.component.ComponentContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class SassCompilerImplTest {

    @Mock
    private ComponentContext mockComponentContext;
    @Mock
    private CompilerContext mockCompilerContext;
    @Mock
    private ScriptResource mockScriptResource;

    @Captor
    private ArgumentCaptor<String> writerCaptor;

    private SassCompilerImpl sassCompiler;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        sassCompiler = new SassCompilerImpl();
        sassCompiler.activate(mockComponentContext);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("scss", sassCompiler.getName());
    }

    @Test
    public void testGetMimeType() {
        Assert.assertEquals("text/css", sassCompiler.getMimeType());
    }

    @Test
    public void testGetOutputExtension() {
        Assert.assertEquals("css", sassCompiler.getOutputExtension());
    }

    @Test
    public void testHandlesScss() {
        Assert.assertTrue(sassCompiler.handles("scss"));
    }

    @Test
    public void testCompile() throws IOException {
        String inputScss = "html { p { color: red; } }";
        // use String.format to ensure platform-dependent line separator
        String outputCss = String.format("html p {%n  color: red; }%n");
        PrintWriter out = spy(new PrintWriter(File.createTempFile("aem-sass-compiler", "")));
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(IOUtils.toInputStream(inputScss)));

        sassCompiler.compile(Arrays.asList(mockScriptResource), out, mockCompilerContext);

        verify(out, times(1)).write(writerCaptor.capture());
        Assert.assertEquals(outputCss, writerCaptor.getValue());
    }

    @Test
    public void testCompilationException() throws IOException {
        String inputScss = "html() & { p { color: red; } }";
        PrintWriter out = spy(new PrintWriter(File.createTempFile("aem-sass-compiler", "")));
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(IOUtils.toInputStream(inputScss)));

        sassCompiler.compile(Arrays.asList(mockScriptResource), out, mockCompilerContext);

        verify(out, times(5)).write(writerCaptor.capture());
        Assert.assertTrue(writerCaptor.getAllValues().contains("SASS compilation failed due an error!\n\n"));
    }
}
