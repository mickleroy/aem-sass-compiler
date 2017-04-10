# Sass Compiler for AEM 6.x

[![Project Status: WIP - Initial development is in progress, but there has not yet been a stable, usable release suitable for the public.](http://www.repostatus.org/badges/latest/wip.svg)](http://www.repostatus.org/#wip)
[![Build Status](https://travis-ci.org/mickleroy/aem-sass-compiler.svg?branch=master)](https://travis-ci.org/mickleroy/aem-sass-compiler)

This bundle provides support for the [Sass](http://sass-lang.com/) CSS pre-processor in Adobe Experience Manager 6.x.

## Usage

* Install the provided OSGi bundle (.jar) in the latest release.
* Start writing `.scss` files just like you would write `.less` files within AEM.
```
/etc/designs/clientlibs
├── css.txt
├── main.scss
├── partials
│   └── _base.scss
└── plain.css
```
The `css.txt` file should only reference your main Sass file:
```
#base=.
plain.css
main.scss
```

## Bundle Install Log

The following log entries should be visible when installing the bundle:

```
*INFO* [...] com.adobe.granite.ui.clientlibs.impl.CompilerProviderImpl Registering client library compiler scss
*INFO* [...] com.github.mickleroy.aem.sass.impl.SassCompilerImpl Activating Sass Compiler
*INFO* [...] com.github.mickleroy.aem-sass-compiler Service [com.github.mickleroy.aem.sass.impl.SassCompilerImpl,7730, [com.adobe.granite.ui.clientlibs.script.ScriptCompiler]] ServiceEvent REGISTERED
```

## Sass Compilation Log

The following log entries should be visible when compiling a `.scss` file:

```
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl Start building CSS library: /etc/designs/aem-sass-compiler/clientlib
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.github.mickleroy.aem.sass.impl.SassCompilerImpl Compiled Sass in 97ms
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl finished building library /etc/designs/aem-sass-compiler/clientlib.css
```

## Implementation Details

Currently support LibSass version **3.4.3**.

**LibSass** is a C/C++ port of the original engine written in Ruby.
More info at [https://github.com/sass/libsass](https://github.com/sass/libsass)

**jsass** is a feature complete Java wrapper of LibSass (requires Java 8).
More info at [https://github.com/bit3/jsass](https://github.com/bit3/jsass)
