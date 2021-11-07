package seedu.address.testutil;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.task.Task;

/**
 * A utility class containing a list of {@code Task} objects to be used in tests.
 */
public class TypicalTasks {
    public static final Task REPORT_1 = new TaskBuilder().withDescription("Report 1")
            .withUniqueId("f31648db-5619-4bad-99d2-87367a2b5f28").withDeadline("2021-10-10").build();
    public static final Task REPORT_2 = new TaskBuilder().withDescription("Report 2")
            .withUniqueId("84213370-69bf-44e4-9d11-229411f3c1c9").withDeadline("2021-10-20").build();
    public static final Task MATH_HOMEWORK = new TaskBuilder().withDescription("Math Homework")
            .withUniqueId("d3ecfc97-be80-40b3-a798-1674c542ed46").withDeadline("2021-12-21").build();
    public static final Task STATS_ASSIGNMENT = new TaskBuilder().withDescription("Statistics assignment")
            .withUniqueId("c6ada13b-2617-4870-8f57-d9c39c94229b").withDeadline("2021-11-15").build();

    /**
     * Returns an {@code AddressBook} with all the typical tasks.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Task task : getTypicalTasks()) {
            ab.addTask(task);
        }
        return ab;
    }

    public static List<Task> getTypicalTasks() {
        return new ArrayList<>(List.of(REPORT_1, REPORT_2,
                MATH_HOMEWORK, STATS_ASSIGNMENT));
    }
}
