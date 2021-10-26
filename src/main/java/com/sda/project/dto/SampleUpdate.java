package com.sda.project.dto;

public class SampleUpdate {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SampleUpdate{" +
                "name='" + name + '\'' +
                '}';
    }
}
