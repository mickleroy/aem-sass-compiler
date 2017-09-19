### 0.9.1
* Fixes issue with client library not getting invalidated when modifying dependent files (#4)

### 0.9.0
* Initial release for 6.1 support

### 1.0.2
* Maintenance release with [Jsass 5.5.1](https://github.com/bit3/jsass/releases/tag/5.5.1)
* Updated uber-jar version to 6.2.0-SP1

### 1.0.1
* Release for Maven Central

### 1.0.0
* Initial release
    * Fixed issue with some paths being processed incorrectly by AEM

### 0.0.2
* Adds support for nested `@import` directives
* Known issues:
    * AEM resolves some paths to `../../../var/clientlibs/<path-to-resource>`

### 0.0.1
* Initial pre-release
* Known issues: 
    * Jsass resolves paths to `/var/clientlibs/...`
    * no support for nested `@import`