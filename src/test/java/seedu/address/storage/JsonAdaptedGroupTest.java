package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedGroup.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalGroups.GROUP_1;
import static seedu.address.testutil.TypicalLessons.MON_10_12_BIOLOGY;
import static seedu.address.testutil.TypicalLessons.MON_11_13_MATH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.group.GroupName;
import seedu.address.model.id.UniqueId;
import seedu.address.model.lesson.NoOverlapLessonList;

public class JsonAdaptedGroupTest {

    private static final String INVALID_NAME = "M@them@tics";

    private static final String VALID_ID = GROUP_1.getId().toString();
    private static final String VALID_NAME = GROUP_1.getName().toString();
    private static final List<JsonAdaptedUniqueId> VALID_ASSIGNED_TASK_IDS = new ArrayList<>();
    private static final List<JsonAdaptedUniqueId> VALID_ASSIGNED_PERSON_IDS = new ArrayList<>();
    private static final List<JsonAdaptedLesson> VALID_LESSONS = GROUP_1.getLessons()
            .stream().map(JsonAdaptedLesson::new).collect(Collectors.toList());

    @Test
    public void toModelType_validGroupDetails_returnsGroup() throws Exception {
        JsonAdaptedGroup group = new JsonAdaptedGroup(GROUP_1);
        assertEquals(GROUP_1, group.toModelType());
    }

    @Test
    public void toModelType_nullId_throwsIllegalValueException() {
        JsonAdaptedGroup group = new JsonAdaptedGroup(null, VALID_NAME, VALID_ASSIGNED_TASK_IDS,
                VALID_ASSIGNED_PERSON_IDS, VALID_LESSONS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, UniqueId.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, group::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedGroup group = new JsonAdaptedGroup(VALID_ID, INVALID_NAME, VALID_ASSIGNED_TASK_IDS,
                VALID_ASSIGNED_PERSON_IDS, VALID_LESSONS);
        String expectedMessage = GroupName.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, group::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedGroup group = new JsonAdaptedGroup(VALID_ID, null, VALID_ASSIGNED_TASK_IDS,
                VALID_ASSIGNED_PERSON_IDS, VALID_LESSONS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, GroupName.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, group::toModelType);
    }

    @Test
    void toModelType_overlappingList_throwsIllegalValueException() {
        List<JsonAdaptedLesson> overlappingLessons = Arrays.asList(new JsonAdaptedLesson(MON_10_12_BIOLOGY),
                new JsonAdaptedLesson(MON_11_13_MATH));
        JsonAdaptedGroup group = new JsonAdaptedGroup(VALID_ID, VALID_NAME, VALID_ASSIGNED_TASK_IDS,
                VALID_ASSIGNED_PERSON_IDS, overlappingLessons);
        assertThrows(IllegalValueException.class, NoOverlapLessonList.LESSON_OVERLAP, group::toModelType);
    }


}
