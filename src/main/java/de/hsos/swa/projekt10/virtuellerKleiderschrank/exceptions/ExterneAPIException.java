package de.hsos.swa.projekt10.virtuellerKleiderschrank.exceptions;

public class ExterneAPIException extends Exception {
    public static final int ERROR = 1;
    public static final int NOTFOUND = 2;
    private int errorCode;
    
    public ExterneAPIException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode; 
    }

    public int getErrorCode() {
        return errorCode;
    }
}
