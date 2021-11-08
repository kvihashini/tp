package seedu.address.logic.parser.groups;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.GROUP_COMMAND;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.groups.GroupRemoveLessonCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditPersonCommand object
 */
public class GroupRemoveLessonParser implements Parser<GroupRemoveLessonCommand> {

    public static final String COMMAND_WORD = "-dl";
    public static final String MESSAGE_USAGE = GROUP_COMMAND + " " + COMMAND_WORD
            + ": Removes the lesson identified by the index number "
            + "from the group identified by the index number used in the displayed list.\n"
            + "Parameters: INDEX1 INDEX2 (both must be a positive integer)\n"
            + "Example: " + GROUP_COMMAND + " " + COMMAND_WORD + " 1 1";

    @Override
    public GroupRemoveLessonCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);

        List<Index> indexes = ParserUtil.parseOnlyIndexString(userInput, 2, MESSAGE_USAGE);

        return new GroupRemoveLessonCommand(indexes.get(0), indexes.get(1));
    }
}
