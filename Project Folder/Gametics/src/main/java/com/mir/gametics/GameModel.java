package com.mir.gametics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GameModel {

    ObservableList<Game> gameList = FXCollections.observableArrayList();

    public ObservableList<Game> getGameList() {
        return gameList;
    }

    public void setGameList(ObservableList<Game> gameList) {
        this.gameList = gameList;
    }

    public void addGame(Game game) {
        gameList.add(game);
    }

    public void clearList() {
        gameList.clear();
    }
}
