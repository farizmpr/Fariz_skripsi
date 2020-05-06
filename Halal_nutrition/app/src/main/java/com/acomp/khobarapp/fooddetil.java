package com.acomp.khobarapp;

public class fooddetil {
    int image;
    String name;
    int size;

    public fooddetil(int image, String name, int size) {
        this.image = image;
        this.name = name;
        this.size = size;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
