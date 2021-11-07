package seedu.address.logic.parser.persons;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PERSON_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import seedu.address.logic.commands.persons.EditPersonCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.lessons.AddLessonParserUtil;

/**
 * Parses input arguments and creates a new EditPersonCommand object
 */
public class PersonAddLessonParser implements Parser<EditPersonCommand> {

    public static final String COMMAND_WORD = "-al";
    public static final String MESSAGE_USAGE = PERSON_COMMAND + " " + COMMAND_WORD
            + ": Adds a lesson to the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_SUBJECT + "SUBJECT] "
            + "[" + PREFIX_START_TIME + "HH:MM START TIME] "
            + "[" + PREFIX_END_TIME + "HH:MM END TIME] "
            + "[" + PREFIX_DAY + "DAY]\n"
            + "Example: " + PERSON_COMMAND + " " + COMMAND_WORD + " 1 "
            + PREFIX_DAY + "Thu " + PREFIX_START_TIME + "10:00 " + PREFIX_END_TIME + "12:00 "
            + PREFIX_SUBJECT + "Chinese ";
    public static final String ADD_LESSON_SUCCESS = "Lesson added to person:\n%s";

    @Override
    public EditPersonCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        AddLessonParserUtil lessonParser = AddLessonParserUtil.parseAddLesson(userInput, MESSAGE_USAGE);
        EditPersonCommand.EditPersonDescriptor editPersonDescriptor = new EditPersonCommand.EditPersonDescriptor();
        editPersonDescriptor.addLesson(lessonParser.getLesson());
        return new EditPersonCommand(lessonParser.getIndex(), editPersonDescriptor, ADD_LESSON_SUCCESS);
    }
}
