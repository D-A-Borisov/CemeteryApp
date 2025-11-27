package com.example.CemeteryApplication.models;

public class Grave {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String birthDate;
    private String deathDate;
    private String cemeteryName;
    private String section;
    private String row;
    private String graveNumber;
    private double latitude;
    private double longitude;

    public Grave(int id, String firstName, String lastName, String middleName,
                 String birthDate, String deathDate, String cemeteryName,
                 String section, String row, String graveNumber,
                 double latitude, double longitude) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.cemeteryName = cemeteryName;
        this.section = section;
        this.row = row;
        this.graveNumber = graveNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getMiddleName() { return middleName; }
    public String getBirthDate() { return birthDate; }
    public String getDeathDate() { return deathDate; }
    public String getCemeteryName() { return cemeteryName; }
    public String getSection() { return section; }
    public String getRow() { return row; }
    public String getGraveNumber() { return graveNumber; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public String getFullName() {
        return lastName + " " + firstName + " " + (middleName != null ? middleName : "");
    }

    public String getLocationInfo() {
        return "Участок: " + section + ", Ряд: " + row + ", Могила: " + graveNumber;
    }

    public String getYearsOfLife() {
        return birthDate + " - " + deathDate;
    }
}