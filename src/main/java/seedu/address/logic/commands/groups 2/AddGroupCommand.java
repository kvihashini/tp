package seedu.address.logic.commands.groups;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ViewingType;
import seedu.address.model.group.Group;
import seedu.address.model.id.UniqueId;
import seedu.address.model.person.Person;

/**
 * Adds a group to the model with the associated person ids.
 */
public class AddGroupCommand extends Command {

    public static final String MESSAGE_SUCCESS = "New group created: %1$s";
    public static final String MESSAGE_DUPLICATE_GROUP = "This group already exists in the address book";

    private final Group toAdd;
    private final List<Index> personsIndex;

    /**
     * Constructs a {@code AddGroupCommand} with the given parameters
     *
     * @param toAdd        Group to add
     * @param personsIndex List of indexes of persons to add
     */
    public AddGroupCommand(Group toAdd, List<Index> personsIndex) {
        this.toAdd = toAdd;
        this.personsIndex = new ArrayList<>(personsIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (model.hasGroup(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_GROUP);
        }

        List<Person> lastShownList = model.getFilteredPersonList();
        Set<UniqueId> personsId = new HashSet<>();
        ArrayList<Person> personsToReplace = new ArrayList<>();
        ArrayList<Person> personsWithGroupId = new ArrayList<>();
        for (Index index : personsIndex) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            Person person = lastShownList.get(index.getZeroBased());
            assert !person.hasGroupId(toAdd.getId()); // its a new group, there should not be an exisitng id!
            personsId.add(person.getId());
            personsToReplace.add(person);
            personsWithGroupId.add(person.addGroupId(toAdd.getId()));
        }

        assert personsId.size() == personsIndex.size(); // all indexes are added with distinct ids.
        // update persons
        for (int i = 0; i < personsToReplace.size(); i++) {
            model.setPerson(personsToReplace.get(i), personsWithGroupId.get(i));
        }
        // add group!
        Group withIds = toAdd.updateAssignedPersonIds(personsId);
        model.addGroup(withIds);

        model.setGroupToView(withIds);
        model.setViewingType(ViewingType.GROUP);

        return new CommandResult(String.format(MESSAGE_SUCCESS, withIds));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof AddGroupCommand)) {
            return false;
        }
        AddGroupCommand that = (AddGroupCommand) o;
        return toAdd.isSameGroup(that.toAdd) && Objects.equals(personsIndex, that.personsIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toAdd, personsIndex);
    }

}
