package seedu.address.logic.parser.lessons;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_INDEX_GIVEN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.time.DayOfWeek;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.Subject;
import seedu.address.model.lesson.Timeslot;

public class AddLessonParserUtil {
    private final Lesson lesson;
    private final Index index;

    /**
     * Constructor that holds a lesson and index
     */
    private AddLessonParserUtil(Lesson lesson, Index index) {
        requireNonNull(lesson);
        requireNonNull(index);
        this.lesson = lesson;
        this.index = index;
    }

    /**
     * Parses a add lesson command and returns an object containing the lesson and index
     * @param userInput to parse
     * @return AddLessonParseUtil object with the lesson and index.
     * @throws ParseException if any fields are wrong.
     */
    public static AddLessonParserUtil parseAddLesson(String userInput, String messageUsage) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_SUBJECT, PREFIX_START_TIME, PREFIX_END_TIME, PREFIX_DAY);

        if (!argMultimap.arePrefixesPresent(PREFIX_SUBJECT, PREFIX_START_TIME, PREFIX_END_TIME, PREFIX_DAY)
                || argMultimap.getPreamble().isEmpty() || !argMultimap.preambleHasExpectedSegments(1)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    messageUsage));
        }

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_INVALID_INDEX_GIVEN), pe);
        }

        Timeslot timeslot = ParserUtil.parseTimeslot(argMultimap.getValue(PREFIX_START_TIME).get(),
                argMultimap.getValue(PREFIX_END_TIME).get());
        Subject subject = ParserUtil.parseSubject(argMultimap.getValue(PREFIX_SUBJECT).get());
        DayOfWeek dayOfWeek = ParserUtil.parseDayOfWeek(argMultimap.getValue(PREFIX_DAY).get());
        Lesson lesson = new Lesson(timeslot, subject, dayOfWeek);
        return new AddLessonParserUtil(lesson, index);
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Index getIndex() {
        return index;
    }
}
