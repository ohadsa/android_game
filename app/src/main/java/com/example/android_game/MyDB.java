package com.example.android_game;

import java.util.ArrayList;

public class MyDB {

    private ArrayList<Player> records = new ArrayList<>();

    public MyDB() { }

    public ArrayList<Player> getRecords() {
        return records;
    }

    public MyDB setRecords(ArrayList<Player> records) {
        this.records = records;
        return this;
    }
}
