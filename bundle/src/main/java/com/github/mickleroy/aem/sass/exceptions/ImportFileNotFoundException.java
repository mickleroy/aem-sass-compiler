package com.github.mickleroy.aem.sass.exceptions;


public class ImportFileNotFoundException extends RuntimeException {

    private String path;

    public ImportFileNotFoundException(String path) {
        super();
        this.path = path;
    }

    public String getMessage() {
        return "Could not find @import located at " + path;
    }
}
