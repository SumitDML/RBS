package com.dml.project.rbs.exception;

public class FileNotFoundException extends  RuntimeException {

    private static final long serialVersionUID = 1L;
    public FileNotFoundException(final String message){
        super(message);
    }

}
