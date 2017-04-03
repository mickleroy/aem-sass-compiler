# Sass Compiler for AEM 6.x

[![Project Status: WIP - Initial development is in progress, but there has not yet been a stable, usable release suitable for the public.](http://www.repostatus.org/badges/latest/wip.svg)](http://www.repostatus.org/#wip)

This bundle provides support for the [Sass](http://sass-lang.com/) CSS pre-processor in Adobe Experience Manager 6.x.

## Usage

Install the provided OSGi bundle (.jar) in the latest release.

## Install Log

The following log entries should be visible when installing the bundle:

```
*INFO* [OsgiInstallerImpl] com.adobe.granite.ui.clientlibs.impl.CompilerProviderImpl Registering client library compiler scss
*INFO* [OsgiInstallerImpl] com.github.mickleroy.aem-sass-compiler Service [com.github.mickleroy.aem.sass.impl.SassCompilerImpl,7730, [com.adobe.granite.ui.clientlibs.script.ScriptCompiler]] ServiceEvent REGISTERED
```

## Sass Compilation Log

The following log entries should be visible when compiling a `.scss` file:

```
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl Start building CSS library: /etc/designs/aem-sass-compiler/clientlib
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.github.mickleroy.aem.sass.impl.SassCompilerImpl Found source /etc/designs/aem-sass-compiler/clientlib/sample.scss
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.github.mickleroy.aem.sass.impl.SassCompilerImpl Compile Sass in 1ms
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl finished building library /etc/designs/aem-sass-compiler/clientlib.css
```

## Implementation Details

Currently support LibSass version **3.4.3**.

**LibSass** is a C/C++ port of the original engine written in Ruby.
More info at [https://github.com/sass/libsass](https://github.com/sass/libsass)

**jsass** is a feature complete Java wrapper of LibSass (requires Java 8).
More info at [https://github.com/bit3/jsass](https://github.com/bit3/jsass)
