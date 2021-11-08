package seedu.address.model.id.exceptions;

import seedu.address.model.id.UniqueId;

public class IdNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Id not found: ";

    public IdNotFoundException(UniqueId id) {
        super(MESSAGE + id.toString());
    }
}
