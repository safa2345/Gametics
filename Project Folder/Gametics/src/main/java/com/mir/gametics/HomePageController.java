package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {

    private final PageLoadNumber pageLoadNumber = PageLoadNumber.getInstance();

    @FXML
    private Hyperlink allGames, recentGames, popularGames, searchGames, logIn, createAccount;

    @FXML
    public void initialize() {
        allGames.setOnAction(event -> {
            pageLoadNumber.setPageNumber(1);
            loadFxml("fxml/all_game_page.fxml");
        });
        recentGames.setOnAction(event -> {
            pageLoadNumber.setPageNumber(2);
            loadFxml("fxml/all_game_page.fxml");
        });
        searchGames.setOnAction(event -> loadFxml("fxml/search_game_page.fxml"));
        logIn.setOnAction(event -> loadFxml("fxml/login_page.fxml"));
        createAccount.setOnAction(event -> loadFxml("fxml/create_account_page.fxml"));
    }

    private void loadFxml(String fxmlLocation) {
        FXMLLoader fxmlLoader = new FXMLLoader(Gametics.class.getResource(fxmlLocation));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) logIn.getScene().getWindow();
        stage.setScene(scene);
    }
}
