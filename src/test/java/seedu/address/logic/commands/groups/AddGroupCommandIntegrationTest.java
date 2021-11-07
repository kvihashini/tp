package seedu.address.logic.commands.groups;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ViewingType;
import seedu.address.model.group.Group;
import seedu.address.model.id.UniqueId;
import seedu.address.model.person.Person;
import seedu.address.testutil.GroupBuilder;
import seedu.address.testutil.ModelStub;

public class AddGroupCommandIntegrationTest {

    private Group toAdd = new GroupBuilder().withName("HURRAY").build();
    private List<Index> indexes = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
    private AddGroupCommand command = new AddGroupCommand(toAdd, indexes);

    @Test
    public void execute_validGroupAndPersons_groupAddedSuccessfully() throws CommandException {
        List<Person> twoPersonsList = new ArrayList<>(Arrays.asList(ALICE, BENSON));
        ModelStubAddGroupSuccess modelStub = new ModelStubAddGroupSuccess(new ArrayList<>(), null);
        modelStub.personList = twoPersonsList;

        Set<UniqueId> uniqueIds = Set.of(ALICE.getId(), BENSON.getId());
        Group expectedGroup = toAdd.updateAssignedPersonIds(uniqueIds);
        List<Person> expectedPersons = new ArrayList<>(Arrays.asList(ALICE.addGroupId(toAdd.getId()),
                BENSON.addGroupId(toAdd.getId())));
        ModelStubAddGroupSuccess expectedModel = new ModelStubAddGroupSuccess(expectedPersons, expectedGroup);

        assertCommandSuccess(command, modelStub, String.format(AddGroupCommand.MESSAGE_SUCCESS, expectedGroup),
                expectedModel);
    }

    @Test
    public void execute_invalidPersonsIndex_failure() {
        ModelStubAddGroupSuccess modelStub = new ModelStubAddGroupSuccess(new ArrayList<>(), null);
        // fail when index is invalid
        assertCommandFailure(command, modelStub, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateGroup_failure() {
        ModelStubHasGroup modelStub = new ModelStubHasGroup();
        assertCommandFailure(command, modelStub, AddGroupCommand.MESSAGE_DUPLICATE_GROUP);
    }

    private class ModelStubHasGroup extends ModelStub {
        @Override
        public boolean hasGroup(Group group) {
            return true;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList();
        }
    }


    private class ModelStubAddGroupSuccess extends ModelStub {
        private List<Person> personList = new ArrayList<>();
        private Group added = null;
        private ViewingType type = null;
        private Group toView = null;

        public ModelStubAddGroupSuccess(List<Person> personList, Group added) {
            this.personList = personList;
            this.added = added;
        }

        @Override
        public boolean hasGroup(Group group) {
            return false;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableList(personList);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            personList.set(personList.indexOf(target), editedPerson);
        }

        @Override
        public void addGroup(Group group) {
            this.added = group;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public void setGroupToView(Group group) {
            toView = group;
        }

        @Override
        public void setViewingType(ViewingType type) {
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || (!(o instanceof ModelStubAddGroupSuccess))) {
                return false;
            }

            ModelStubAddGroupSuccess other = (ModelStubAddGroupSuccess) o;
            return added.equals(other.added) && personList.equals(other.personList);
        }
    }
}
