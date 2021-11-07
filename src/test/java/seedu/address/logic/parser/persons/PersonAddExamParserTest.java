package seedu.address.logic.parser.persons;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DATE_TIME_DESC1;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_TIME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SUBJECT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_TIME_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_MATH;
import static seedu.address.logic.parser.CommandParserTestUtil.INVALID_COMMAND_INVALID_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.INVALID_DATE_TIME_FORMAT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.persons.EditPersonCommand;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.Subject;
import seedu.address.model.person.Exam;

class PersonAddExamParserTest {

    private static final String MESSAGE_INVALID_FORMAT = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            PersonAddExamParser.MESSAGE_USAGE);

    private static final String VALID_INDEX = String.format("%d", INDEX_FIRST_PERSON.getOneBased());

    private PersonAddExamParser parser = new PersonAddExamParser();

    @Test
    public void parse_validFields_success() throws ParseException {
        Exam expectedExam = new Exam(ParserUtil.parseSubject(VALID_SUBJECT_MATH),
                ParserUtil.parseLocalDateTime(VALID_DATE_TIME_1));
        EditPersonCommand.EditPersonDescriptor descriptor = new EditPersonCommand.EditPersonDescriptor();
        descriptor.addExam(expectedExam);

        assertParseSuccess(parser, VALID_INDEX + SUBJECT_DESC_MATH + DATE_TIME_DESC1,
                new EditPersonCommand(INDEX_FIRST_PERSON, descriptor, PersonAddExamParser.ADD_EXAM_SUCCESS));
    }

    @Test
    public void parse_missingFields_failure() {
        // no preamble
        assertParseFailure(parser, SUBJECT_DESC_MATH + DATE_TIME_DESC1, MESSAGE_INVALID_FORMAT);

        // no subject
        assertParseFailure(parser, VALID_INDEX + DATE_TIME_DESC1, MESSAGE_INVALID_FORMAT);

        // no date
        assertParseFailure(parser, VALID_INDEX + SUBJECT_DESC_MATH, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidFields_failure() {
        // empty
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);

        // invalid index
        assertParseFailure(parser, "-1" + SUBJECT_DESC_MATH + DATE_TIME_DESC1, INVALID_COMMAND_INVALID_INDEX);
        assertParseFailure(parser, "abc" + SUBJECT_DESC_MATH + DATE_TIME_DESC1, INVALID_COMMAND_INVALID_INDEX);

        // invalid subject
        assertParseFailure(parser, VALID_INDEX + INVALID_SUBJECT_DESC + DATE_TIME_DESC1, Subject.MESSAGE_CONSTRAINTS);

        // invalid date time
        assertParseFailure(parser, VALID_INDEX + SUBJECT_DESC_MATH + INVALID_DATE_TIME_DESC, INVALID_DATE_TIME_FORMAT);
    }

}
