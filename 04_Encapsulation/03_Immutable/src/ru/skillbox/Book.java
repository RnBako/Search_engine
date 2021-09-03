package ru.skillbox;

public class Book {
    private final String name;
    private final String author;
    private final int pagesNumber;
    private final int ISBN;

    public Book(String name, String author, int pagesNumber, int ISBN) {
        this.name = name;
        this.author = author;
        this.pagesNumber = pagesNumber;
        this.ISBN = ISBN;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public int getISBN() {
        return ISBN;
    }
}
