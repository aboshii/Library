package pl.library.model;

import pl.library.exception.PublicationAlreadyExistException;
import pl.library.exception.UserAlreadyExistException;

import java.io.Serializable;
import java.util.*;

public class Library implements Serializable {

    private Map<String, Publication> publications = new HashMap<>();
    private Map<String, LibraryUser> users = new HashMap<>();

    public Map<String, Publication> getPublications() {
        return publications;
    }

    public Collection<Publication> getSortedPublications(Comparator<Publication> comparator){
        ArrayList<Publication> list = new ArrayList<>(publications.values());
        list.sort(comparator);
        return list;
    }

    public Optional<Publication> findPublicationByTitle(String title){
        return Optional.ofNullable(publications.get(title));
    }

    public Collection<LibraryUser> getSortedUsers(Comparator<LibraryUser> comparator){
        ArrayList<LibraryUser> list = new ArrayList<>(users.values());
        list.sort(comparator);
        return list;
    }

    public Map<String, LibraryUser> getUsers() {
        return users;
    }

    public void addPublication(Publication publication){
            if (publications.containsKey(publication.getTitle())){
                addBookCopy(publication);
                throw new PublicationAlreadyExistException("Publikacja o takim tytule już istnieje, dodałem kolejny egzemplarz");
            }
            publications.put(publication.getTitle(), publication);
    }

    public void addUser(LibraryUser user){
        if (users.containsKey(user.getPesel())){
            throw new UserAlreadyExistException("Użytkownik o takim peselu już istnieje");
        }
        users.put(user.getPesel(), user);
    }

    private void addBookCopy(Publication publication) {
        int actualCount = publications.get(publication.getTitle()).getCount();
        publications.get(publication.getTitle()).setCount(actualCount + 1);
    }

    public boolean removePublication(Publication publication){
        if (publications.containsValue(publication)) {
            publications.remove(publication.getTitle());
            return true;
        } else {
            return false;
        }
    }
}