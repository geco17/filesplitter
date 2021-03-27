package org.example.filesplitter.exception;

public class SplitException extends Exception {

    /**
     * A wrapper for exceptions raised during splitting.
     *
     * @param e The base exception.
     */
    public SplitException(final Exception e) {
        super(e);
    }

}
