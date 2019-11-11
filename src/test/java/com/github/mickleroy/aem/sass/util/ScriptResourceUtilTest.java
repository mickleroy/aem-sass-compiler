package com.github.mickleroy.aem.sass.util;

import com.adobe.granite.ui.clientlibs.script.ScriptResource;
import junitx.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ScriptResourceUtilTest {

    @Mock
    private ScriptResource mockScriptResource;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testContentsFromScriptResource() throws IOException {
        InputStream inputStream = IOUtils.toInputStream("hello world", CharEncoding.UTF_8);
        when(mockScriptResource.getReader()).thenReturn(new InputStreamReader(inputStream));
        Assert.assertEquals("hello world", ScriptResourceUtil.retrieveContents(mockScriptResource));
    }
}
