package com.example.manit;

class pdf {
    String name;
    String url;
    String year;

    public pdf( ) {
       //
    }

    public pdf(String name, String url, String year) {
        this.name = name;
        this.url = url;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getYear() {
        return year;
    }
}
