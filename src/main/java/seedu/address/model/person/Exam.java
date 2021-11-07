package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import seedu.address.model.lesson.Subject;

/**
 * Represents an exam that the Person has. Has a subject and a datetime
 * Compares exam based on exam dates
 */
public class Exam implements Comparable<Exam> {

    private static final DateTimeFormatter examDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Subject subject;
    private final LocalDateTime dateTime;

    /**
     * Constructs an exam with a subject and a date time
     * @param subject or topic of the exam
     * @param dateTime when the exam will occur
     */
    public Exam(Subject subject, LocalDateTime dateTime) {
        requireAllNonNull(subject, dateTime);
        this.subject = subject;
        this.dateTime = dateTime;
    }

    public Subject getSubject() {
        return subject;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", dateTime.format(examDateTimeFormat), subject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Exam)) {
            return false;
        }
        Exam exam = (Exam) o;
        return Objects.equals(subject, exam.subject) && Objects.equals(dateTime, exam.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, dateTime);
    }

    @Override
    public int compareTo(Exam o) {
        return dateTime.compareTo(o.dateTime);
    }
}
