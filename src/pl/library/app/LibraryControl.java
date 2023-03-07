package pl.library.app;

import pl.library.exception.NoSuchOptionException;
import pl.library.io.ConsolePrinter;
import pl.library.io.DataReader;
import pl.library.model.Book;
import pl.library.model.Library;
import pl.library.model.Magazine;

import java.util.InputMismatchException;

class LibraryControl {
    private ConsolePrinter consolePrinter = new ConsolePrinter();
    private DataReader dataReader = new DataReader(consolePrinter);

    private Library library = new Library();

    void controlLoop() {
        Option option;

        do {
            printOptions();
            option = getOption();
            switch (option) {
                case ADD_BOOK:
                    addBook();
                    break;
                case ADD_MAGAZINE:
                    addMagazine();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                    break;
                case PRINT_MAGAZINES:
                    printMagazines();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    consolePrinter.printLine("Nie ma takiej opcji, wprowadź ponownie: ");
            }
        } while (option != Option.EXIT);
    }

    private Option getOption() {
        boolean optionOk = false;
        Option option = null;
        while (!optionOk){
            try
            {
                option = Option.createFromInt(dataReader.getInt());
                optionOk = true;
            } catch (NoSuchOptionException e) {
                consolePrinter.printLine(e.getMessage());
            } catch (InputMismatchException e) {
                consolePrinter.printLine("Wprowadzona wartość nie jest liczbą, podaj ponownie");
            }
        }
        return option;
    }

    private void printOptions() {
        consolePrinter.printLine("Wybierz opcję: ");
        for (Option option : Option.values()) {
            consolePrinter.printLine(option.toString());
        }
    }

    private void addBook() {
        try{
            Book book = dataReader.readAndCreateBook();
            library.addBook(book);
        } catch (InputMismatchException e) {
            consolePrinter.printLine("Nie udało się utworzyć książki, podana wartość została nieprawidłowa");
        } catch (ArrayIndexOutOfBoundsException e) {
            consolePrinter.printLine("Osiągnięto limit biblioteki");
        }

    }

    private void printBooks() {
        consolePrinter.printBooks(library.getPublications());
    }

    private void addMagazine() {
        try{
        Magazine magazine = dataReader.readAndCreateMagazine();
        library.addMagazine(magazine);
    } catch (InputMismatchException e) {
        consolePrinter.printLine("Nie udało się utworzyć magazynu, podana wartość została nieprawidłowa");
    } catch (ArrayIndexOutOfBoundsException e) {
        consolePrinter.printLine("Osiągnięto limit biblioteki");
    }
    }

    private void printMagazines() {
        consolePrinter.printMagazines(library.getPublications());
    }

    private void exit() {
        consolePrinter.printLine("Koniec programu, papa!");
        // zamykamy strumień wejścia
        dataReader.close();
    }
}