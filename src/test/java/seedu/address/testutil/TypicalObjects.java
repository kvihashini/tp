package seedu.address.testutil;

import static seedu.address.testutil.TypicalPersons.getTypicalPersonsWithLesson;
import static seedu.address.testutil.TypicalTasks.getTypicalTasks;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

/**
 * A utility class containing a list of {@code Person} and {@code Task} objects to be used in tests.
 */
public class TypicalObjects {

    private TypicalObjects() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersonsWithLesson()) {
            ab.addPerson(person);
        }
        for (Task task : getTypicalTasks()) {
            ab.addTask(task);
        }
        return ab;
    }
}
