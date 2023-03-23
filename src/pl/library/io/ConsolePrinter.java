package pl.library.io;

import pl.library.model.*;

import java.util.Collection;
import java.util.Collections;

public class ConsolePrinter {

    public void printBooks(Collection<Publication> publications) {
        //int counter = 0;
/*        for (Publication publication : publications) {
            if(publication instanceof Book) {
                printLine(publication.toString());
                counter++;
            }
            }*/
        long count = publications.stream()
                .filter(p -> p instanceof Book)
                .map(Publication::toString)
                .peek(this::printLine)
                .count();
        if (count == 0) {
            printLine("Brak książek w bibliotece");
        }
    }

    public void printMagazines(Collection<Publication> publications) {
        long count = publications.stream()
                .filter(p -> p instanceof Magazine)
                .map(Publication::toString)
                .peek(this::printLine)
                .count();
        if (count == 0) {
            printLine("Brak magazynów w bibliotece");
        }
    }
    public void printUsers(Collection<LibraryUser> users){
/*        for (LibraryUser user : users) {
            printLine(user.toString());
        }*/
        users.stream()
                .map(User::toString)
                .forEach(this::printLine);
    }

    public void printLine(String text) {
        System.out.println(text.toUpperCase());
    }
}