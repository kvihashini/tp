package seedu.address.logic.parser.persons;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.persons.AddPersonCommand;
import seedu.address.logic.commands.persons.DeletePersonCommand;
import seedu.address.logic.commands.persons.EditPersonCommand;
import seedu.address.logic.commands.persons.FindPersonCommand;
import seedu.address.logic.commands.persons.ViewPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class PersonCommandsParser {

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

        case AddPersonCommand.COMMAND_WORD:
            return new AddPersonCommandParser().parse(arguments);

        case EditPersonCommand.COMMAND_WORD:
            return new EditPersonCommandParser().parse(arguments);

        case DeletePersonCommand.COMMAND_WORD:
            return new DeletePersonCommandParser().parse(arguments);

        case FindPersonCommand.COMMAND_WORD:
            return new FindPersonCommandParser().parse(arguments);

        case PersonAddLessonParser.COMMAND_WORD:
            return new PersonAddLessonParser().parse(arguments);

        case PersonRemoveLessonParser.COMMAND_WORD:
            return new PersonRemoveLessonParser().parse(arguments);

        case ViewPersonCommand.COMMAND_WORD:
            return new ViewPersonCommandParser().parse(arguments);

        case PersonAddExamParser.COMMAND_WORD:
            return new PersonAddExamParser().parse(arguments);

        case PersonRemoveExamParser.COMMAND_WORD:
            return new PersonRemoveExamParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
