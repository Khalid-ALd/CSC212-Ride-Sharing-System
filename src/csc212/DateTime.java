
package csc212;

import java.time.DateTimeException;
import java.time.LocalDate;

public class DateTime implements IDateTime, Comparable<IDateTime> {

    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;

    public DateTime(int year, int month, int day, int hour, int minute) {
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Invalid time");
        }

        try {
            LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid date");
        }

        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getMonth() {
        return month;
    }

    @Override
    public int getDay() {
        return day;
    }

    @Override
    public int getHour() {
        return hour;
    }

    @Override
    public int getMinute() {
        return minute;
    }

    @Override
    public String format() {
        return String.format(
            "%02d/%02d/%04d %02d:%02d",
            month, day, year, hour, minute
        );
    }

    @Override
    public int compareTo(IDateTime other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }

        int result = Integer.compare(year, other.getYear());
        if (result != 0) return result;

        result = Integer.compare(month, other.getMonth());
        if (result != 0) return result;

        result = Integer.compare(day, other.getDay());
        if (result != 0) return result;

        result = Integer.compare(hour, other.getHour());
        if (result != 0) return result;

        return Integer.compare(minute, other.getMinute());
    }

    @Override
    public String toString() {
        return format();
    }
}
