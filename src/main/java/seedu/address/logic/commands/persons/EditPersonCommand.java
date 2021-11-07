package seedu.address.logic.commands.persons;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PERSON_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ViewingType;
import seedu.address.model.group.Group;
import seedu.address.model.id.UniqueId;
import seedu.address.model.id.UniqueIdMapper;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.NoOverlapLessonList;
import seedu.address.model.lesson.exceptions.CannotAssignException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Exam;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditPersonCommand extends Command {

    public static final String COMMAND_WORD = "-e";

    public static final String MESSAGE_USAGE = PERSON_COMMAND + " " + COMMAND_WORD
            + ": Edits the details of the student identified "
            + "by the index number used in the displayed student list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + PERSON_COMMAND + " " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This student already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;
    private final String successMsg;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditPersonCommand(Index index, EditPersonDescriptor editPersonDescriptor, String successMsg) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);
        requireNonNull(successMsg);
        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
        this.successMsg = successMsg;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor, model);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateLessonWithAttendeesList();
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.setPersonToView(editedPerson);
        model.setViewingType(ViewingType.PERSON);
        return new CommandResult(String.format(successMsg, editedPerson));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor,
            Model model)
            throws CommandException {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Set<UniqueId> updatedAssignedTaskIds = personToEdit.getAssignedTaskIds(); // not allowed to be edited here
        Map<UniqueId, Boolean> updatedTasksCompletion = personToEdit.getTasksCompletion(); // not editable here
        NoOverlapLessonList lessonList = personToEdit.getLessonsList();
        List<Exam> exams = personToEdit.getExams();
        Set<UniqueId> assignedGroupIds = personToEdit.getAssignedGroupIds();

        Person newPerson = new Person(personToEdit.getId(), updatedName, updatedPhone, updatedEmail,
                updatedAddress, updatedTags, updatedAssignedTaskIds,
                updatedTasksCompletion, lessonList, exams, assignedGroupIds);
        newPerson = editPersonDescriptor.updateLessons(newPerson, model.getGroupMapper());
        newPerson = editPersonDescriptor.updateExams(newPerson);
        return newPerson;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditPersonCommand)) {
            return false;
        }

        // state check
        EditPersonCommand e = (EditPersonCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && successMsg.equals(successMsg);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {

        public static final String CANNOT_ASSIGN_LESSON_GROUP = "lesson conflicts with lessons in group: ";

        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;
        private List<Index> lessonsToRemove = new ArrayList<>();
        private List<Lesson> lessonsToAdd = new ArrayList<>();
        private List<Index> examsToRemove = new ArrayList<>();
        private List<Exam> examsToAdd = new ArrayList<>();

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
            lessonsToRemove.addAll(toCopy.lessonsToRemove);
            lessonsToAdd.addAll(toCopy.lessonsToAdd);
            examsToRemove.addAll(toCopy.examsToRemove);
            examsToAdd.addAll(toCopy.examsToAdd);
        }


        /**
         * Util method to remove lesson from a person
         * @param personToEdit person to remove lesson from
         * @param lessonsToRemove indexes to remove
         * @return Person with lessons removed
         * @throws IndexOutOfBoundsException if any index is out of bounds
         */
        private static Person removeLessons(Person personToEdit,
                                            List<Index> lessonsToRemove) throws IndexOutOfBoundsException {
            // sort because removing from the back will not hurt the earlier indexes!
            lessonsToRemove.sort(new Index.SortDescending());
            for (Index i : lessonsToRemove) {
                personToEdit = personToEdit.unassignLesson(i.getZeroBased());
            }
            return personToEdit;
        }

        /**
         * Util method to add all lessons to a person
         * @param personToEdit to add lessons to
         * @param lessonsToAdd to the person
         * @return Person with lessons added
         * @throws CannotAssignException
         */
        private static Person addLessons(Person personToEdit, List<Lesson> lessonsToAdd)
                throws CannotAssignException {
            for (Lesson l : lessonsToAdd) {
                personToEdit = personToEdit.assignLesson(l);
            }
            return personToEdit;
        }

        /**
         * Util method to find a group that cannot be assigned the lesson
         * @param groups to check
         * @param lessons to check
         * @return Optional group that is conflicting with lessons
         */
        private static Optional<Group> findConflictingLessonGroup(Set<Group> groups, List<Lesson> lessons) {
            for (Lesson lesson : lessons) {
                for (Group group : groups) {
                    if (!group.canAssignLesson(lesson)) {
                        return Optional.of(group);
                    }
                }
            }
            return Optional.empty();
        }

        /**
         * Util method to remove exams from a person
         * @param personToEdit person to remove exams from
         * @param examsToRemove indexes to remove
         * @return Person with exams removed
         * @throws IndexOutOfBoundsException if any index is out of bounds
         */
        private static Person removeExams(Person personToEdit,
                List<Index> examsToRemove) throws IndexOutOfBoundsException {
            // sort because removing from the back will not hurt the earlier indexes!
            examsToRemove.sort(new Index.SortDescending());
            for (Index i : examsToRemove) {
                personToEdit = personToEdit.removeExam(i.getZeroBased());
            }
            return personToEdit;
        }


        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        /**
         * Adds a lesson to be added to the person later
         */
        public void addLesson(Lesson l) {
            requireNonNull(l);
            lessonsToAdd.add(l);
        }

        /**
         * Adds an index of a lesson to be removed from the person later
         */
        public void removeLesson(Index index) {
            requireNonNull(index);
            lessonsToRemove.add(index);
        }

        /**
         * Adds an exam to be added to the person later
         */
        public void addExam(Exam e) {
            requireNonNull(e);
            examsToAdd.add(e);
        }

        /**
         * Adds an index of an exam to be removed from the person later
         */
        public void removeExam(Index index) {
            requireNonNull(index);
            examsToRemove.add(index);
        }

        /**
         * Updates the lesson of a person according to the specified order previously.
         * @param personToEdit person to update lessons list.
         * @return Person with updated lessons list, removal is done before adding.
         * @throws CommandException if any specified index or lesson is invalid
         */
        public Person updateLessons(Person personToEdit, UniqueIdMapper<Group> mapper) throws CommandException {
            // removes lesson first before adding lessons
            try {
                personToEdit = removeLessons(personToEdit, lessonsToRemove);
            } catch (IndexOutOfBoundsException index) {
                throw new CommandException(Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
            }
            // adding lessons
            try {
                // first cross-check with all groups;
                Set<Group> groupsPersonIsIn = mapper.getFromUniqueIds(personToEdit.getAssignedGroupIds());
                Optional<Group> conflictingGroup = findConflictingLessonGroup(groupsPersonIsIn, lessonsToAdd);
                conflictingGroup.ifPresent(group -> {
                    throw new CannotAssignException(CANNOT_ASSIGN_LESSON_GROUP + group.getName());
                });
                // now try add lesson to person
                personToEdit = addLessons(personToEdit, lessonsToAdd);
            } catch (CannotAssignException e) {
                throw new CommandException(e.getMessage());
            }
            return personToEdit;
        }

        /**
         * Updates the exams of a person according to the specified order previously.
         * @param personToEdit person to update exams list.
         * @return Person with updated exams list, removal is done before adding.
         * @throws CommandException if any specified index is invalid
         */
        public Person updateExams(Person personToEdit) throws CommandException {
            // removes lesson first before adding lessons
            try {
                // sort because removing from the back will not hurt the earlier indexes!
                personToEdit = removeExams(personToEdit, examsToRemove);
            } catch (IndexOutOfBoundsException index) {
                throw new CommandException(Messages.MESSAGE_INVALID_EXAM_INDEX);
            }
            for (Exam e : examsToAdd) {
                personToEdit = personToEdit.addExam(e);
            }
            return personToEdit;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags())
                    && lessonsToAdd.equals(e.lessonsToAdd)
                    && lessonsToRemove.equals(lessonsToRemove)
                    && examsToAdd.equals(e.examsToAdd)
                    && examsToRemove.equals(e.examsToRemove);
        }
    }
}
