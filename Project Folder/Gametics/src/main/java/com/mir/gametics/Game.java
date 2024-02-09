package com.mir.gametics;

public class Game {
    private String gameName;
    private String gameDeveloper;
    private String gameCategory;
    private String gameReleaseDate;
    private String gamePrice;
    private String gameDescription;


    public Game() {
    }

    public Game(String gameName, String gameCategory, String gameReleaseDate, String gamePrice) {
        this.gameName = gameName;
        this.gameCategory = gameCategory;
        this.gameReleaseDate = gameReleaseDate;
        this.gamePrice = gamePrice;
    }

    public Game(String gameName) {
        this.gameName = gameName;
    }
//    public Game(String gameName, String gameCategory, String gameReleaseDate, String gamePrice, String gameDescription) {
//        this.gameName = gameName;
//        this.gameCategory = gameCategory;
//        this.gameReleaseDate = gameReleaseDate;
//        this.gamePrice = gamePrice;
//        this.gameDescription = gameDescription;
//    }

    public Game(String gameName, String gameDeveloper, String gameCategory, String gameReleaseDate, String gamePrice) {
        this.gameName = gameName;
        this.gameDeveloper = gameDeveloper;
        this.gameCategory = gameCategory;
        this.gameReleaseDate = gameReleaseDate;
        this.gamePrice = gamePrice;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameCategory() {
        return gameCategory;
    }

    public void setGameCategory(String gameCategory) {
        this.gameCategory = gameCategory;
    }

    public String getGameReleaseDate() {
        return gameReleaseDate;
    }

    public void setGameReleaseDate(String gameReleaseDate) {
        this.gameReleaseDate = gameReleaseDate;
    }

    public String getGamePrice() {
        return gamePrice;
    }

    public void setGamePrice(String gamePrice) {
        this.gamePrice = gamePrice;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public String getGameDeveloper() {
        return gameDeveloper;
    }

    public void setGameDeveloper(String gameDeveloper) {
        this.gameDeveloper = gameDeveloper;
    }
}
