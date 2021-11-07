package seedu.address.logic.parser.persons;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DAY_MON;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DAY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SUBJECT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIMESLOT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_BIO;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.TIMESLOT_DESC_10_12;
import static seedu.address.logic.commands.CommandTestUtil.TIMESLOT_DESC_10_14;
import static seedu.address.logic.commands.CommandTestUtil.TIMESLOT_DESC_12_14;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_MON;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_10;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_12;
import static seedu.address.logic.parser.CommandParserTestUtil.INVALID_COMMAND_INVALID_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.persons.EditPersonCommand;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.Subject;
import seedu.address.model.lesson.Timeslot;
import seedu.address.testutil.LessonBuilder;

class PersonAddLessonParserTest {

    private PersonAddLessonParser parser = new PersonAddLessonParser();

    private final Index targetIndex = INDEX_FIRST_PERSON;

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        Lesson expectedLesson = new LessonBuilder().withSubject(VALID_SUBJECT_MATH)
                .withTimeslot(VALID_TIME_10, VALID_TIME_12).withDayOfWeek(ParserUtil.parseDayOfWeek(VALID_DAY_MON))
                .build();

        EditPersonCommand.EditPersonDescriptor editPersonDesc = new EditPersonCommand.EditPersonDescriptor();
        editPersonDesc.addLesson(expectedLesson);
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + targetIndex.getOneBased() + SUBJECT_DESC_MATH
                + TIMESLOT_DESC_10_12 + DAY_MON, new EditPersonCommand(targetIndex, editPersonDesc,
                PersonAddLessonParser.ADD_LESSON_SUCCESS));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, PersonAddLessonParser.MESSAGE_USAGE);
        Index targetIndex = INDEX_FIRST_PERSON;

        // no index
        assertParseFailure(parser, PREAMBLE_WHITESPACE + SUBJECT_DESC_MATH + DAY_MON
                + TIMESLOT_DESC_10_12, expectedMessage);

        // no day
        assertParseFailure(parser, PREAMBLE_WHITESPACE + targetIndex.getOneBased() + SUBJECT_DESC_MATH
                + TIMESLOT_DESC_10_12, expectedMessage);

        // no subject
        assertParseFailure(parser, PREAMBLE_WHITESPACE + targetIndex.getOneBased() + DAY_MON
                + TIMESLOT_DESC_10_14, expectedMessage);

        // no timeslot
        assertParseFailure(parser, PREAMBLE_WHITESPACE + targetIndex.getOneBased() + DAY_MON
                + SUBJECT_DESC_MATH, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;

        assertParseFailure(parser, PREAMBLE_WHITESPACE + "abc" + SUBJECT_DESC_MATH + TIMESLOT_DESC_10_12
                + DAY_MON, INVALID_COMMAND_INVALID_INDEX);

        assertParseFailure(parser, PREAMBLE_WHITESPACE + targetIndex.getOneBased() + INVALID_SUBJECT_DESC
                + TIMESLOT_DESC_10_12 + DAY_MON, Subject.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, PREAMBLE_WHITESPACE + targetIndex.getOneBased() + SUBJECT_DESC_BIO
                + INVALID_TIMESLOT_DESC + DAY_MON, Timeslot.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, PREAMBLE_WHITESPACE + targetIndex.getOneBased() + SUBJECT_DESC_BIO
                + TIMESLOT_DESC_12_14 + INVALID_DAY_DESC, ParserUtil.MESSAGE_INVALID_DAY);
    }
}
