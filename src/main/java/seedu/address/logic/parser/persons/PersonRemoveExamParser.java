package seedu.address.logic.parser.persons;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PERSON_COMMAND;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.persons.EditPersonCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditPersonCommand object
 */
public class PersonRemoveExamParser implements Parser<EditPersonCommand> {

    public static final String COMMAND_WORD = "-de";
    public static final String MESSAGE_USAGE = PERSON_COMMAND + " " + COMMAND_WORD
            + ": Removes the exam identified by the index number "
            + "from the person identified by the index number used in the displayed list.\n"
            + "Parameters: INDEX1 INDEX2 (both must be a positive integer)\n"
            + "Example: " + PERSON_COMMAND + " " + COMMAND_WORD + " 1 1";
    public static final String MESSAGE_SUCCESS = "Exam deleted from person:\n%s";

    @Override
    public EditPersonCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);

        List<Index> indexes = ParserUtil.parseOnlyIndexString(userInput, 2, MESSAGE_USAGE);

        EditPersonCommand.EditPersonDescriptor editPersonDescriptor = new EditPersonCommand.EditPersonDescriptor();
        editPersonDescriptor.removeExam(indexes.get(1));

        return new EditPersonCommand(indexes.get(0), editPersonDescriptor, MESSAGE_SUCCESS);
    }
}
