package seedu.address.logic.parser.groups;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_INDEX_GIVEN;
import static seedu.address.logic.commands.CommandTestUtil.GROUP_DESC_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GROUP_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.groups.AddGroupCommand;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.testutil.GroupBuilder;

public class AddGroupCommandParserTest {

    private AddGroupCommandParser parser = new AddGroupCommandParser();

    @Test
    public void parse_validInput_returnsCorrectCommand() {
        Group expectedGroup = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();
        List<Index> expectedIndexes = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON));
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + " " + GROUP_DESC_TUTORIAL,
                new AddGroupCommand(expectedGroup, expectedIndexes));
    }

    @Test
    public void parse_invalidInput_failure() {
        // invalid indexes
        assertParseFailure(parser, "-1" + GROUP_DESC_TUTORIAL, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MESSAGE_INVALID_INDEX_GIVEN));
        assertParseFailure(parser, "abc" + GROUP_DESC_TUTORIAL, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MESSAGE_INVALID_INDEX_GIVEN));

        // invalid name
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + INVALID_GROUP_NAME_DESC,
                GroupName.MESSAGE_CONSTRAINTS);

        // repeated index
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + " " + INDEX_FIRST_PERSON.getOneBased()
                + GROUP_DESC_TUTORIAL, AddGroupCommandParser.MESSAGE_DUPLICATE_INDEX);
    }


}
