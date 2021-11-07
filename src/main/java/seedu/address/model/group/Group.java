package seedu.address.model.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

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
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.LessonAssignable;
import seedu.address.model.lesson.NoOverlapLessonList;
import seedu.address.model.lesson.exceptions.CannotAssignException;
import seedu.address.model.lesson.exceptions.OverlappingLessonsException;

/**
 * Represents a Group in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Group implements HasUniqueId, TaskAssignable, LessonAssignable {

    // Identity fields
    private final GroupName name;

    // The id of the task
    private final UniqueId id;

    // The id of tasks assigned to the group
    private final Set<UniqueId> assignedTaskIds = new HashSet<>();

    // the id of persons assigned to this group.
    private final Set<UniqueId> assignedPersonIds = new HashSet<>();

    // lessons belonging to this group.
    private final NoOverlapLessonList lessonList;

    /**
     * Every field must be present and not null.
     */
    public Group(GroupName name) {
        this.id = UniqueId.generateId(this);
        requireAllNonNull(name);
        this.name = name;
        this.lessonList = new NoOverlapLessonList();
    }

    /**
     * Every field must be present and not null.
     */
    public Group(GroupName name, UniqueId id) {
        requireAllNonNull(name, id);
        this.id = id;
        this.name = name;
        id.setOwner(this);
        this.lessonList = new NoOverlapLessonList();
    }

    /**
     * Every field must be present and not null.
     */
    public Group(GroupName name, UniqueId id, Set<UniqueId> assignedTaskIds, Set<UniqueId> assignedPersonIds,
                 NoOverlapLessonList lessonList) {
        requireAllNonNull(name, id, assignedTaskIds, assignedPersonIds);
        this.id = id;
        this.name = name;
        this.assignedTaskIds.addAll(assignedTaskIds);
        this.assignedPersonIds.addAll(assignedPersonIds);
        this.lessonList = lessonList;
        id.setOwner(this);
    }

    /**
     * Constructs a group by copying details from the given group
     *
     * @param toCopy to copy
     */
    public Group(Group toCopy) {
        this.id = toCopy.id;
        this.name = toCopy.name;
        this.assignedTaskIds.addAll(toCopy.assignedTaskIds);
        this.assignedPersonIds.addAll(toCopy.assignedPersonIds);
        this.lessonList = toCopy.lessonList;
        id.setOwner(this);
    }

    public GroupName getName() {
        return name;
    }

    public UniqueId getId() {
        return id;
    }

    public Set<UniqueId> getAssignedPersonIds() {
        return Collections.unmodifiableSet(assignedPersonIds);
    }

    /**
     * Checks if this group contains the person id.
     *
     * @param id to check.
     * @return true if this group contains the id.
     */
    public boolean containsPersonId(UniqueId id) {
        return assignedPersonIds.contains(id);
    }

    /**
     * Adds an id to the set of unique Ids, the id is presumed to belong to a person.
     *
     * @param id to add
     * @return new Group with added person id
     * @throws DuplicateIdException if the id was previously added
     */
    public Group addPersonId(UniqueId id) {
        if (assignedPersonIds.contains(id)) {
            throw new DuplicateIdException();
        }
        Group newGroup = new Group(this);
        newGroup.assignedPersonIds.add(id);
        return newGroup;
    }

    /**
     * Removes an id from the set of unique Ids.
     *
     * @param id to remove
     * @return new Group with removed person id
     * @throws IdNotFoundException if the id is not found
     */
    public Group removePersonId(UniqueId id) {
        if (!assignedPersonIds.contains(id)) {
            throw new IdNotFoundException(id);
        }
        Group newGroup = new Group(this);
        newGroup.assignedPersonIds.remove(id);
        return newGroup;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    @Override
    public Set<UniqueId> getAssignedTaskIds() {
        return Collections.unmodifiableSet(assignedTaskIds);
    }

    /**
     * Returns only an empty unmodifiable map since class does not support task completion
     */
    @Override
    public Map<UniqueId, Boolean> getTasksCompletion() {
        return Collections.unmodifiableMap(new HashMap<>());
    }

    /**
     * Immutable way of updating the assigned task id list
     *
     * @param newAssignedTaskIds the new assigned task id list
     * @return new Person instance with the updated assigned task id list
     */
    @Override
    public Group updateAssignedTaskIds(Set<UniqueId> newAssignedTaskIds) {
        requireNonNull(newAssignedTaskIds);
        return new Group(name, id, newAssignedTaskIds, assignedPersonIds, lessonList);
    }

    /**
     * Returns an identical copy of the Group since class does not support task completion
     */
    @Override
    public Group updateTasksCompletion(Map<UniqueId, Boolean> newTasksCompletion) {
        return new Group(name, id, assignedTaskIds, assignedPersonIds, lessonList);
    }

    /**
     * Immutable way of updating the assigned person ids
     *
     * @param ids the new person ids
     * @return new Group instance with the updated assigned person id list
     */
    public Group updateAssignedPersonIds(Set<UniqueId> ids) {
        requireNonNull(ids);
        return new Group(name, id, assignedTaskIds, ids, lessonList);
    }

    @Override
    public String getNameInString() {
        return name.toString();
    }

    @Override
    public boolean isSameTaskAssignable(TaskAssignable otherTaskAssignable) {
        if (!(otherTaskAssignable instanceof Group)) {
            return false;
        }

        return isSameGroup((Group) otherTaskAssignable);
    }

    @Override
    public boolean isInModel(Model model) {
        return model.hasGroup(this);
    }


    @Override
    public boolean canAssignLesson(Lesson lesson) {
        return !lessonList.doesLessonOverlap(lesson);
    }

    @Override
    public Group assignLesson(Lesson lesson) throws CannotAssignException {
        NoOverlapLessonList newList;
        try {
            newList = lessonList.addLesson(lesson);
        } catch (OverlappingLessonsException e) {
            throw new CannotAssignException(e.getMessage());
        }
        return new Group(name, id, assignedTaskIds, assignedPersonIds, newList);
    }

    @Override
    public boolean isValidLessonIndex(int index) {
        return lessonList.isValidIndex(index);
    }

    @Override
    public Group unassignLesson(int index) throws IndexOutOfBoundsException {
        NoOverlapLessonList newList = lessonList.removeLesson(index);
        return new Group(name, id, assignedTaskIds, assignedPersonIds, newList);
    }

    @Override
    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessonList.getLessons());
    }

    @Override
    public Group setLessons(List<Lesson> lessons) throws CannotAssignException {
        if (NoOverlapLessonList.doAnyLessonsOverlap(lessons)) {
            throw new CannotAssignException(OverlappingLessonsException.MESSAGE);
        }
        NoOverlapLessonList newList = NoOverlapLessonList.of(lessons);
        return new Group(name, id, assignedTaskIds, assignedPersonIds, newList);
    }

    /**
     * Returns true if both groups have the same name.
     * This defines a weaker notion of equality between two groups.
     */
    public boolean isSameGroup(Group otherGroup) {
        if (otherGroup == this) {
            return true;
        }

        return otherGroup != null && otherGroup.getName().equals(getName());
    }

    /**
     * Returns true if both groups have the same identity and data fields.
     * This defines a stronger notion of equality between two groups.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Group)) {
            return false;
        }

        Group otherGroup = (Group) other;
        return otherGroup.getId().equals(getId()) && isSameGroup(otherGroup);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return String.valueOf(getName());
    }
}
