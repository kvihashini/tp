package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupWithDetails;
import seedu.address.model.group.UniqueGroupList;
import seedu.address.model.id.UniqueId;
import seedu.address.model.id.UniqueIdMapper;
import seedu.address.model.lesson.Attendee;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.LessonWithAttendees;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonWithDetails;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson/.isSameTask/.isSameGroup comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;

    private final UniqueTaskList tasks;

    private final UniqueGroupList groups;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        tasks = new UniqueTaskList();
        groups = new UniqueGroupList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the task list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks.setTasks(tasks);
    }

    /**
     * Replaces the contents of the group list with {@code groups}.
     * {@code groups} must not contain duplicate groups.
     */
    public void setGroups(List<Group> groups) {
        this.groups.setGroups(groups);
    }


    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setTasks(newData.getTaskList());
        setGroups(newData.getGroupList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * Cleans up all references to this person.
     *
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        UniqueId personId = key.getId();
        persons.remove(key);
        assert !persons.contains(key); // assert removal first, before cleaning up.
        groups.cleanUpPersonId(personId);
    }

    //// task-level operations

    /**
     * Returns true if a task with the same identity as {@code task} exists in the address book.
     */
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return tasks.contains(task);
    }

    /**
     * Adds a task to the address book.
     * The task must not already exist in the address book.
     */
    public void addTask(Task t) {
        tasks.add(t);
    }

    /**
     * Replaces the given task {@code target} in the list with {@code editedTask}.
     * {@code target} must exist in the address book.
     * The task identity of {@code editedTask} must not be the same as another existing task in the address book.
     */
    public void setTask(Task target, Task editedTask) {
        requireNonNull(editedTask);

        tasks.setTask(target, editedTask);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeTask(Task key) {
        tasks.remove(key);
    }

    //// group-level operations

    /**
     * Returns true if a group with the same identity as {@code group} exists in the address book.
     */
    public boolean hasGroup(Group group) {
        requireNonNull(group);
        return groups.contains(group);
    }

    /**
     * Adds a group to the address book.
     * The group must not already exist in the address book.
     */
    public void addGroup(Group g) {
        groups.add(g);
    }

    /**
     * Replaces the given group {@code target} in the list with {@code editedGroup}.
     * {@code target} must exist in the address book.
     * The group identity of {@code editedGroup} must not be the same as another existing group in the address book.
     */
    public void setGroup(Group target, Group editedGroup) {
        requireNonNull(editedGroup);

        groups.setGroup(target, editedGroup);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeGroup(Group key) {
        UniqueId groupId = key.getId();
        groups.remove(key);
        assert !groups.contains(key); // assert removal first, before cleaning up.
        persons.cleanUpGroupId(groupId);
    }

    public UniqueIdMapper<Person> getPersonMapper() {
        return persons;
    }

    public UniqueIdMapper<Group> getGroupMapper() {
        return groups;
    }

    public PersonWithDetails getPersonWithDetails(Person person) {
        Set<GroupWithDetails> groupsPersonIsIn = groups.getFromUniqueIds(person.getAssignedGroupIds()).stream()
                .map(this::getGroupWithDetails)
                .collect(Collectors.toSet());
        Set<Task> tasksPersonHas = tasks.getFromUniqueIds(person.getAssignedTaskIds());
        Map<Task, Boolean> tasksCompletion = new HashMap<>();
        Map<UniqueId, Boolean> tasksCompletionId = person.getTasksCompletion();
        tasksPersonHas.forEach(task -> {
            UniqueId taskId = task.getId();
            Boolean isDone = tasksCompletionId.get(taskId);
            assert !isDone.equals(null);
            tasksCompletion.put(task, isDone);
        });
        return new PersonWithDetails(person, groupsPersonIsIn, tasksPersonHas, tasksCompletion);
    }

    public GroupWithDetails getGroupWithDetails(Group group) {
        Set<Person> studentsInGroup = persons.getFromUniqueIds(group.getAssignedPersonIds());
        Set<Task> tasksInGroup = tasks.getFromUniqueIds(group.getAssignedTaskIds());
        return new GroupWithDetails(group, studentsInGroup, tasksInGroup);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons"
                + tasks.asUnmodifiableObservableList().size() + " tasks"
                + groups.asUnmodifiableObservableList().size() + " groups";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Group> getGroupList() {
        return groups.asUnmodifiableObservableList();
    }

    @Override
    public List<LessonWithAttendees> getSortedLessonsWithAttendees() {
        List<LessonWithAttendees> lessons = new ArrayList<>();
        for (Person person : persons) {
            List<Attendee> newList = new ArrayList<>();
            newList.add(person);
            for (Lesson lesson : person.getLessons()) {
                lessons.add(new LessonWithAttendees(lesson, newList));
            }
        }
        for (Group group : groups) {
            List<Attendee> newList = new ArrayList<>(persons.getFromUniqueIds(group.getAssignedPersonIds()));
            for (Lesson lesson : group.getLessons()) {
                lessons.add(new LessonWithAttendees(lesson, newList));
            }
        }
        Collections.sort(lessons, new LessonWithAttendees.SortByLesson());
        return lessons;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && persons.equals(((AddressBook) other).persons)
                && tasks.equals(((AddressBook) other).tasks)
                && groups.equals(((AddressBook) other).groups));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
