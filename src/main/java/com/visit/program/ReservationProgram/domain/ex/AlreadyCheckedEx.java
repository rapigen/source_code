package com.visit.program.ReservationProgram.domain.ex;

public class AlreadyCheckedEx extends RuntimeException{
    public AlreadyCheckedEx() {
        super();
    }

    public AlreadyCheckedEx(String message) {
        super(message);
    }

    public AlreadyCheckedEx(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyCheckedEx(Throwable cause) {
        super(cause);
    }

    protected AlreadyCheckedEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
