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