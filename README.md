
## Install log
```
*INFO* [OsgiInstallerImpl] com.adobe.granite.ui.clientlibs.impl.CompilerProviderImpl Registering client library compiler scss
*INFO* [OsgiInstallerImpl] com.github.mickleroy.aem-sass-compiler Service [com.github.mickleroy.aem.sass.impl.SassCompilerImpl,7730, [com.adobe.granite.ui.clientlibs.script.ScriptCompiler]] ServiceEvent REGISTERED
```

## Compilation log
```
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl Start building CSS library: /etc/designs/aem-sass-compiler/clientlib
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.github.mickleroy.aem.sass.impl.SassCompilerImpl Found source /etc/designs/aem-sass-compiler/clientlib/sample.scss
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.github.mickleroy.aem.sass.impl.SassCompilerImpl Compile Sass in 1ms
*INFO* [0:0:0:0:0:0:0:1 [1490695427683] GET /etc/designs/aem-sass-compiler/clientlib.css HTTP/1.1] com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl finished building library /etc/designs/aem-sass-compiler/clientlib.css
```

## Test resources
[Clientlib Cache Clear](http://localhost:4502/libs/granite/ui/content/dumplibs.rebuild.html?invalidate=true)

[Sample Scss Clientlib](http://localhost:4502/etc/designs/aem-sass-compiler/clientlib.css)

## Ruby version
The Sass compiler makes use of JRuby 9.1.8.0 under the hood so it is compatible with Ruby 2.x.
More info at [http://jruby.org/](http://jruby.org/)

## Libsass
LibSass is a C/C++ port of the original engine written in Ruby. The latest stable release is 3.4.3.
More info at [https://github.com/sass/libsass](https://github.com/sass/libsass)

### Java Wrapper
JSass is the Java wrapper of LibSass. The latest release is 5.4.3 (for libsass 3.4.3) 
More info at [https://github.com/bit3/jsass](https://github.com/bit3/jsass)
Docs at [http://jsass.readthedocs.io/en/latest/](http://jsass.readthedocs.io/en/latest/)
Note: requires Java 8

### Ruby Wrapper
SassC Ruby is the Ruby wrapper of LibSass. The latest release is 1.11.2 (for libsass 3.4.3)
More info at [https://github.com/sass/sassc-ruby](https://github.com/sass/sassc-ruby)
