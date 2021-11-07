package seedu.address.logic.parser.persons;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_INDEX_GIVEN;
import static seedu.address.logic.parser.CliSyntax.PERSON_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.time.LocalDateTime;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.persons.EditPersonCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.Subject;
import seedu.address.model.person.Exam;

/**
 * Parses input arguments and creates a new EditPersonCommand object
 */
public class PersonAddExamParser implements Parser<EditPersonCommand> {

    public static final String COMMAND_WORD = "-ae";
    public static final String MESSAGE_USAGE = PERSON_COMMAND + " " + COMMAND_WORD
            + ": Adds an exam to the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_SUBJECT + "SUBJECT] "
            + "[" + PREFIX_DAY + ParserUtil.DATE_TIME_FORMAT + "]\n"
            + "Example: " + PERSON_COMMAND + " " + COMMAND_WORD + " 1 "
            + PREFIX_DAY + "2022-10-10 10:00 " + PREFIX_SUBJECT + "Chinese";
    public static final String ADD_EXAM_SUCCESS = "Exam added to person:\n%s";

    @Override
    public EditPersonCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_SUBJECT, PREFIX_DAY);

        if (!argMultimap.arePrefixesPresent(PREFIX_SUBJECT, PREFIX_DAY)
                || argMultimap.getPreamble().isEmpty() || !argMultimap.preambleHasExpectedSegments(1)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_USAGE));
        }

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_INVALID_INDEX_GIVEN), pe);
        }

        Subject subject = ParserUtil.parseSubject(argMultimap.getValue(PREFIX_SUBJECT).get());
        LocalDateTime dateTime = ParserUtil.parseLocalDateTime(argMultimap.getValue(PREFIX_DAY).get());
        Exam exam = new Exam(subject, dateTime);
        EditPersonCommand.EditPersonDescriptor editPersonDescriptor = new EditPersonCommand.EditPersonDescriptor();
        editPersonDescriptor.addExam(exam);
        return new EditPersonCommand(index, editPersonDescriptor, ADD_EXAM_SUCCESS);
    }
}
