# Sass Compiler for AEM 6.x

[![Build Status](https://travis-ci.org/mickleroy/aem-sass-compiler.svg?branch=master)](https://travis-ci.org/mickleroy/aem-sass-compiler)

This bundle provides support for the [Sass](http://sass-lang.com/) CSS pre-processor in Adobe Experience Manager 6.x.

## Features

Supports all the latest features of Sass:
* Variables
* Nesting
* Partials
* Imports
* Mixins
* Inheritance
* Operators

Examples of supported `@import` directives:
```
@import 'file';
@import 'file.scss';
@import 'partials/file';
@import 'partials/file.scss';
@import 'file1', 'file2';
@import '/etc/designs/aem-sass-compiler/clientlib/file';
@import '/etc/designs/aem-sass-compiler/clientlib/file.scss';
```

If any one of the conditions below are met, the `@import` will be skipped and compiled to a CSS `@import`:

* The file’s extension is .css
* The filename begins with http://
* The filename is a url()
* The @import has any media queries


## Usage

* Install the provided OSGi bundle (.jar) in the latest [release](https://github.com/mickleroy/aem-sass-compiler/releases).
* Start writing `.scss` files just like you would write `.less` files within AEM.
```
/etc/designs/clientlibs
├── css.txt
├── main.scss
└── partials
    └── _base.scss

```
The `css.txt` file should only reference your main Sass file:
```
#base=.
main.scss
```

## Bundle Install Log

The following log entries should be visible when installing the bundle:

```
*INFO* [...] com.adobe.granite.ui.clientlibs.impl.CompilerProviderImpl Registering client library compiler scss
*INFO* [...] com.github.mickleroy.aem.sass.impl.SassCompilerImpl Activating Sass Compiler
```

## Sass Compilation Log

Similar log entries should be visible when compiling a `.scss` file:

```
*INFO* [...] com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl Start building CSS library: /etc/designs/aem-sass-compiler/clientlib
*INFO* [...] com.github.mickleroy.aem.sass.impl.SassCompilerImpl Compiled Sass in 97ms
*INFO* [...] com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl finished building library /etc/designs/aem-sass-compiler/clientlib.css
```

## Implementation Details

Currently support LibSass version **3.4.3**.

**LibSass** is a C/C++ port of the original engine written in Ruby.
More info at [https://github.com/sass/libsass](https://github.com/sass/libsass)

**jsass** is a feature complete Java wrapper of LibSass (requires Java 8).
More info at [https://github.com/bit3/jsass](https://github.com/bit3/jsass)

## Release

Create a release branch off the `master` branch
```
git branch release/X.X.X
```

Prepare the release (use vX.X.X for the tag)
```
mvn release:prepare -DpushChanges=false
```

Push the changes to the repository
```
git push origin
```

Checkout the newly created tag and build the project
```
git checkout tags/vX.X.X
```

```
mvn clean package
```

Attach the jar to the release on Github and merge back to `master`.