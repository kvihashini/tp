package seedu.address.logic.commands.persons;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PERSON_COMMAND;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ViewingType;
import seedu.address.model.person.Person;


/**
 * Views a person identified using it's displayed index from the address book.
 */
public class ViewPersonCommand extends Command {

    public static final String COMMAND_WORD = "-v";

    public static final String MESSAGE_USAGE = PERSON_COMMAND + " " + COMMAND_WORD
            + ": Views a student in the students list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + PERSON_COMMAND + " " + COMMAND_WORD + " 1";

    public static final String MESSAGE_VIEW_PERSON_SUCCESS = "View Student: %1$s";

    private final Index targetIndex;

    public ViewPersonCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToView = lastShownList.get(targetIndex.getZeroBased());
        model.setPersonToView(personToView);
        model.setViewingType(ViewingType.PERSON);
        return new CommandResult(String.format(MESSAGE_VIEW_PERSON_SUCCESS, personToView));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewPersonCommand // instanceof handles nulls
                && targetIndex.equals(((ViewPersonCommand) other).targetIndex)); // state check
    }
}
