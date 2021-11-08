package seedu.address.logic.commands.groups;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.GROUP_COMMAND;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ViewingType;
import seedu.address.model.group.Group;


/**
 * Deletes a group identified using it's displayed index from the address book.
 */
public class ViewGroupCommand extends Command {

    public static final String COMMAND_WORD = "-v";

    public static final String MESSAGE_USAGE = GROUP_COMMAND + " " + COMMAND_WORD
            + ": Views a group in the groups list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + "group " + COMMAND_WORD + " 1";

    public static final String MESSAGE_VIEW_GROUP_SUCCESS = "View Group: %1$s";

    private final Index targetIndex;

    public ViewGroupCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Group> lastShownList = model.getFilteredGroupList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_GROUP_DISPLAYED_INDEX);
        }

        Group groupToView = lastShownList.get(targetIndex.getZeroBased());
        model.setGroupToView(groupToView);
        model.setViewingType(ViewingType.GROUP);
        return new CommandResult(String.format(MESSAGE_VIEW_GROUP_SUCCESS, groupToView));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewGroupCommand // instanceof handles nulls
                && targetIndex.equals(((ViewGroupCommand) other).targetIndex)); // state check
    }
}
