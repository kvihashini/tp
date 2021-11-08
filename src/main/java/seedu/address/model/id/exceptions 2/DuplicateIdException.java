package seedu.address.model.id.exceptions;

public class DuplicateIdException extends RuntimeException {

    public static final String MESSAGE = "Operation results in duplicate id";

    public DuplicateIdException() {
        super(MESSAGE);
    }
}
