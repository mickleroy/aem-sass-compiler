package com.github.mickleroy.aem.sass.util;


import com.adobe.granite.ui.clientlibs.script.ScriptResource;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.mockito.Mockito.when;

public class ScriptResourceUtilTest {

    @Mock
    private ScriptResource mockScriptResource;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testContentsFromScriptResource() throws IOException {
        InputStream inputStream = IOUtils.toInputStream("hello world");
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(inputStream));
        Assert.assertEquals("hello world", ScriptResourceUtil.retrieveContents(mockScriptResource));
    }
}
