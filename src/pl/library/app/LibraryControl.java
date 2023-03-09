package pl.library.app;

import pl.library.exception.DataExportException;
import pl.library.exception.DataImportException;
import pl.library.exception.InvalidDataException;
import pl.library.exception.NoSuchOptionException;
import pl.library.io.ConsolePrinter;
import pl.library.io.DataReader;
import pl.library.io.file.FileManager;
import pl.library.io.file.FileManagerBuilder;
import pl.library.model.Book;
import pl.library.model.Library;
import pl.library.model.Magazine;

import java.util.InputMismatchException;

class LibraryControl {
    private ConsolePrinter consolePrinter = new ConsolePrinter();
    private DataReader dataReader = new DataReader(consolePrinter);
    private FileManager fileManager;

    private Library library;

    public LibraryControl() {
        fileManager = new FileManagerBuilder(consolePrinter, dataReader).build();
        try{
            library = fileManager.importData();
            consolePrinter.printLine("Zaimportowano bazę.");
        } catch (DataImportException | InvalidDataException e) {
            System.out.println(e.getMessage());;
            consolePrinter.printLine("Zainicjowano nową bazę.");
            library = new Library();
        }
    }

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
                case REMOVE_BOOK:
                    removeBook();
                    break;
                case REMOVE_MAGAZINE:
                    removeMagazine();
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
            library.addPublication(book);
        } catch (InputMismatchException e) {
            consolePrinter.printLine("Nie udało się utworzyć książki, podana wartość została nieprawidłowa");
        } catch (ArrayIndexOutOfBoundsException e) {
            consolePrinter.printLine("Osiągnięto limit biblioteki");
        }
    }
    private void removeBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            if (library.removePublication(book)) {
                consolePrinter.printLine("Usunięto książkę");
            } else
            consolePrinter.printLine("Nie udało się usunąć książki");
        } catch (InputMismatchException e) {
            consolePrinter.printLine("Nie udało się usunąć książki, podana wartość została nieprawidłowa");
        }
    }

    private void printBooks() {
        consolePrinter.printBooks(library.getPublications());
    }

    private void addMagazine() {
        try{
        Magazine magazine = dataReader.readAndCreateMagazine();
        library.addPublication(magazine);
    } catch (InputMismatchException e) {
        consolePrinter.printLine("Nie udało się utworzyć magazynu, podana wartość została nieprawidłowa");
    } catch (ArrayIndexOutOfBoundsException e) {
        consolePrinter.printLine("Osiągnięto limit biblioteki");
    }
    }
    private void removeMagazine() {
        try{
            Magazine magazine = dataReader.readAndCreateMagazine();
            if (library.removePublication(magazine)){
                consolePrinter.printLine("Usunięto magazyn");
            }
            consolePrinter.printLine("Nie udało się usunąć magazynu");
        } catch (InputMismatchException e) {
            consolePrinter.printLine("Nie udało się usunąć magazynu, podana wartość została nieprawidłowa");
        }
    }

    private void printMagazines() {
        consolePrinter.printMagazines(library.getPublications());
    }

    private void exit() {
        try {
            fileManager.exportData(library);
            consolePrinter.printLine("Eksport danych do pliku zakończony powodzeniem");
        } catch (DataExportException e) {
            consolePrinter.printLine(e.getMessage());
        }
        consolePrinter.printLine("Koniec programu, papa!");
        // zamykamy strumień wejścia
        dataReader.close();
    }
    private enum Option {
        EXIT(0, "Wyjście z programu"),
        ADD_BOOK(1, "Dodanie książki"),
        ADD_MAGAZINE(2,"Dodanie magazynu/gazety"),
        PRINT_BOOKS(3, "Wyświetlenie dostępnych książek"),
        PRINT_MAGAZINES(4, "Wyświetlenie dostępnych magazynów/gazet"),
        REMOVE_BOOK(5, "Usuń książkę"),
        REMOVE_MAGAZINE(6, "Usuń magazyn");

        private int value;
        private String description;

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        Option(int value, String desc) {
            this.value = value;
            this.description = desc;
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException {
            try {
                return Option.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("Brak opcji o id " + option);
            }
        }
    }
}