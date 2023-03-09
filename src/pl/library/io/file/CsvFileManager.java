package pl.library.io.file;

import pl.library.exception.*;
import pl.library.model.Book;
import pl.library.model.Library;
import pl.library.model.Magazine;
import pl.library.model.Publication;

import java.io.*;
import java.util.Scanner;

public class CsvFileManager implements FileManager{
    private static final String FILE_NAME = "Library.csv";
    @Override
    public Library importData() {
        Library library = new Library();
        try (Scanner fileReader = new Scanner(new File(FILE_NAME)))
        {
            while(fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                Publication publication = createObjectFromString(line);
                library.addPublication(publication);
            }
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku");
        }
        return library;
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
    @Override
    public void exportData(Library library) {
        Publication[] publications = library.getPublications();
        try (FileWriter fw = new FileWriter(FILE_NAME);
             var bufferedWriter = new BufferedWriter(fw);
        ) {
            for (Publication publication : publications) {
                bufferedWriter.write(publication.toCsv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new DataExportException("Błąd zapisu danych do pliku " + FILE_NAME);
        }
    }
}
