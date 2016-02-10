package com.example.jordi.myapplication;

public class User {
    private String icon;
    private String name;
    private String rank;

    User(String icon, String name, String rank){
        this.icon = icon;
        this.name = name;
        this.rank = rank;

    }
    User(){

    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getRank() { return rank; }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
