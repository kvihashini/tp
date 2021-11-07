package seedu.address.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.group.GroupWithDetails;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonWithDetails;
import seedu.address.model.task.Task;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonViewCard extends UiPart<Region> {

    private static final String FXML = "PersonViewCard.fxml";

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
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private VBox lessons;
    @FXML
    private Label groupLessonsLabel;
    @FXML
    private VBox groupLessons;
    @FXML
    private VBox tasks;
    @FXML
    private Label groupTasksLabel;
    @FXML
    private VBox groupTasks;
    @FXML
    private VBox exams;
    @FXML
    private VBox groups;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonViewCard(ObservableValue<PersonWithDetails> personWithDetailsObservable) {
        super(FXML);
        ChangeListener<? super PersonWithDetails> changeListener = new ChangeListener<PersonWithDetails>() {
            @Override
            public void changed(ObservableValue<? extends PersonWithDetails> observable,
                                PersonWithDetails oldValue, PersonWithDetails newValue) {
                updateCard(newValue);
            }
        };
        personWithDetailsObservable.addListener(changeListener);
        updateCard(personWithDetailsObservable.getValue());

    }

    private void updateCard(PersonWithDetails personWithDetails) {
        if (personWithDetails == null) {
            // null value, this is expected to happen if there is no persons!
            // however, logic handlers should prevent user from viewing an empty view card.
            return;
        }

        updatePersonDetails(personWithDetails);
        updateGroupRelatedItems(personWithDetails.getGroups());
    }

    /**
     * Update all person related details
     * @param personWithDetails to update
     */
    private void updatePersonDetails(PersonWithDetails personWithDetails) {
        Person person = personWithDetails.getPerson();
        Set<Task> personTasks = personWithDetails.getTasks();
        Map<Task, Boolean> personTasksCompletion = personWithDetails.getTasksCompletion();

        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        tags.getChildren().clear();
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        // add person lessons
        UiUtil.addIndexedLabels(lessons, person.getLessons().stream().map((lessons) ->
                lessons.toString()).collect(Collectors.toList()), Optional.of("No lessons yet!"));

        // add person tasks
        UiUtil.addIndexedLabels(tasks, personTasks.stream().map(task -> {
            Boolean isDone = personTasksCompletion.get(task);
            assert !isDone.equals(null);
            return task.toCompletionString(isDone);
        }).collect(Collectors.toList()), Optional.of("No tasks yet!"));

        // add person exams
        UiUtil.addIndexedLabels(exams, person.getExams().stream().map(exam ->
                exam.toString()).collect(Collectors.toList()), Optional.of("No exams yet!"));
    }

    /**
     * Update all group related details
     * @param personGroups to update
     */
    private void updateGroupRelatedItems(Set<GroupWithDetails> personGroups) {
        // create a list to hold the lesson details in groups that person belongs to
        List<String> groupLessonsList = getDetailsWithGroupName(personGroups, groupWithDetails ->
                groupWithDetails.getGroup().getLessons());
        // create a list to hold the task details in groups that person belongs to
        List<String> groupTasksList = getDetailsWithGroupName(personGroups, groupWithDetails ->
                groupWithDetails.getTasks());

        // remove the group lessons and tasks labels if is empty
        groupLessonsLabel.setVisible(!groupLessonsList.isEmpty());
        groupLessonsLabel.setManaged(!groupLessonsList.isEmpty());
        groupTasksLabel.setVisible(!groupTasksList.isEmpty());
        groupTasksLabel.setManaged(!groupTasksList.isEmpty());

        // add group lessons, no empty message
        UiUtil.addIndexedLabels(groupLessons, groupLessonsList, Optional.empty());

        // add group tasks, no empty message
        UiUtil.addIndexedLabels(groupTasks, groupTasksList, Optional.empty());

        // add person group names
        UiUtil.addIndexedLabels(groups, personGroups.stream().map(group ->
                group.getGroup().getName().toString()).collect(Collectors.toList()), Optional.of("No groups yet!"));
    }

    private List<String> getDetailsWithGroupName(Set<GroupWithDetails> groups,
            Function<GroupWithDetails, Collection<? extends Object>> function) {
        List<String> list = new ArrayList<>();
        for (GroupWithDetails groupWithDetails : groups) {
            String groupName = groupWithDetails.getGroup().getNameInString();
            for (Object obj : function.apply(groupWithDetails)) {
                list.add(String.format("%s with group %s", obj, groupName));
            }
        }
        return list;
    }
}
