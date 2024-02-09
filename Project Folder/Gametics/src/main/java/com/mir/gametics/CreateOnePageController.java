package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Pattern;

public class CreateOnePageController {

    private Connection connection;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField userNameTextField, userEmailTextField;
    @FXML
    private PasswordField userPasswordField, userAdminCodeField;

    @FXML
    private Hyperlink loginPageHyperLink, homePageHyperLink;

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @FXML
    public void initialize()
    {
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=p@ssword81";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createAccountButton.setOnAction(actionEvent -> {
            onCreateAccount();
            try {
                backToLoginPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        loginPageHyperLink.setOnAction(actionEvent -> {
            try {
                backToLoginPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        homePageHyperLink.setOnAction(actionEvent -> {
            try {
                backToHomePage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



    private void onCreateAccount() {
        String userName = userNameTextField.getText();
        String userEmail = userEmailTextField.getText();
        String userPassword = userPasswordField.getText();
        String adminCode = userAdminCodeField.getText();
        if (checkUserName(userName) && checkUserEmail(userEmail) && checkUserPassword(userPassword) && checkAdminCode(adminCode)) {
            String insertQuery = "INSERT INTO Users VALUES (?, ?, ?)";

            try {
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, userEmail);
                preparedStatement.setString(3, userPassword);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean checkAdminCode(String adminCode) {
        String query = "select * from AdminPassword where admin_password = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,adminCode);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkUserPassword(String userPassword) {
        if(userPassword.length() >= 8) {
            return true;
        }
        else {
            DialogBox.getDialogBox(getStage(), "Password is too Small");
            return false;
        }
    }

    private boolean checkUserEmail(String userEmail) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if(!pattern.matcher(userEmail).matches()) {
            DialogBox.getDialogBox(getStage(), "Invalid Email");
        }

        String query = "Select * from Users where user_email = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userEmail);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                DialogBox.getDialogBox(getStage(), "Email is Taken");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean checkUserName(String userName) {
        String query = "Select * from Users where user_name = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                DialogBox.getDialogBox(getStage(), "User Name is Taken");
                return false;
            }
            if(userName.length() > 15) {
                DialogBox.getDialogBox(getStage(), "User Name is Too Long");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void backToHomePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Gametics.class.getResource("fxml/home_page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) createAccountButton.getScene().getWindow();
        stage.setScene(scene);
    }


    private void backToLoginPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Gametics.class.getResource("fxml/login_page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) createAccountButton.getScene().getWindow();
        stage.setScene(scene);
    }

    private Stage getStage() {
        return (Stage) userNameTextField.getScene().getWindow();
    }
}
