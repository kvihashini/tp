package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLessons.MON_10_12_BIOLOGY;
import static seedu.address.testutil.TypicalLessons.MON_11_13_MATH;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.id.UniqueId;
import seedu.address.model.lesson.NoOverlapLessonList;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_ID = BENSON.getId().toString();
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedUniqueId> VALID_ASSIGNED_TASK_IDS = new ArrayList<>();
    private static final List<JsonAdaptedUniqueId> VALID_ASSIGNED_GROUP_IDS = new ArrayList<>();
    private static final List<JsonAdaptedTaskCompletion> VALID_TASKS_COMPLETION = new ArrayList<>();
    private static final List<JsonAdaptedLesson> VALID_LESSONS = BENSON.getLessonsList()
            .getLessons()
            .stream().map(JsonAdaptedLesson::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedExam> VALID_EXAMS = BENSON.getExams().stream()
            .map(JsonAdaptedExam::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_nullId_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_TAGS, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                VALID_TASKS_COMPLETION, VALID_LESSONS, VALID_EXAMS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, UniqueId.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                        VALID_TASKS_COMPLETION, VALID_LESSONS, VALID_EXAMS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                VALID_TASKS_COMPLETION, VALID_LESSONS, VALID_EXAMS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                        VALID_TASKS_COMPLETION, VALID_LESSONS, VALID_EXAMS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                VALID_TASKS_COMPLETION, VALID_LESSONS, VALID_EXAMS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                        VALID_TASKS_COMPLETION, VALID_LESSONS, VALID_EXAMS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                VALID_TAGS, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                VALID_TASKS_COMPLETION, VALID_LESSONS, VALID_EXAMS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                        VALID_TAGS, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                        VALID_TASKS_COMPLETION, VALID_LESSONS, VALID_EXAMS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_TAGS, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                VALID_TASKS_COMPLETION, VALID_LESSONS, VALID_EXAMS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        invalidTags, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                        VALID_TASKS_COMPLETION, VALID_LESSONS, VALID_EXAMS);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    void toModelType_overlappingList_throwsIllegalValueException() {
        List<JsonAdaptedLesson> overlappingLessons = Arrays.asList(new JsonAdaptedLesson(MON_10_12_BIOLOGY),
                new JsonAdaptedLesson(MON_11_13_MATH));
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_TAGS, VALID_ASSIGNED_TASK_IDS, VALID_ASSIGNED_GROUP_IDS,
                VALID_TASKS_COMPLETION, overlappingLessons, VALID_EXAMS);
        assertThrows(IllegalValueException.class, NoOverlapLessonList.LESSON_OVERLAP, person::toModelType);
    }

}
