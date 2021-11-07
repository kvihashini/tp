package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.Model;
import seedu.address.model.TaskAssignable;
import seedu.address.model.id.HasUniqueId;
import seedu.address.model.id.UniqueId;
import seedu.address.model.id.exceptions.DuplicateIdException;
import seedu.address.model.id.exceptions.IdNotFoundException;
import seedu.address.model.lesson.Attendee;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.LessonAssignable;
import seedu.address.model.lesson.NoOverlapLessonList;
import seedu.address.model.lesson.exceptions.CannotAssignException;
import seedu.address.model.lesson.exceptions.OverlappingLessonsException;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */

public class Person implements HasUniqueId, Attendee,
        TaskAssignable, LessonAssignable {

    // Identity fields
    private final Name name;
    private final UniqueId id;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<UniqueId> assignedTaskIds = new HashSet<>();
    private final Map<UniqueId, Boolean> tasksCompletion = new HashMap<>();
    private final NoOverlapLessonList lessonsList;
    private final List<Exam> exams = new ArrayList<>();
    private final Set<UniqueId> assignedGroupIds = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  Set<UniqueId> assignedTaskIds, Map<UniqueId, Boolean> tasksCompletion,
                  NoOverlapLessonList lessonsList, List<Exam> exams, Set<UniqueId> assignedGroupIds) {
        this.id = UniqueId.generateId(this);
        requireAllNonNull(name, phone, email, address, tags, assignedTaskIds, id, lessonsList, exams, assignedGroupIds);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.assignedTaskIds.addAll(assignedTaskIds);
        this.tasksCompletion.putAll(tasksCompletion);
        this.lessonsList = lessonsList;
        this.exams.addAll(exams);
        this.assignedGroupIds.addAll(assignedGroupIds);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(UniqueId uniqueId, Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  Set<UniqueId> assignedTaskIds, Map<UniqueId, Boolean> tasksCompletion,
                  NoOverlapLessonList lessonsList, List<Exam> exams, Set<UniqueId> assignedGroupIds) {
        requireAllNonNull(name, phone, email, address, tags, assignedTaskIds, uniqueId, lessonsList, exams,
                assignedGroupIds);
        this.id = uniqueId;
        uniqueId.setOwner(this);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.assignedTaskIds.addAll(assignedTaskIds);
        this.tasksCompletion.putAll(tasksCompletion);
        this.lessonsList = lessonsList;
        this.exams.addAll(exams);
        this.assignedGroupIds.addAll(assignedGroupIds);
    }

    /**
     * Constructs a person based on anther person immutably
     * @param toCopy person to copy
     */
    public Person (Person toCopy) {
        this.id = toCopy.id;
        this.name = toCopy.name;
        this.phone = toCopy.phone;
        this.email = toCopy.email;
        this.address = toCopy.address;
        this.tags.addAll(toCopy.tags);
        this.assignedTaskIds.addAll(toCopy.assignedTaskIds);
        this.tasksCompletion.putAll(toCopy.tasksCompletion);
        this.lessonsList = toCopy.lessonsList;
        this.exams.addAll(toCopy.exams);
        this.assignedGroupIds.addAll(toCopy.assignedGroupIds);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public UniqueId getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<UniqueId> getAssignedTaskIds() {
        return Collections.unmodifiableSet(assignedTaskIds);
    }

    /**
     * Method to get the map of assigned tasks and their completion status
     *
     * @return the Person's tasksCompletion map
     */
    @Override
    public Map<UniqueId, Boolean> getTasksCompletion() {
        return Collections.unmodifiableMap(tasksCompletion);
    }

    public NoOverlapLessonList getLessonsList() {
        return lessonsList;
    }

    public List<Exam> getExams() {
        return Collections.unmodifiableList(exams);
    }

    public Set<UniqueId> getAssignedGroupIds() {
        return Collections.unmodifiableSet(assignedGroupIds);
    }

    /**
     * Checks if this person contains the group id.
     *
     * @param id to check.
     * @return true if this person contains the id.
     */
    public boolean containsGroupId(UniqueId id) {
        return assignedGroupIds.contains(id);
    }

    @Override
    public boolean canAssignLesson(Lesson lesson) {
        return !lessonsList.doesLessonOverlap(lesson);
    }

    @Override
    public String getAttendeeDetails() {
        return name.toString();
    }

    @Override
    public Person assignLesson(Lesson lesson) throws CannotAssignException {
        NoOverlapLessonList newList;
        try {
            newList = lessonsList.addLesson(lesson);
        } catch (OverlappingLessonsException e) {
            throw new CannotAssignException(e.getMessage());
        }

        return new Person(id, name, phone, email, address, tags, assignedTaskIds, tasksCompletion,
                newList, exams, assignedGroupIds);
    }

    @Override
    public boolean isValidLessonIndex(int index) {
        return lessonsList.isValidIndex(index);
    }

    @Override
    public Person unassignLesson(int index) throws IndexOutOfBoundsException {
        NoOverlapLessonList newList = lessonsList.removeLesson(index);
        return new Person(id, name, phone, email, address, tags, assignedTaskIds, tasksCompletion,
                newList, exams, assignedGroupIds);
    }

    @Override
    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessonsList.getLessons());
    }

    @Override
    public LessonAssignable setLessons(List<Lesson> lessons) throws CannotAssignException {
        if (NoOverlapLessonList.doAnyLessonsOverlap(lessons)) {
            throw new CannotAssignException(OverlappingLessonsException.MESSAGE);
        }
        NoOverlapLessonList newList = NoOverlapLessonList.of(lessons);
        return new Person(id, name, phone, email, address, tags, assignedTaskIds, tasksCompletion,
                newList, exams, assignedGroupIds);
    }

    /**
     * Immutable way of adding an exam
     * @param e exam to add
     * @return Person with exam added
     */
    public Person addExam(Exam e) {
        Person newPerson = new Person(this);
        newPerson.exams.add(e);
        return newPerson;
    }

    /**
     * Immutable way of removing an exam
     * @param index of exam to remove
     * @return Person with examed removed
     * @throws IndexOutOfBoundsException if specified index is out of bounds
     */
    public Person removeExam(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= exams.size()) {
            throw new IndexOutOfBoundsException();
        }
        Person newPerson = new Person(this);
        newPerson.exams.remove(index);
        return newPerson;
    }

    /**
     * Checks if this person has the specified group id
     * @param id to check
     * @return true if present
     */
    public boolean hasGroupId(UniqueId id) {
        return assignedGroupIds.contains(id);
    }

    /**
     * Adds the group id to the person. Group id is presumed to belong to a group.
     * @param id to add.
     * @return new Person containing the added id.
     */
    public Person addGroupId(UniqueId id) {
        if (assignedGroupIds.contains(id)) {
            throw new DuplicateIdException();
        }
        Person newPerson = new Person(this);
        newPerson.assignedGroupIds.add(id);
        return newPerson;
    }

    /**
     * Remove the group id from the person.
     * @param id to remove.
     * @return new Person with id removed.
     */
    public Person removeGroupId(UniqueId id) {
        if (!assignedGroupIds.contains(id)) {
            throw new IdNotFoundException(id);
        }
        Person newPerson = new Person(this);
        newPerson.assignedGroupIds.remove(id);
        return newPerson;
    }

    @Override
    public String getNameInString() {
        return name.toString();
    }

    /**
     * Immutable way of updating the assigned task id list
     *
     * @param newAssignedTaskIds the new assigned task id list
     * @return new Person instance with the updated assigned task id list
     */
    @Override
    public Person updateAssignedTaskIds(Set<UniqueId> newAssignedTaskIds) {
        requireNonNull(newAssignedTaskIds);
        return new Person(id, name, phone, email, address, tags, newAssignedTaskIds, tasksCompletion,
                lessonsList, exams, assignedGroupIds);
    }

    /**
     * Immutable way of updating the task completion map
     *
     * @param newTasksCompletion the new task completion map
     * @return new Person instance with the updated task completion map
     */
    @Override
    public Person updateTasksCompletion(Map<UniqueId, Boolean> newTasksCompletion) {
        requireNonNull(newTasksCompletion);
        return new Person(id, name, phone, email, address, tags, assignedTaskIds, newTasksCompletion,
                lessonsList, exams, assignedGroupIds);
    }

    @Override
    public boolean isSameTaskAssignable(TaskAssignable otherTaskAssignable) {
        if (!(otherTaskAssignable instanceof Person)) {
            return false;
        }

        return isSamePerson((Person) otherTaskAssignable);
    }

    @Override
    public boolean isInModel(Model model) {
        return model.hasPerson(this);
    }

    /**
     * Returns true if both persons have the same name, phone and email.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail());
    }

    /**
     * Returns true if both persons have exactly the same fields, possibly except the id.
     */
    public boolean hasSameData(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;

        return otherPerson.isSamePerson(this)
                && otherPerson.getAddress().equals(address)
                && otherPerson.getTags().equals(tags)
                && otherPerson.getAssignedTaskIds().equals(assignedTaskIds)
                && otherPerson.getAssignedGroupIds().equals(assignedGroupIds)
                && otherPerson.getTasksCompletion().equals(tasksCompletion)
                && otherPerson.getLessonsList().equals(lessonsList)
                && otherPerson.getExams().equals(exams);
    }

    /**
     * Returns true if both persons have the same identity fields and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;

        return otherPerson.hasSameData(this)
                && otherPerson.getId().equals(id);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, id, phone, email, address, tags, lessonsList, exams);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Phone: ")
                .append(getPhone())
                .append("; Email: ")
                .append(getEmail())
                .append("; Address: ")
                .append(getAddress());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }
        return builder.toString();
    }

}
