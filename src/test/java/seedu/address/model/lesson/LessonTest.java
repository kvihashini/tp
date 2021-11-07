package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLessons.MON_10_12_BIOLOGY;
import static seedu.address.testutil.TypicalLessons.MON_11_13_MATH;
import static seedu.address.testutil.TypicalLessons.MON_16_18_MATH;
import static seedu.address.testutil.TypicalLessons.TUE_16_18_ENGLISH;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.LessonBuilder;

class LessonTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Lesson(null, null, null));
    }

    @Test
    void doLessonsOverlap_overlappingLessons_returnsTrue() {
        // overlapping timing same day
        assertTrue(MON_10_12_BIOLOGY.doLessonsOverlap(MON_11_13_MATH));
    }

    @Test
    void doLessonsOverlap_noOverlapLessons_returnsFalse() {
        // totally different
        assertFalse(MON_10_12_BIOLOGY.doLessonsOverlap(TUE_16_18_ENGLISH));
        // same timing different day
        assertFalse(MON_16_18_MATH.doLessonsOverlap(TUE_16_18_ENGLISH));
        // same day different timing
        assertFalse(MON_10_12_BIOLOGY.doLessonsOverlap(MON_16_18_MATH));
        // same name different timing
        assertFalse(MON_11_13_MATH.doLessonsOverlap(MON_16_18_MATH));
    }

    @Test
    void compareToTest() {
        // Mon 10-12 is earlier than Tue 16-18
        assertEquals(MON_10_12_BIOLOGY.compareTo(TUE_16_18_ENGLISH), -1);

        // 16:00-18:00 is later than 10:00 - 12:00
        assertEquals(MON_16_18_MATH.compareTo(MON_10_12_BIOLOGY), 1);

        // equal
        assertEquals(MON_10_12_BIOLOGY.compareTo(MON_10_12_BIOLOGY), 0);
    }

    @Test
    void testEquals() {
        // same details
        Lesson lesson = new LessonBuilder().withTimeslot("10:00", "14:00").withSubject("Math").withDayOfWeek(5).build();
        Lesson sameDetails = new LessonBuilder(lesson).build();
        assertTrue(lesson.equals(sameDetails));

        // different timeslot
        Lesson differentTimeslot = new LessonBuilder(lesson).withTimeslot("12:00", "16:00").build();
        assertFalse(lesson.equals(differentTimeslot));

        // different subject
        Lesson differentSubject = new LessonBuilder(lesson).withSubject("Biology").build();
        assertFalse(lesson.equals(differentSubject));

        // different day of week
        Lesson differentDay = new LessonBuilder(lesson).withDayOfWeek(2).build();
        assertFalse(lesson.equals(differentDay));
    }
}
