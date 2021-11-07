package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLessons.MON_10_12_BIOLOGY;
import static seedu.address.testutil.TypicalLessons.MON_11_13_MATH;
import static seedu.address.testutil.TypicalLessons.MON_16_18_MATH;
import static seedu.address.testutil.TypicalLessons.TUE_16_18_ENGLISH;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;


public class NoOverlapLessonListTest {

    @Test
    void doAnyLessonsOverlap_noOverlap_returnFalse() {
        List<Lesson> lessonList = Arrays.asList(MON_10_12_BIOLOGY, MON_16_18_MATH, TUE_16_18_ENGLISH);
        assertFalse(NoOverlapLessonList.doAnyLessonsOverlap(lessonList));
    }

    @Test
    void doAnyLessonsOverlap_overlaps_returnTrue() {
        List<Lesson> lessonList = Arrays.asList(MON_10_12_BIOLOGY, MON_16_18_MATH, TUE_16_18_ENGLISH,
                MON_11_13_MATH);
        assertTrue(NoOverlapLessonList.doAnyLessonsOverlap(lessonList));
    }

    @Test
    void doesLessonOverlap_overlappingLesson_returnTrue() {
        List<Lesson> lessonList = Arrays.asList(MON_10_12_BIOLOGY, MON_16_18_MATH, TUE_16_18_ENGLISH);
        NoOverlapLessonList noOverlapLessonList = NoOverlapLessonList.of(lessonList);
        // test above list with new lesson below
        assertTrue(noOverlapLessonList.doesLessonOverlap(MON_11_13_MATH));
    }

    @Test
    void isValidIndex() {
        List<Lesson> lessonList = Arrays.asList(MON_10_12_BIOLOGY, MON_16_18_MATH, TUE_16_18_ENGLISH);
        NoOverlapLessonList noOverlapLessonList = NoOverlapLessonList.of(lessonList);
        assertFalse(noOverlapLessonList.isValidIndex(-1));
        assertTrue(noOverlapLessonList.isValidIndex(0));
        assertTrue(noOverlapLessonList.isValidIndex(2));
        assertFalse(noOverlapLessonList.isValidIndex(100));
    }

    @Test
    void addLesson_validLesson_returnsNewInstanceWithLesson() {
        List<Lesson> lessonList = Arrays.asList(MON_10_12_BIOLOGY, MON_16_18_MATH);
        NoOverlapLessonList noOverlapLessonList = NoOverlapLessonList.of(lessonList);
        NoOverlapLessonList addedList = noOverlapLessonList.addLesson(TUE_16_18_ENGLISH);
        assertEquals(addedList.getLessons().get(2), TUE_16_18_ENGLISH);
    }

    @Test
    void addLesson_nullLesson_throwsNullPointerException() {
        NoOverlapLessonList lessonList = new NoOverlapLessonList();
        assertThrows(NullPointerException.class, () -> lessonList.addLesson(null));
    }

    @Test
    void removeLesson_validIndex_returnsNewInstanceWithoutLesson() {
        List<Lesson> lessonList = Arrays.asList(MON_10_12_BIOLOGY, MON_16_18_MATH, TUE_16_18_ENGLISH);
        NoOverlapLessonList noOverlapLessonList = NoOverlapLessonList.of(lessonList);
        NoOverlapLessonList removedList = noOverlapLessonList.removeLesson(2);
        assertEquals(removedList.getLessons().size(), 2);
    }

    @Test
    void removeLesson_inValidIndex_throwsIndexOutOfBoundsException() {
        NoOverlapLessonList lessonList = new NoOverlapLessonList();
        assertThrows(IndexOutOfBoundsException.class, () -> lessonList.removeLesson(1));
    }
}

