package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.id.UniqueId;
import seedu.address.model.lesson.NoOverlapLessonList;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Group objects.
 */
public class GroupBuilder {

    public static final String DEFAULT_NAME = "Stats tutorial";

    private GroupName name;
    private UniqueId uniqueId;
    private Set<UniqueId> assignedTaskIds;
    private Set<UniqueId> assignedPersonIds;
    private NoOverlapLessonList lessonsList;

    /**
     * Creates a {@code GroupBuilder} with the default details.
     */
    public GroupBuilder() {
        this.name = new GroupName(DEFAULT_NAME);
        this.uniqueId = UniqueId.generateId(UUID.randomUUID().toString());
        this.assignedTaskIds = new HashSet<>();
        this.assignedPersonIds = new HashSet<>();
        this.lessonsList = new NoOverlapLessonList();
    }

    /**
     * Initializes the GroupBuilder with the data of {@code groupToCopy}.
     */
    public GroupBuilder(Group groupToCopy) {
        name = groupToCopy.getName();
        uniqueId = groupToCopy.getId();
        this.assignedTaskIds = new HashSet<>(groupToCopy.getAssignedTaskIds());
        this.assignedPersonIds = new HashSet<>(groupToCopy.getAssignedPersonIds());
        this.lessonsList = NoOverlapLessonList.of(groupToCopy.getLessons());
    }

    /**
     * Sets the {@code Description} of the {@code Group} that we are building.
     */
    public GroupBuilder withName(String name) {
        this.name = new GroupName(name);
        return this;
    }

    /**
     * Sets the {@code UniqueId} of the {@code Group} that we are building.
     */
    public GroupBuilder withUniqueId(String uniqueId) {
        this.uniqueId = UniqueId.generateId(uniqueId);
        return this;
    }

    /**
     * Parses the {@code ids} into a {@code Set<UniqueId>} and set it to the {@code Group} that we are building.
     */
    public GroupBuilder withAssignedTaskIds(String ... assignedTaskIds) {
        this.assignedTaskIds = SampleDataUtil.getUniqueIdSet(assignedTaskIds);
        return this;
    }

    /**
     * Parses the {@code ids} into a {@code Set<UniqueId>} and set it to the {@code Group} that we are building.
     */
    public GroupBuilder withAssignedPersonIds(String ... assignedPersonIds) {
        this.assignedPersonIds = SampleDataUtil.getUniqueIdSet(assignedPersonIds);
        return this;
    }

    /**
     * Sets the {@code Lessons List} of the {@code Group} that we are building.
     */
    public GroupBuilder withLessonsList(NoOverlapLessonList lessonsList) {
        this.lessonsList = lessonsList;
        return this;
    }

    /**
     * Builds a {@code Group} object from the {@code GroupBuilder}.
     *
     * @return A {@code Group} object.
     */
    public Group build() {
        Group group = new Group(name);
        uniqueId.setOwner(group);
        return group;
    }

    /**
     * Builds a {@code Group} object with ID from the {@code GroupBuilder}.
     *
     * @return A {@code Group} object.
     */
    public Group buildWithID() {
        Group anotherGroup = new Group(name, uniqueId);
        uniqueId.setOwner(anotherGroup);
        return anotherGroup;
    }

    /**
     * Builds a {@code Group} object to copy from the {@code GroupBuilder}.
     *
     * @return A {@code Group} object.
     */
    public Group buildToCopy() {
        Group groupToCopy = new Group(name, uniqueId);
        Group originalGroup = new Group(groupToCopy);
        return originalGroup;
    }

}
