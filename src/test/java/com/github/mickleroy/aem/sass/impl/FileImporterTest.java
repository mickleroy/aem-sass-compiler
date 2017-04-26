package com.github.mickleroy.aem.sass.impl;


import com.adobe.granite.ui.clientlibs.script.CompilerContext;
import com.adobe.granite.ui.clientlibs.script.ScriptResource;
import com.adobe.granite.ui.clientlibs.script.ScriptResourceProvider;
import com.github.mickleroy.aem.sass.exceptions.ImportFileNotFoundException;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class FileImporterTest {

    @Mock
    private CompilerContext mockCompilerContext;
    @Mock
    private ScriptResourceProvider mockResourceProvider;
    @Mock
    private ScriptResource mockScriptResource;
    @Mock
    private Import mockPreviousImport;

    private static final String SAMPLE_CONTENTS = "html { margin: 0; }";
    private static final String ROOT_SASS_FILE = "/etc/designs/acme/clientlibs/main.scss";

    @Before
    public void before() throws URISyntaxException {
        MockitoAnnotations.initMocks(this);
        when(mockCompilerContext.getResourceProvider()).thenReturn(mockResourceProvider);
        when(mockScriptResource.getName()).thenReturn("");
        when(mockPreviousImport.getAbsoluteUri()).thenReturn(new URI("stdin"));
    }

    @Test
    public void testSkipImporterHttp() throws URISyntaxException {
        Importer importer = spy(new FileImporter(mockCompilerContext, ROOT_SASS_FILE));
        Collection<Import> imports = importer.apply("http://foo.bar", mockPreviousImport);
        Assert.assertNull(imports);
    }

    @Test
    public void testSkipImporterCss() throws URISyntaxException {
        Importer importer = spy(new FileImporter(mockCompilerContext, ROOT_SASS_FILE));
        Collection<Import> imports = importer.apply("plain.css", mockPreviousImport);
        Assert.assertNull(imports);
    }

    @Test
    public void testImportScss() throws URISyntaxException, IOException {
        InputStream inputStream = IOUtils.toInputStream(SAMPLE_CONTENTS);
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(inputStream));
        when(mockResourceProvider.getResource("/etc/designs/acme/clientlibs/reset.scss")).thenReturn(mockScriptResource);

        Importer importer = spy(new FileImporter(mockCompilerContext, ROOT_SASS_FILE));
        Collection<Import> imports = importer.apply("reset", mockPreviousImport);

        Assert.assertEquals(1, imports.size());
        Assert.assertEquals(SAMPLE_CONTENTS, imports.iterator().next().getContents());
    }

    @Test
    public void testImportScssWithExtension() throws URISyntaxException, IOException {
        InputStream inputStream = IOUtils.toInputStream(SAMPLE_CONTENTS);
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(inputStream));
        when(mockResourceProvider.getResource("/etc/designs/acme/clientlibs/reset.scss")).thenReturn(mockScriptResource);

        Importer importer = spy(new FileImporter(mockCompilerContext, ROOT_SASS_FILE));
        Collection<Import> imports = importer.apply("reset.scss", mockPreviousImport);

        Assert.assertEquals(1, imports.size());
        Assert.assertEquals(SAMPLE_CONTENTS, imports.iterator().next().getContents());
    }

    @Test
    public void testImportAbsolute() throws URISyntaxException, IOException {
        InputStream inputStream = IOUtils.toInputStream(SAMPLE_CONTENTS);
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(inputStream));
        when(mockResourceProvider.getResource("/etc/designs/myco/clientlibs/absolute.scss")).thenReturn(mockScriptResource);

        Importer importer = spy(new FileImporter(mockCompilerContext, ROOT_SASS_FILE));
        Collection<Import> imports = importer.apply("/etc/designs/myco/clientlibs/absolute", mockPreviousImport);

        Assert.assertEquals(1, imports.size());
        Assert.assertEquals(SAMPLE_CONTENTS, imports.iterator().next().getContents());
    }

    @Test
    public void testImportAbsoluteWithExtension() throws URISyntaxException, IOException {
        InputStream inputStream = IOUtils.toInputStream(SAMPLE_CONTENTS);
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(inputStream));
        when(mockResourceProvider.getResource("/etc/designs/myco/clientlibs/absolute.scss")).thenReturn(mockScriptResource);

        Importer importer = spy(new FileImporter(mockCompilerContext, ROOT_SASS_FILE));
        Collection<Import> imports = importer.apply("/etc/designs/myco/clientlibs/absolute.scss", mockPreviousImport);

        Assert.assertEquals(1, imports.size());
        Assert.assertEquals(SAMPLE_CONTENTS, imports.iterator().next().getContents());
    }

    @Test
    public void testImportPartial() throws URISyntaxException, IOException {
        InputStream inputStream = IOUtils.toInputStream(SAMPLE_CONTENTS);
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(inputStream));
        when(mockResourceProvider.getResource("/etc/designs/acme/clientlibs/partials/base.scss")).thenReturn(mockScriptResource);

        Importer importer = spy(new FileImporter(mockCompilerContext, ROOT_SASS_FILE));
        Collection<Import> imports = importer.apply("partials/base.scss", mockPreviousImport);

        Assert.assertEquals(1, imports.size());
        Assert.assertEquals(SAMPLE_CONTENTS, imports.iterator().next().getContents());
    }

    @Test(expected = ImportFileNotFoundException.class)
    public void testImportNotFound() throws URISyntaxException, IOException {
        when(mockResourceProvider.getResource("/etc/designs/acme/clientlibs/unknown.png")).thenReturn(null);

        Importer importer = spy(new FileImporter(mockCompilerContext, ROOT_SASS_FILE));
        importer.apply("unknown.png", mockPreviousImport);
    }
}
