package pl.library.model;

import java.io.Serializable;
import java.time.Year;
import java.util.Objects;

abstract public class Publication implements Serializable, Comparable<Publication>, CsvConvertible {
    private String title;
    private String publisher;
    private Year year;
    private int count = 0;

    Publication(String title, String publisher, int year) {
        this.title = title;
        this.publisher = publisher;
        this.year = Year.of(year);
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getPublisher() {
        return publisher;
    }

    void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public abstract String toCsv();

    @Override
    public String toString() {
        return title + ", " + publisher + ", " + year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return count == that.count && Objects.equals(title, that.title) && Objects.equals(publisher, that.publisher) && Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, publisher, year, count);
    }

    @Override
    public int compareTo(Publication p) {
        return title.compareToIgnoreCase(p.title);
    }
}