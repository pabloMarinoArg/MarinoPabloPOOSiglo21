package src.errors;

public class EmptyException extends Exception {

    public static final String EMPTY_EXCEPTION = "Empty exception ";

    public EmptyException() {
        super(EMPTY_EXCEPTION);
    }

    public EmptyException(String msg) {
        super(EMPTY_EXCEPTION + msg);
    }
}
