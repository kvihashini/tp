package seedu.address.model.task;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.id.HasUniqueId;
import seedu.address.model.id.UniqueId;

/**
 * Represents a Task in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Task implements HasUniqueId {
    // The task description
    private final Description description;

    // The deadline of the task
    private final Deadline deadline;

    // The id of the task
    private final UniqueId id;

    /**
     * Every field must be present and not null.
     */
    public Task(Description description, Deadline deadline) {
        this.id = UniqueId.generateId(this);
        requireAllNonNull(description, deadline, id);
        this.description = description;
        this.deadline = deadline;
    }

    /**
     * Every field must be present and not null.
     */
    public Task(Description description, Deadline deadline, UniqueId id) {
        requireAllNonNull(description, deadline, id);
        this.description = description;
        this.deadline = deadline;
        this.id = id;
        id.setOwner(this);
    }

    public Description getDescription() {
        return description;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public UniqueId getId() {
        return id;
    }

    /**
     * Returns true if both tasks have the same description and deadline.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameTask(Task otherTask) {
        if (otherTask == this) {
            return true;
        }

        return otherTask != null
                && otherTask.getDescription().equals(getDescription())
                && otherTask.getDeadline().equals(getDeadline());
    }

    /**
     * Returns true if both tasks have the same id, description and deadline.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return otherTask.getId().equals(getId()) && isSameTask(otherTask);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(description, deadline, id);
    }

    @Override
    public String toString() {
        return getDescription()
                + "; Deadline: "
                + getDeadline();
    }

    /**
     * Returns the task string with given completion status for use in PersonViewCard
     */
    public String toCompletionString(Boolean isDone) {
        return (isDone ? "[X] " : "[  ] ") + toString();
    }
}
