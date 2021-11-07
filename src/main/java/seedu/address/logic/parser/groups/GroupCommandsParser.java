package seedu.address.logic.parser.groups;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.groups.DeleteGroupCommand;
import seedu.address.logic.commands.groups.ViewGroupCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class GroupCommandsParser {

    /**
     * Used for further separation of command action and args.
     */
    private static final Pattern COMMAND_FORMAT = Pattern.compile("(?<action>\\-\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param commandArgs user input string after COMMAND_WORD
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public static Command parseCommand(String commandArgs) throws ParseException {
        final Matcher matcher = COMMAND_FORMAT.matcher(commandArgs.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String action = matcher.group("action");
        final String arguments = matcher.group("arguments");

        switch (action) {
        case AddGroupCommandParser.COMMAND_WORD:
            return new AddGroupCommandParser().parse(arguments);

        case GroupAddLessonParser.COMMAND_WORD:
            return new GroupAddLessonParser().parse(arguments);

        case GroupRemoveLessonParser.COMMAND_WORD:
            return new GroupRemoveLessonParser().parse(arguments);

        case DeleteGroupCommand.COMMAND_WORD:
            return new DeleteGroupCommandParser().parse(arguments);

        case ViewGroupCommand.COMMAND_WORD:
            return new ViewGroupCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
