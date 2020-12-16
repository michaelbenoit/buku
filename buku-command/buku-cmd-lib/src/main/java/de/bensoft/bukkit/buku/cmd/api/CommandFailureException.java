package de.bensoft.bukkit.buku.cmd.api;

/**
 * Created by CUSTDEV3 on 01/11/2020.
 */
public class CommandFailureException extends RuntimeException {

    public CommandFailureException() {
    }

    public CommandFailureException(String message) {
        super(message);
    }

    public CommandFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandFailureException(Throwable cause) {
        super(cause);
    }

    public CommandFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
