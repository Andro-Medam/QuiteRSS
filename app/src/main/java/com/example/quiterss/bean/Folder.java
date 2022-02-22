package com.example.quiterss.bean;

public class Folder {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Channel{" +
                ", name='" + name + '\'' +
                '}';
    }
}
