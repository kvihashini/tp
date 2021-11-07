package seedu.address.logic.parser.groups;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.GROUP_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import seedu.address.logic.commands.groups.GroupAddLessonCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.lessons.AddLessonParserUtil;

/**
 * Parses input arguments and creates a new EditPersonCommand object
 */
public class GroupAddLessonParser implements Parser<GroupAddLessonCommand> {

    public static final String COMMAND_WORD = "-al";
    public static final String MESSAGE_USAGE = GROUP_COMMAND + " " + COMMAND_WORD
            + ": Adds a lesson to the group identified "
            + "by the index number used in the displayed group list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_SUBJECT + "SUBJECT] "
            + "[" + PREFIX_START_TIME + "HH:MM START TIME] "
            + "[" + PREFIX_END_TIME + "HH:MM END TIME] "
            + "[" + PREFIX_DAY + "DAY]\n"
            + "Example: " + GROUP_COMMAND + " " + COMMAND_WORD + " 1 "
            + PREFIX_DAY + "Fri " + PREFIX_START_TIME + "14:00 " + PREFIX_END_TIME + "18:00 "
            + PREFIX_SUBJECT + "Biology ";

    @Override
    public GroupAddLessonCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        AddLessonParserUtil lessonParserUtil = AddLessonParserUtil.parseAddLesson(userInput, MESSAGE_USAGE);

        return new GroupAddLessonCommand(lessonParserUtil.getIndex(), lessonParserUtil.getLesson());
    }
}
