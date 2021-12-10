package com.example.android_game;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;

public class Player {
    private String name ;
    private int gameType ;
    private int speed ;
    private int score = 0 ;
    private double lat = 0.0;
    private double lon = 0.0;

    public double getLat() {
        return lat;
    }

    public Player setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Player setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Player setScore(int score) {
        this.score = score;
        return this;
    }

    public int getGameType() {
        return gameType;

    }

    public Player setGameType(int gameType) {
        this.gameType = gameType;
        return this;
    }

    public int getSpeed() {
        return speed;
    }

    public Player setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    @Override
    public String toString() {

        int n = name.length() + (""+score).length() ;
        String str = " -" ;
        for(int i = 0 ; i < 17 - n ; i++ ){
            str = str + " -";
        }
        str = (name  + str + " "+ score);

        return  str;
    }

    public Boolean saveRecordToFile(){

        MSPV3 instance = MSPV3.getMe();
        int numOfRecords = instance.getInt("NUMBER_OF_RECORDS", 0 );
        Gson g = new Gson();
        ArrayList<Player> players = new ArrayList<>();
        Boolean result = true ;

        for(int i = 0 ; i < numOfRecords ; i++ ) {
            String str = instance.getString("player" + i, "");
            Player p;
            p = g.fromJson(str, Player.class);
            players.add(p);
        }

        Comparator comparator = new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.setScore() - o1.setScore() ;
            }
        };

        if(numOfRecords > 0 ) {
            players.sort(comparator);
            if (numOfRecords < 10) {
                players.add(this);
                players.sort(comparator);
                numOfRecords++;

            } else {
                    if(players.get(9).score >  this.score   )
                        result = false ;
                    players.add(this);
                    players.sort(comparator);
                    players.remove(10);
                }

            for (int i = 0; i < numOfRecords  ; i++) {
                instance.putString("player" + i, g.toJson(players.get(i)));
            }
        }

        else {
            players.add(this);
            instance.putString("player" + 0, g.toJson(players.get(0)));
            numOfRecords++ ;
        }

        instance.putInt("NUMBER_OF_RECORDS" , numOfRecords );
            return result;
    }




    private int setScore() {
        return score;
    }
}


