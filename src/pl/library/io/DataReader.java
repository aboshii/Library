package pl.library.io;

import pl.library.model.Book;
import pl.library.model.Magazine;

import java.util.Scanner;

public class DataReader {
    private Scanner sc = new Scanner(System.in);
    private ConsolePrinter consolePrinter;

    public DataReader(ConsolePrinter consolePrinter) {
        this.consolePrinter = consolePrinter;
    } //wstrzykiwanie zależności - DataReader zależy od ConsolePrinter

    public void close() {
        sc.close();
    }

    public int getInt() {
        try{
            return sc.nextInt();
        }
        finally {
            sc.nextLine();
        }
    }

    public Book readAndCreateBook() {
        consolePrinter.printLine("Tytuł: ");
        String title = sc.nextLine();
        consolePrinter.printLine("Autor: ");
        String author = sc.nextLine();
        consolePrinter.printLine("Wydawnictwo: ");
        String publisher = sc.nextLine();
        consolePrinter.printLine("ISBN: ");
        String isbn = sc.nextLine();
        consolePrinter.printLine("Rok wydania: ");
        int releaseDate = getInt();
        consolePrinter.printLine("Ilość stron: ");
        int pages = getInt();
        return new Book(title, author, releaseDate, pages, publisher, isbn);
    }

    public Magazine readAndCreateMagazine() {
        consolePrinter.printLine("Tytuł: ");
        String title = sc.nextLine();
        consolePrinter.printLine("Wydawnictwo: ");
        String publisher = sc.nextLine();
        consolePrinter.printLine("Język: ");
        String language = sc.nextLine();
        consolePrinter.printLine("Rok wydania: ");
        int year = getInt();
        consolePrinter.printLine("Miesiąc: ");
        int month = getInt();
        consolePrinter.printLine("Dzień: ");
        int day = getInt();

        return new Magazine(title, publisher, language, year, month, day);
    }
}