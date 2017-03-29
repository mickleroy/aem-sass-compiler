
### Install log
```
*INFO* [OsgiInstallerImpl] com.adobe.granite.ui.clientlibs.impl.CompilerProviderImpl Registering client library compiler scss
*INFO* [OsgiInstallerImpl] com.github.mickleroy.aem-sass-compiler Service [com.github.mickleroy.aem.sass.impl.SassCompilerImpl,7730, [com.adobe.granite.ui.clientlibs.script.ScriptCompiler]] ServiceEvent REGISTERED
```

### Compilation log
```
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl Start building CSS library: /etc/designs/aem-sass-compiler/clientlib
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.github.mickleroy.aem.sass.impl.SassCompilerImpl Found source /etc/designs/aem-sass-compiler/clientlib/sample.scss
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.github.mickleroy.aem.sass.impl.SassCompilerImpl Compile Sass in 1ms
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl finished building library /etc/designs/aem-sass-compiler/clientlib.css
```

### Test resources
[Clientlib Cache Clear](http://localhost:4502/libs/granite/ui/content/dumplibs.rebuild.html?invalidate=true)

[Sample Scss Clientlib](http://localhost:4502/etc/designs/aem-sass-compiler/clientlib.css)