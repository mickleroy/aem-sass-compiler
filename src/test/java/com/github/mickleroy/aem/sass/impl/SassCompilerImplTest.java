package com.github.mickleroy.aem.sass.impl;

import com.adobe.granite.ui.clientlibs.script.CompilerContext;
import com.adobe.granite.ui.clientlibs.script.ScriptResource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.osgi.service.component.ComponentContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

import static junitx.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);

        sassCompiler = new SassCompilerImpl();
        sassCompiler.activate(mockComponentContext);
    }

    @Test
    public void testGetName() {
        assertEquals("scss", sassCompiler.getName());
    }

    @Test
    public void testGetMimeType() {
        assertEquals("text/css", sassCompiler.getMimeType());
    }

    @Test
    public void testGetOutputExtension() {
        assertEquals("css", sassCompiler.getOutputExtension());
    }

    @Test
    public void testHandlesScss() {
        assertTrue(sassCompiler.handles("scss"));
    }

    @Test
    public void testCompile() throws IOException {
        String inputScss = "html { p { color: red; } }";
        // use String.format to ensure platform-dependent line separator
        String outputCss = String.format("html p {%n  color: red; }%n");
        PrintWriter out = spy(new PrintWriter(File.createTempFile("aem-sass-compiler", "")));
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(IOUtils.toInputStream(inputScss, CharEncoding.UTF_8)));

        sassCompiler.compile(Arrays.asList(mockScriptResource), out, mockCompilerContext);

        verify(out, times(1)).write(writerCaptor.capture());
        assertEquals(outputCss, writerCaptor.getValue());
    }

    @Test
    public void testCompilationException() throws IOException {
        String inputScss = "html() & { p { color: red; } }";
        PrintWriter out = spy(new PrintWriter(File.createTempFile("aem-sass-compiler", "")));
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(IOUtils.toInputStream(inputScss, CharEncoding.UTF_8)));

        sassCompiler.compile(Arrays.asList(mockScriptResource), out, mockCompilerContext);

        verify(out, times(5)).write(writerCaptor.capture());
        assertTrue(writerCaptor.getAllValues().contains("SASS compilation failed due an error!\n\n"));
    }
}
