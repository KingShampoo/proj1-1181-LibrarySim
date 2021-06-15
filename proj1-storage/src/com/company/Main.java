package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<textBook> library = new ArrayList<>();
        boolean done = false;

        try {
            FileInputStream fileIn = new FileInputStream("LibraryBin.bin");
            ObjectInputStream bookIn = new ObjectInputStream(fileIn);

            int size = bookIn.readInt();
            if (size == 0){
                System.out.println("Library is currently empty!");
            } else {
                for (int i = 0; i < size; i++) {
                    textBook tbToken = (textBook) bookIn.readObject();//the way i save my file is i write an integer saying how big my library array is , then have my objects
                    library.add(tbToken);//so i can read in exact # of objects i have and not read in garbage data
                }
            }
            bookIn.close();//close all my readers
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("Running with no previous library");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        while (!done) {
            int selection = -1;
            menuLoop();
            while (selection == -1) {//i use -1 as a method fail number so i can keep looping my try blocks all throughout my code
                try {
                    selection = Integer.parseInt(sc.next());
                    if (selection == 1 || selection == 2 || selection == 3 || selection == 4 || selection == 5|| selection == 6 || selection == 7 || selection == 0 ) {

                    } else {//if selection is one of these continue, if not restart loop and print below statement. this is to get rid of wrong integer inputs
                        selection = -1;
                        System.out.println("Invalid selection");
                    }
                } catch (NumberFormatException ignore) {
                    System.out.println("Please enter a whole number from the list provided in the " +// if this error is read either a wrong format number is entered or a string
                            "form of '1' or '2' with no symbols or punctuation");
                }
            }
            if      (selection == 1)
                library.add(makeBook());
            else if (selection == 2) {
                changePrice(library);
            }
            else if (selection == 3)
                listLibrary(library);
            else if (selection == 4)
                bookToString(library);
            else if (selection == 5)
                removeBook(library);
            else if (selection == 6)
                libraryStats(library);
            else if (selection == 7)
                isbnSearch(library);
            else if (selection == 0) {
                done = true;
                saveFile(library);// when exiting call save library method
            }
        }
    }

    public static void menuLoop() {//first menu is just a print function
        System.out.println(
                        "Hello would you like to :\n" +
                        "1) Add a book.\n" +
                        "2) Change the price of a listed book.\n" +
                        "3) List the books currently in stock.\n" +
                        "4) View a books details.\n" +
                        "5) Remove a book from the inventory.\n" +
                        "6) Library Stats\n" +
                        "7) Search by isbn\n" +
                        "...\n" +
                        "0) Exit and save.\n" +
                        "(please enter the number for the selection you want)\n");
    }

    public static textBook makeBook() {//selection 1
        Scanner sc = new Scanner(System.in);
        int isbn = 0;
        String title = null;//init variables that will be book fields
        String author = null;
        double price = 0;
        boolean done = false;//use this to loop till i get the right data type

        while (!done) {//i can check the double and int but not the strings as some books have numbers in titles
            try {
                System.out.println("What is the isbn without dashes or spaces?");
                isbn = Integer.parseInt(sc.next());
                done = true;

            } catch (NumberFormatException ignore) {
                System.out.println("Please enter a an isbn in integer form");
            }
        }

        done = false;//reset done to save memory
        System.out.println("What is the title?");
        while (!done) {
            sc.nextLine();// this gobbles the end line char, without this it fills the .nextLine with whitespace basically
            title = sc.nextLine().trim();
            if (title.equals("") || title == null) {// only errors i can check for without checking a database of correct titles is empty entries
                done = false;
                System.out.println("Please enter the title");

            } else {
                done = true;
            }
        }

        done = false;//reset done to save memory
        System.out.println("What is the Authors name?");
        while (!done) {
            author = sc.nextLine().trim();
            done = true;
            if (author == "") {//same as above empty string check
                System.out.println("Please enter the authors name");
                done = false;
            }
        }

        done = false;//reset done to save memory
        while (!done) {
            try {
                System.out.print("\nWhat is the price? \n$");
                price = Double.parseDouble(sc.next());
                done = true;//if it has a number format ex this wont get read and it will loop again

            } catch (NumberFormatException ignore) {
                System.out.println("Please enter a a price in $xx.xx form or $xx");
            }
        }
        textBook returnBook = new textBook(isbn, title, author, price);
        return returnBook;
    }//end selection 1

    public static void changePrice(ArrayList<textBook> library) {//selection 2
        Scanner sc = new Scanner(System.in);
        if (library.size()==0){
            System.out.println("Your library is empty! ");
        } else {
            System.out.println("What is the index of the book you are looking for?");
            int input = 0;
            int index = -1;
            while (index == -1) {
                try {
                    input = Integer.parseInt(sc.next());
                    if (input < 1 || input > library.size())
                        throw new NumberFormatException();//show blanket error message
                    index = input;
                } catch (NumberFormatException ignore) {
                    System.out.println("Please enter a correct index, here is your library, index value is on the left");
                    listLibrary(library);
                }
            }

            double price = 0;
            boolean done = false;
            while (!done) {
                try {
                    System.out.print("\nWhat is the new price? \n$");
                    price = Double.parseDouble(sc.next());
                    if (price<0) {
                        System.out.println("Please enter a positive price");
                        throw new NumberFormatException();
                    }
                    done = true;//if it has a number format exc this wont get read and it will loop again

                } catch (NumberFormatException ignore) {
                    System.out.println("Please enter a a price in $xx.xx form or $xx");
                }
                library.get(index-1).setPrice(price);
            }
        }
    }//end selection 2

    public static void listLibrary(ArrayList<textBook> library) {
        int counter = 1;
        for (textBook tb : library) {
            System.out.println(counter + ") " + tb.toString() + "\n");
            counter++;
        }
        if (library.size()==0){
            System.out.println("Library is currently empty!\n\n");
        }
    }

    public static void bookToString(ArrayList<textBook> library) {
        Scanner sc = new Scanner(System.in);
        System.out.println("What is the index of the book you are looking for?");
        int input = 0;
        int index = -1;

        while (index==-1) {
            try {
                input = Integer.parseInt(sc.next());
                if (input < 1 || input > library.size())
                    throw new NumberFormatException();//show blanket error message
                index = input;
            } catch (NumberFormatException ignore) {
                System.out.println("Please enter an integer index");
                listLibrary(library);
            }
        }
        System.out.println(library.get(index-1).toString() + "\n");
    }

    public static void removeBook(ArrayList<textBook> library) {
        Scanner sc = new Scanner(System.in);
        System.out.println("What is the isbn of the book you are looking to remove?");
        int input = -1;
        int index = -1;
        try {
            input = Integer.parseInt(sc.next());
            for (int i = 0; i < library.size(); i++) {
                if (library.get(i).getIsbn() == input)
                    index = i;
            }
        } catch (NumberFormatException ignore) {
            System.out.println("Please enter an integer isbn ");
        }
        if (index == -1)
            removeBook(library);//recursively calls this function if index does not get assigned a value, just wanted to replace a while block
        else
            library.remove(index);
    }

    public static void deleteFile(){
        File myFile = new File("LibraryBin.bin");
        if (myFile.delete()){
            System.out.println("success");//if it can delete it, it will, else it prints not deleted and something bad happened
        } else {
            System.out.println("not deleted");
        }
    }

    public static void saveFile(ArrayList<textBook> library) {
        try {
            deleteFile();//clear old data file
            // create a new file with an ObjectOutputStream
            FileOutputStream out = new FileOutputStream("LibraryBin.bin");
            ObjectOutputStream oout = new ObjectOutputStream(out);

            oout.writeInt(library.size());//write how many books we have in the library first so we can read our file

            for (textBook tb: library) {
                oout.writeObject(tb);
            }
            oout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void libraryStats(ArrayList<textBook> library){
        System.out.println("Size of library ="+library.size());
        double libraryValue = 0;
        for (textBook tb: library){
            libraryValue=libraryValue+tb.getPrice();
        }
        System.out.println(" Total value of library ="+libraryValue);
        System.out.println();
    }

    public static void isbnSearch(ArrayList<textBook> library){
        Scanner sc = new Scanner(System.in);
        int index = -1;

        while (index==-1){
            try {
                System.out.println("What is the isbn?");
                int searchISBN = Integer.parseInt(sc.next());
                for (int i = 0; i < library.size(); i++) {
                    if (library.get(i).getIsbn()==searchISBN)
                        index = i;
                }
                if (index == -1)
                    System.out.println("isbn not found");
            } catch (NumberFormatException ignore){
                System.out.println("Please enter a valid integer isbn");
                listLibrary(library);//show library to help user find book
            }
        }
        System.out.println(library.get(index).toString());
        System.out.println();//formatting
    }
}
