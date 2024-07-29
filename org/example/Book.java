package org.example;

public class Book {
    private String bookId;
    private String bookName;
    private String author;
    private String genre;
    private String isbn;
    private int booksCount;
        public Book(){}
        public Book(String bookName, String author, String genre, int booksCount) {
            this.bookName = bookName;
            this.author = author;
            this.genre = genre;
            this.booksCount = booksCount;
        }

        public String getBookName() {
            return bookName;
        }
        public void setBookName(String bookName) {
            this.bookName = bookName;
        }
        public String getAuthor() {
            return author;
        }
        public void setAuthor(String author) {
            this.author = author;
        }
        public String getGenre() {

            return genre;
        }
        public void setGenre(String genre) {

            this.genre = genre;
        }
        public int getBooksCount() {
            return booksCount;
        }
        public void setBooksCount(int booksCount) {
            this.booksCount = booksCount;
        }
        public void setBookId(String bookId) {
            this.bookId=bookId;
        }
        public void setIsbn(String isbn){
            this.isbn=isbn;
        }
}
