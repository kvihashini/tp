package seedu.address.model.util;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.id.UniqueId;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.NoOverlapLessonList;
import seedu.address.model.lesson.Subject;
import seedu.address.model.lesson.Timeslot;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Exam;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Description;
import seedu.address.model.task.Task;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    private static final Logger logger = LogsCenter.getLogger(SampleDataUtil.class);

    public static Person[] getSamplePersons() throws ParseException {
        Person[] persons;
        try {
            persons = new Person[] {
                    new Person(UniqueId.generateId("8df2379c-d093-4ab5-9f82-4f05d5f70c23"), new Name("Alex Yeoh"),
                            new Phone("87438807"), new Email("alexyeoh@example.com"),
                            new Address("Blk 30 Geylang Street 29, #06-40"), getTagSet("friends"),
                            getUniqueIdSet("20453a92-15bb-4871-b810-139826f024a0"), getFirstTaskCompletion(),
                            new NoOverlapLessonList().addLesson(new Lesson(new Timeslot("18:00", "20:00"),
                                    new Subject("Maths"), ParserUtil.parseDayOfWeek("Mon"))),
                            new ArrayList<Exam>(), getUniqueIdSet("f01ff39c-e483-4191-b923-fcdbc1c64341"))
                            .addExam(new Exam(new Subject("Maths"),
                            ParserUtil.parseLocalDateTime("2021-12-20 14:00"))),
                    new Person(UniqueId.generateId("e1f36b8b-3bd5-40f6-971e-eb4083398f27"), new Name("Bernice Yu"),
                            new Phone("99272758"), new Email("berniceyu@example.com"),
                            new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                            getTagSet("colleagues", "friends"),
                            getUniqueIdSet("3328c8ab-ca7e-495c-ab3a-9e0e8e78138b"), getSecondTaskCompletion(),
                            new NoOverlapLessonList().addLesson(new Lesson(new Timeslot("18:00", "20:00"),
                                    new Subject("Econs"), ParserUtil.parseDayOfWeek("Wed"))),
                            new ArrayList<Exam>(), getUniqueIdSet("c4172df3-5f8e-478b-aa26-7f4fadb83329"))
                            .addExam(new Exam(new Subject("Econs"),
                            ParserUtil.parseLocalDateTime("2021-12-20 18:00"))),
                    new Person(UniqueId.generateId("824a4d52-5995-4e20-b3ea-fc4c354f2eb1"),
                            new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                            new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), getTagSet("neighbours"),
                            getUniqueIdSet("3328c8ab-ca7e-495c-ab3a-9e0e8e78138b"), getSecondTaskCompletion(),
                            new NoOverlapLessonList().addLesson(new Lesson(new Timeslot("18:00", "20:00"),
                                    new Subject("Econs"), ParserUtil.parseDayOfWeek("Wed"))),
                            new ArrayList<Exam>(), getUniqueIdSet("c4172df3-5f8e-478b-aa26-7f4fadb83329"))
                            .addExam(new Exam(new Subject("Econs"),
                            ParserUtil.parseLocalDateTime("2021-12-20 18:00")))
            };
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT), pe);
        }
        return persons;
    }

    public static Task[] getSampleTasks() {
        Task[] tasks = new Task[] {
            new Task(new Description("Complete math assignment"), new Deadline("2021-10-10"),
                UniqueId.generateId("20453a92-15bb-4871-b810-139826f024a0")),
            new Task(new Description("Complete english assignment"), new Deadline("2021-12-12"),
                UniqueId.generateId("3328c8ab-ca7e-495c-ab3a-9e0e8e78138b"))
        };
        return tasks;
    }

    public static Map<UniqueId, Boolean> getFirstTaskCompletion() {
        Map<UniqueId, Boolean> taskCompletion = new HashMap<>();
        UniqueId id = UniqueId.generateId("20453a92-15bb-4871-b810-139826f024a0");
        taskCompletion.put(id, false);
        return taskCompletion;
    }

    public static Map<UniqueId, Boolean> getSecondTaskCompletion() {
        Map<UniqueId, Boolean> taskCompletion = new HashMap<>();
        UniqueId id = UniqueId.generateId("3328c8ab-ca7e-495c-ab3a-9e0e8e78138b");
        taskCompletion.put(id, false);
        return taskCompletion;
    }

    public static Group[] getSampleGroups() {
        Group[] groups = new Group[] {
            new Group(new GroupName("Maths tutorial"), UniqueId.generateId("f01ff39c-e483-4191-b923-fcdbc1c64341"),
                getUniqueIdSet("20453a92-15bb-4871-b810-139826f024a0"),
                getUniqueIdSet("8df2379c-d093-4ab5-9f82-4f05d5f70c23"),
                new NoOverlapLessonList()),
            new Group(new GroupName("Econs tutorial"), UniqueId.generateId("c4172df3-5f8e-478b-aa26-7f4fadb83329"),
                getUniqueIdSet("3328c8ab-ca7e-495c-ab3a-9e0e8e78138b"),
                getUniqueIdSet("e1f36b8b-3bd5-40f6-971e-eb4083398f27",
                    "824a4d52-5995-4e20-b3ea-fc4c354f2eb1"), new NoOverlapLessonList())
        };
        return groups;
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        try {
            for (Person samplePerson : getSamplePersons()) {
                sampleAb.addPerson(samplePerson);
            }
        } catch (ParseException pe) {
            logger.info("Invalid command format");
        }
        for (Task sampleTask : getSampleTasks()) {
            sampleAb.addTask(sampleTask);
        }
        for (Group sampleGroup : getSampleGroups()) {
            sampleAb.addGroup(sampleGroup);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a unique id set containing the list of strings given.
     */
    public static Set<UniqueId> getUniqueIdSet(String... strings) {
        return Arrays.stream(strings)
                .map(UniqueId::generateId)
                .collect(Collectors.toSet());
    }

}
