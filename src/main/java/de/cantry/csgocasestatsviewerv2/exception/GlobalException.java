package de.cantry.csgocasestatsviewerv2.exception;

public class GlobalException extends RuntimeException {

    public GlobalException(String message, Exception e) {
        super(message, e);
    }

    public GlobalException(String message) {
        super(message);
    }

}
