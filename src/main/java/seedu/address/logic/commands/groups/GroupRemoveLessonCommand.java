package seedu.address.logic.commands.groups;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ViewingType;
import seedu.address.model.group.Group;

public class GroupRemoveLessonCommand extends Command {

    public static final String MESSAGE_SUCCESS = "Lesson deleted";

    private Index groupIndex;
    private Index lessonIndex;

    /**
     * Constructs a GroupRemoveLessonCommand object with the given group index and lesson index.
     * @param groupIndex of group to remove lesson from.
     * @param lessonIndex of lesson in group to remove.
     */
    public GroupRemoveLessonCommand(Index groupIndex, Index lessonIndex) {
        this.groupIndex = groupIndex;
        this.lessonIndex = lessonIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Group> groups = model.getFilteredGroupList();

        if (groupIndex.getZeroBased() >= groups.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_GROUP_DISPLAYED_INDEX);
        }

        Group toRemoveFrom = groups.get(groupIndex.getZeroBased());

        if (!toRemoveFrom.isValidLessonIndex(lessonIndex.getZeroBased())) {
            throw new CommandException(Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
        }

        Group removedLesson = toRemoveFrom.unassignLesson(lessonIndex.getZeroBased());

        model.setGroup(toRemoveFrom, removedLesson);
        model.updateFilteredGroupList(Model.PREDICATE_SHOW_ALL_GROUPS);
        model.setGroupToView(removedLesson);
        model.setViewingType(ViewingType.GROUP);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
