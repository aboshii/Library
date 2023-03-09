package pl.library.model;

import java.io.Serializable;
import java.util.Arrays;

public class Library implements Serializable {

    private static final int INITIAL_CAPACITY = 1;
    private int publicationsNumber;
    private Publication[] publications = new Publication[INITIAL_CAPACITY];

    public Publication[] getPublications() {
        Publication[] result = new Publication[publicationsNumber];
        for (int i = 0; i < result.length; i++) {
            result[i] = publications[i];
        }
        return result;
    }

    public void addPublication(Publication publication){
        if (publicationsNumber == INITIAL_CAPACITY) {
            publications = Arrays.copyOf(publications, INITIAL_CAPACITY * 2);
        }
            publications[publicationsNumber] = publication;
            publicationsNumber++;
    }
    public boolean removePublication(Publication publication){
        final int notFound = -1;
        int foundIndex = notFound;
        int i = 0;
        while (i < publicationsNumber && foundIndex == notFound) {
            if (publication.equals(publications[i])){
                foundIndex = i;
            } else {
                i++;
            }
        }
        if (foundIndex != notFound) {
            System.arraycopy(publications, foundIndex + 1, publications, foundIndex, publications.length - foundIndex - 1);
            publicationsNumber--;
            publications[publicationsNumber] = null;
        }
        return foundIndex != notFound;
    }
}