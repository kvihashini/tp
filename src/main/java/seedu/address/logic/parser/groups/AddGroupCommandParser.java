package seedu.address.logic.parser.groups;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_INDEX_GIVEN;
import static seedu.address.logic.parser.CliSyntax.GROUP_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.groups.AddGroupCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;

public class AddGroupCommandParser implements Parser<AddGroupCommand> {

    public static final String COMMAND_WORD = "-a";
    public static final String MESSAGE_USAGE = GROUP_COMMAND + " " + COMMAND_WORD
            + ": Creates a group with the persons identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX1 INDEX2 ... (must be distinct positive integers) "
            + "[" + PREFIX_NAME + "GROUP NAME]\n"
            + "Example: " + GROUP_COMMAND + " " + COMMAND_WORD + "1 2 3 "
            + PREFIX_NAME + "Chinese Group ";

    public static final String MESSAGE_DUPLICATE_INDEX = "You cannot specify the same person multiple times!";

    @Override
    public AddGroupCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_NAME);

        if (!argMultimap.arePrefixesPresent(PREFIX_NAME)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }

        List<Index> indexes;

        try {
            indexes = ParserUtil.parseAllIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INVALID_INDEX_GIVEN), pe);
        }

        assert indexes.size() > 0; // indexes should not be empty, since preamble is non-empty and no parse exception

        // attempting to do add the same person to group multiple times!
        if (indexes.size() != indexes.stream().distinct().count()) {
            throw new ParseException(MESSAGE_DUPLICATE_INDEX);
        }

        GroupName groupName = ParserUtil.parseGroupName(argMultimap.getValue(PREFIX_NAME).get());

        Group group = new Group(groupName);

        return new AddGroupCommand(group, indexes);
    }
}
