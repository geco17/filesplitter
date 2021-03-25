package org.example.filesplitter.exception;

public class SplitException extends Exception {

    public SplitException(Exception e) {
        super(e);
    }

    public SplitException(String message) {
        super(message);
    }

    public SplitException(String message, Exception e) {
        super(message, e);
    }

}
