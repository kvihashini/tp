package seedu.address.logic.parser.persons;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.INVALID_COMMAND_INVALID_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EXAM;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.persons.EditPersonCommand;

public class PersonRemoveExamParserTest {

    private static final String INVALID_FORMAT = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            PersonRemoveExamParser.MESSAGE_USAGE);

    private PersonRemoveExamParser parser = new PersonRemoveExamParser();

    @Test
    public void parse_validArgs_returnsCorrectCommand() {
        EditPersonCommand.EditPersonDescriptor descriptor = new EditPersonCommand.EditPersonDescriptor();
        descriptor.removeExam(INDEX_FIRST_EXAM);
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + " " + INDEX_FIRST_EXAM.getOneBased(),
                new EditPersonCommand(INDEX_FIRST_PERSON, descriptor, PersonRemoveExamParser.MESSAGE_SUCCESS));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // empty
        assertParseFailure(parser, "", INVALID_FORMAT);

        // missing index
        assertParseFailure(parser, " " + INDEX_FIRST_PERSON.getOneBased(), INVALID_FORMAT);

        // one invalid index
        assertParseFailure(parser, "..." + " " + INDEX_FIRST_EXAM.getOneBased(), INVALID_COMMAND_INVALID_INDEX);

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + " " + "abc", INVALID_COMMAND_INVALID_INDEX);
    }
}
