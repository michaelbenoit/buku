package de.bensoft.bukkit.buku.cmd.api.qualifier;

/**
 * Created by CUSTDEV3 on 01/11/2020.
 */
public class QualifierException extends Exception {
    public QualifierException() {
    }

    public QualifierException(String message) {
        super(message);
    }

    public QualifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public QualifierException(Throwable cause) {
        super(cause);
    }

    public QualifierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
