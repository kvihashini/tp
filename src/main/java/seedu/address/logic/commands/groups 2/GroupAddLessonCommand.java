package seedu.address.logic.commands.groups;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ViewingType;
import seedu.address.model.group.Group;
import seedu.address.model.id.UniqueIdMapper;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.exceptions.CannotAssignException;
import seedu.address.model.person.Person;

public class GroupAddLessonCommand extends Command {

    public static final String CANNOT_ASSIGN_MESSAGE = " in the group cannot be assigned this lesson";
    public static final String ADD_LESSON_SUCCESS = "Lesson added";

    private final Index groupIndex;
    private final Lesson lessonToAdd;

    /**
     * Constructs a {@code GroupAddLessonCommand} with the given index and lesson
     * @param index of group to add lesson to
     * @param lesson to add
     */
    public GroupAddLessonCommand(Index index, Lesson lesson) {
        groupIndex = index;
        lessonToAdd = lesson;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Group> groupList = model.getFilteredGroupList();
        if (groupIndex.getZeroBased() >= groupList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_GROUP_DISPLAYED_INDEX);
        }
        Group group = groupList.get(groupIndex.getZeroBased());
        Group groupWithLesson;
        try {
            groupWithLesson = group.assignLesson(lessonToAdd);
        } catch (CannotAssignException e) {
            throw new CommandException(e.getMessage());
        }
        assert group.isSameGroup(groupWithLesson); // assigning lesson should not change details

        UniqueIdMapper<Person> personMapper = model.getPersonMapper();
        Set<Person> personsInGroup = personMapper.getFromUniqueIds(group.getAssignedPersonIds());
        for (Person person : personsInGroup) {
            if (!person.canAssignLesson(lessonToAdd)) {
                throw new CommandException(person.getAttendeeDetails() + CANNOT_ASSIGN_MESSAGE);
            }
        }
        model.setGroup(group, groupWithLesson);
        model.updateLessonWithAttendeesList();
        model.updateFilteredGroupList(Model.PREDICATE_SHOW_ALL_GROUPS);
        model.setGroupToView(groupWithLesson);
        model.setViewingType(ViewingType.GROUP);
        return new CommandResult(ADD_LESSON_SUCCESS);
    }
}
