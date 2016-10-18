package com.termproject.familyprotector;

/**
 * Created by Mehul on 10/15/2016.
 */
public class BookMark {
    String bookmarkTitle;
    String bookmarkUrl;
    String bookmarkDate;
    String bookmarkTime;
    String[] bookmarkCategories;

    public BookMark(String bookmarkTitle, String bookmarkUrl, String bookmarkDate, String bookmarkTime, String[] bookmarkCategories){

        this.bookmarkTitle = bookmarkTitle;
        this.bookmarkUrl = bookmarkUrl;
        this.bookmarkDate = bookmarkDate;
        this.bookmarkTime = bookmarkTime;
        this.bookmarkCategories = bookmarkCategories;

    }

}
