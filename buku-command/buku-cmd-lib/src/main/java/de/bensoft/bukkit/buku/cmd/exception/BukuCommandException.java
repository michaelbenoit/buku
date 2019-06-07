package de.bensoft.bukkit.buku.cmd.exception;

public class BukuCommandException extends RuntimeException {

    public BukuCommandException() {
    }

    public BukuCommandException(String message) {
        super(message);
    }

    public BukuCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public BukuCommandException(Throwable cause) {
        super(cause);
    }

    public BukuCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
