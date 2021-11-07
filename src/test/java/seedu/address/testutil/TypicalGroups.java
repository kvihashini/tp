package seedu.address.testutil;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.group.Group;

/**
 * A utility class containing a list of {@code Group} objects to be used in tests.
 */
public class TypicalGroups {
    public static final Group GROUP_1 = new GroupBuilder().withName("CS2103T")
            .withUniqueId("c42f4eea-2cb4-443b-bd0d-fdccd98641df").build();
    public static final Group GROUP_2 = new GroupBuilder().withName("CS2100")
            .withUniqueId("ee2226bd-b34f-4fd0-accd-05068e314146").build();

    /**
     * Returns an {@code AddressBook} with all the typical groups.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Group group : getTypicalGroups()) {
            ab.addGroup(group);
        }
        return ab;
    }

    public static List<Group> getTypicalGroups() {
        return new ArrayList<>(List.of(GROUP_1, GROUP_2));
    }
}
