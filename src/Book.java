// Book.java
/**
 * Book class on the server side represents a book in the library system
 * It contains the ID, title, publisher, ISBN, author and the ID of an owner
 */
public class Book {
    private int id;
    private String title;
    private String publisher;
    private String isbn;
    private String author;
    private int ownerID;

    //Constructor
    public Book(String title, String publisher, String isbn, String author, int ownerID) {
        this.title = title;
        this.publisher = publisher;
        this.isbn = isbn;
        this.author = author;
        this.ownerID = ownerID;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    public int getOwnerID() {
        return ownerID;
    }

    //Setters
    public void setID(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
}
