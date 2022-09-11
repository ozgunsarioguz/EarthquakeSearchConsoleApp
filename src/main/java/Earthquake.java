package main.java;

public class Earthquake {

    private String country;
    private String place;
    private String magnitude;
    private String date;
    private String time;

        public Earthquake() {
    }

    public Earthquake(String country, String place, String magnitude, String date, String time) {
        this.country = country;
        this.place = place;
        this.magnitude = magnitude;
        this.date = date;
        this.time = time;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Earthquake in " + country.toUpperCase() + " on " + date + ", with a magnitude of " + magnitude + ", at " + place
                + " with local time " + time;
    }
    
    
}
