package com.mir.gametics;

public class GameHolder {

    private int id;
    private String name;


    private final static GameHolder INSTANCE = new GameHolder();


    private GameHolder() {}


    public static GameHolder getInstance() {
        return INSTANCE;
    }


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}
