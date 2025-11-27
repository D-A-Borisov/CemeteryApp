package com.example.CemeteryApplication.models;

public class Cemetery {
    private String name;
    private String address;

    public Cemetery(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return name;
    }
}