package de.bensoft.bukkit.buku.descgen.lib.exception;

public class DescGeneratorException extends RuntimeException {

    public DescGeneratorException() {
    }

    public DescGeneratorException(String message) {
        super(message);
    }

    public DescGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DescGeneratorException(Throwable cause) {
        super(cause);
    }

    public DescGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
