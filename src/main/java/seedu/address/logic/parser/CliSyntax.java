package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Umbrella command definitions */
    public static final String PERSON_COMMAND = "student";
    public static final String GROUP_COMMAND = "group";

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_START_TIME = new Prefix("st/");
    public static final Prefix PREFIX_END_TIME = new Prefix("et/");
    public static final Prefix PREFIX_DAY = new Prefix("d/");
    public static final Prefix PREFIX_SUBJECT = new Prefix("s/");

    /* Prefixes for tasks */
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("n/");
    public static final Prefix PREFIX_DEADLINE = new Prefix("d/");

    public static final String MONDAY = "mon";
    public static final String TUESDAY = "tue";
    public static final String WEDNESDAY = "wed";
    public static final String THURSDAY = "thu";
    public static final String FRIDAY = "fri";
    public static final String SATURDAY = "sat";
    public static final String SUNDAY = "sun";
}
