package com.mir.gametics;

public class PageLoadNumber {
    private int pageNumber;

    private final static PageLoadNumber INSTANCE = new PageLoadNumber();

    private PageLoadNumber() {}

    public static PageLoadNumber getInstance() {
        return INSTANCE;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
