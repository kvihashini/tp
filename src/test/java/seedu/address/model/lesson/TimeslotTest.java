package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalTimeslots.ELEVEN_TO_ONE;
import static seedu.address.testutil.TypicalTimeslots.TEN_TO_TWELVE;
import static seedu.address.testutil.TypicalTimeslots.TWELVE_TO_TWO;

import org.junit.jupiter.api.Test;

class TimeslotTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Timeslot(null, null));
    }

    @Test
    public void constructor_invalidTimings_throwsIllegalArgumentException() {
        // invalid format
        assertThrows(IllegalArgumentException.class, () -> new Timeslot("....", "._."));
        // end time earlier than start time
        assertThrows(IllegalArgumentException.class, () -> new Timeslot("14:00", "12:00"));
    }

    @Test
    void isValidTimeslot_invalidTimings_returnsFalse() {
        assertFalse(Timeslot.isValidTimeslot("hahahaha", "thisiswrong"));
        assertFalse(Timeslot.isValidTimeslot("15:00", "14:00"));
    }

    @Test
    void isValidTimeslot_validTimings_returnsTrue() {
        assertTrue(Timeslot.isValidTimeslot("14:00", "15:00"));
        assertTrue(Timeslot.isValidTimeslot("15:00", "15:01"));
    }

    @Test
    void doTimingsOverlap_overlappingTimeslots_returnsTrue() {
        assertTrue(TEN_TO_TWELVE.doTimingsOverlap(ELEVEN_TO_ONE));
    }

    @Test
    void doTimingsOverlap_noOverlapTimeslots_returnsFalse() {
        assertFalse(TEN_TO_TWELVE.doTimingsOverlap(TWELVE_TO_TWO));
    }

    @Test
    void testEquals_sameTimings_returnsTrue() {
        Timeslot t1 = new Timeslot("10:00", "12:00");
        Timeslot t2 = new Timeslot("10:00", "12:00");
        assertEquals(t1, t2);
    }

    @Test
    void testEquals_sameTimings_returnsFalse() {
        Timeslot timeSlot = new Timeslot("10:00", "12:00");
        Timeslot differentEnd = new Timeslot("10:00", "11:00");
        Timeslot differentStart = new Timeslot("11:00", "12:00");
        assertNotEquals(timeSlot, differentEnd);
        assertNotEquals(timeSlot, differentStart);
    }
}
