package org.uam.exceptions;

public class FileLoadingException extends Exception {

    public FileLoadingException(String msg) {
        super(msg);
    }

    public FileLoadingException(String msg, Exception ex) {
        super(msg, ex);
    }
}
