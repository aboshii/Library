package pl.library.io.file;

import pl.library.exception.*;
import pl.library.model.*;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

public class CsvFileManager implements FileManager{
    private static final String FILE_NAME = "Library.csv";
    private static final String USERS_FILE_NAME = "Library_users.csv";
    @Override
    public Library importData() {
        Library library = new Library();
        importPublications(library);
        importUsers(library);
        return library;
    }

    private void importUsers(Library library) {
        try (
                //Scanner fileReader = new Scanner(new File(USERS_FILE_NAME))
                var bufferedReader = new BufferedReader(new FileReader(USERS_FILE_NAME));
        )
        {
/*            while(fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                LibraryUser user = createUserFromString(line);
                library.addUser(user);
            }*/
            bufferedReader.lines()
                    .map(this::createUserFromString)
                    .forEach(library::addUser);
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku");
        } catch (IOException e) {
            throw new DataImportException("Błąd odczytu pliku");
        }
    }

    private void importPublications(Library library) {
        try (
                //Scanner fileReader = new Scanner(new File(FILE_NAME))
            var bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
        )
        {
/*            while(fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                Publication publication = createObjectFromString(line);
                library.addPublication(publication);
            }*/
            bufferedReader.lines()
                    .map(this::createObjectFromString)
                    .forEach(library::addPublication);
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku");
        } catch (IOException e) {
            throw new DataImportException("Błąd odczytu pliku");
        }
    }

    @Override
    public void exportData(Library library) {
        exportPublications(library);
        exportUsers(library);
    }

    private void exportUsers(Library library) {
        Collection<LibraryUser> users = library.getUsers().values();
        exportToCsv(users, USERS_FILE_NAME);

    }
    private void exportPublications(Library library) {
        Collection<Publication> publications = library.getPublications().values();
        exportToCsv(publications, FILE_NAME);

    }

    private <T extends CsvConvertible> void exportToCsv(Collection<T> collection, String fileName) {
        try (var fileWriter = new FileWriter(USERS_FILE_NAME);
             var bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            for (T value : collection) {
                bufferedWriter.write(value.toCsv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new DataExportException("Błąd zapisu danych do pliku " + fileName);
        }
    }

    private Publication createObjectFromString(String line) {
        String[] splitedLine = line.split(";");
        String publicationType = splitedLine[0];
        if (Book.TYPE.equals(publicationType)){
            return createBook(splitedLine);
        } else if (Magazine.TYPE.equals(publicationType)) {
            return createMagazine(splitedLine);
        } else throw new InvalidDataException("Nieznany typ publikacji: " + splitedLine[0]);
    }
    private LibraryUser createUserFromString(String csvLine) {
        String[] splitedLine = csvLine.split(";");
        String firstName = splitedLine[0];
        String lastName = splitedLine[1];
        String pesel = splitedLine[2];
        return new LibraryUser(firstName, lastName, pesel);
    }
    private Magazine createMagazine(String[] splitedLine) {
        String title = splitedLine[1];
        String author = splitedLine[2];
        int year = Integer.valueOf(splitedLine[3]);
        int month = Integer.valueOf(splitedLine[4]);
        int day = Integer.valueOf(splitedLine[5]);
        String language = splitedLine[6];
        return new Magazine(title, author, language, year, month, day);
    }
    private Book createBook(String[] splitedLine) {
        String title = splitedLine[1];
        String author = splitedLine[4];
        int year = Integer.valueOf(splitedLine[3]);
        int pages = Integer.valueOf(splitedLine[5]);
        String publisher = splitedLine[2];
        String isbn = splitedLine[6];
        return new Book(title, author, year, pages, publisher, isbn);
    }

}
