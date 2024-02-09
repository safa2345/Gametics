package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class LoginPageController {

    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink createOneHyperLink, backToHomeHyperLink;

    private Connection connection;
    private LogInStatus logInStatus;

    @FXML
    public void initialize() {
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=p@ssword81";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        logInStatus = LogInStatus.getInstance();

        createOneHyperLink.setOnAction(event -> loadFxml("fxml/create_account_page.fxml"));
        backToHomeHyperLink.setOnAction(event -> loadFxml("fxml/home_page.fxml"));

    }

    public void onLogin() {
        String userName = userField.getText();
        String userPassword = passwordField.getText();

        String sql_query = "select * from Users where user_name = ? and user_password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPassword);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                DialogBox.getDialogBox((Stage) userField.getScene().getWindow(), "Wrong Username/Password");
            } else {
                logInStatus.setLoggedIn(true);
                FXMLLoader fxmlLoader = new FXMLLoader(LoginPageController.class.getResource("fxml/all_game_page.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) userField.getScene().getWindow();
                stage.setScene(scene);

            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    private void loadFxml(String fxmlLocation) {
        FXMLLoader fxmlLoader = new FXMLLoader(Gametics.class.getResource(fxmlLocation));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) backToHomeHyperLink.getScene().getWindow();
        stage.setScene(scene);
    }
}
