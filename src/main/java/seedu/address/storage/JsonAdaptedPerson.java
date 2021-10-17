package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.id.UniqueId;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.NoOverlapLessonList;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Exam;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String uniqueId;
    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();
    private final List<JsonAdaptedUniqueId> assignedTaskIds = new ArrayList<>();
    private final List<JsonAdaptedLesson> lessonsList = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("uniqueId") String uniqueId, @JsonProperty("name") String name,
            @JsonProperty("phone") String phone, @JsonProperty("email") String email,
            @JsonProperty("address") String address, @JsonProperty("tagged") List<JsonAdaptedTag> tagged,
            @JsonProperty("assignedTaskIds") List<JsonAdaptedUniqueId> assignedTaskIds,
            @JsonProperty("lessonsList") List<JsonAdaptedLesson> lessonsList) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
        if (assignedTaskIds != null) {
            this.assignedTaskIds.addAll(assignedTaskIds);
        }
        if (lessonsList != null) {
            this.lessonsList.addAll(lessonsList);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        uniqueId = source.getId().getUuid().toString();
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        assignedTaskIds.addAll(source.getAssignedTaskIds().stream()
                .map(JsonAdaptedUniqueId::new)
                .collect(Collectors.toList()));
        lessonsList.addAll(source.getLessonsList().getLessons().stream()
                .map(JsonAdaptedLesson::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        final List<UniqueId> personAssignedTaskIds = new ArrayList<>();
        for (JsonAdaptedUniqueId id : assignedTaskIds) {
            personAssignedTaskIds.add(id.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Set<UniqueId> modelAssignedTaskIds = new HashSet<>(personAssignedTaskIds);

        final List<Lesson> modelLessonsList = new ArrayList<>();
        for (JsonAdaptedLesson l : lessonsList) {
            modelLessonsList.add(l.toModelType());
        }

        if (NoOverlapLessonList.doAnyLessonsOverlap(modelLessonsList)) {
            throw new IllegalValueException(NoOverlapLessonList.LESSON_OVERLAP);
        }

        NoOverlapLessonList lessonsList = NoOverlapLessonList.of(modelLessonsList);

        if (uniqueId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    UniqueId.class.getSimpleName()));
        }
        final UniqueId modelUniqueId = UniqueId.generateId(uniqueId);

        // TODO actually store exams in storage
        List<Exam> exams = new ArrayList<>();

        return new Person(modelUniqueId, modelName, modelPhone, modelEmail,
                modelAddress, modelTags, modelAssignedTaskIds, lessonsList, exams);
    }

}
