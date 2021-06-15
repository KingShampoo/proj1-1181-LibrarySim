package com.company;

import java.io.Serializable;

public class textBook implements Serializable {//uses sirializable so its easier to write to a binary file with objects instead of having to write the fields to the file
    private int isbn;                          // remake all the objects using the fields (slow)
    private String title;
    private String author;
    private double price;


    public textBook(int isbn, String title, String author, double price) {//constructor for new books
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public textBook(textBook tb) {//copy constructor, dont need it but it is nice to have if i wanted to add functionality
        this.isbn = tb.getIsbn();
        this.title = tb.getTitle();
        this.author = tb.getAuthor();
        this.price = tb.getPrice();
    }

    //getters and setters
    public int getIsbn() {
        return isbn;
    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {//my to string for each book
        return "['"+title + "', written by :"+author+", is isbn #"+isbn+", and is selling for $"+price+"]";
    }

}
