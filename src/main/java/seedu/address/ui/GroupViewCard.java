package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupWithDetails;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

/**
 * An UI component that displays information of a {@code Group}.
 */
public class GroupViewCard extends UiPart<Region> {

    private static final String FXML = "GroupViewCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    @FXML
    private VBox cardPane;
    @FXML
    private Label name;
    @FXML
    private VBox persons;
    @FXML
    private Label personsLabel;
    @FXML
    private Label lessonsLabel;
    @FXML
    private VBox lessons;
    @FXML
    private Label tasksLabel;
    @FXML
    private VBox tasks;


    /**
     * Creates a {@code Groupcode} with the given {@code Group} and index to display.
     */
    public GroupViewCard(ObservableValue<GroupWithDetails> groupWithDetailsObservable) {
        super(FXML);
        ChangeListener<? super GroupWithDetails> changeListener = new ChangeListener<GroupWithDetails>() {
            @Override
            public void changed(ObservableValue<? extends GroupWithDetails> observable,
                                GroupWithDetails oldValue, GroupWithDetails newValue) {
                updateCard(newValue);
            }
        };
        groupWithDetailsObservable.addListener(changeListener);
        updateCard(groupWithDetailsObservable.getValue());

    }

    private void updateCard(GroupWithDetails groupWithDetails) {
        if (groupWithDetails == null) {
            // null value, this is expected to happen if there is no groups!
            // however, logic handlers should prevent user from viewing an empty view card.
            return;
        }
        updateGroupDetails(groupWithDetails);
    }

    /**
     * Update all group related details
     * @param groupWithDetails to update
     */
    private void updateGroupDetails(GroupWithDetails groupWithDetails) {

        // to list all persons inside group
        Group group = groupWithDetails.getGroup();
        name.setText(group.getName().name);
        Set<Person> personslist = groupWithDetails.getPersons();

        List<Person> personList = new ArrayList<>();
        personList.addAll(personslist);


        // add group persons
        UiUtil.addIndexedLabels(persons, personList.stream().map((persons) ->
                persons.getName().fullName).collect(Collectors.toList()), Optional.of("No students yet!"));

        // add group lessons
        UiUtil.addIndexedLabels(lessons, group.getLessons().stream().map((lessons) ->
                lessons.toString()).collect(Collectors.toList()), Optional.of("No lessons yet!"));


        // add group tasks
        Set<Task> groupTasks = groupWithDetails.getTasks();
        UiUtil.addIndexedLabels(tasks, groupTasks.stream().map(task ->
                task.toString()).collect(Collectors.toList()), Optional.of("No tasks yet!"));

    }

}
