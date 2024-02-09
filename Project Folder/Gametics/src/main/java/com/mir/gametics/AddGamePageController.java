package com.mir.gametics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.converter.StringConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class AddGamePageController {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private GameModel gameModel;
    private GameHolder gameHolder;

    @FXML
    private ComboBox<String> selectCategory, selectDeveloper, selectPublisher, selectLanguage,
            selectMemory, selectPopularity, selectStorage, selectDirectX;
    @FXML
    private CheckComboBox<String> selectOS, selectProcessor, selectGraphics;
    @FXML
    private TextField nameTextField, priceTextField, processorTextField, graphicsTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button addGameButton, backButton;
    @FXML
    private DatePicker datePicker;
    private String gameId = null;

    final ObservableList<String> categoryNameList = FXCollections.observableArrayList();
    final ObservableList<String> languageList = FXCollections.observableArrayList();
    final ObservableList<String> developerList = FXCollections.observableArrayList();
    final ObservableList<String> publisherList = FXCollections.observableArrayList();
    final ObservableList<String> osList = FXCollections.observableArrayList();
    final ObservableList<String> processorNameList = FXCollections.observableArrayList();
    final ObservableList<String> memoryList = FXCollections.observableArrayList();
    final ObservableList<String> storageList = FXCollections.observableArrayList();
    final ObservableList<String> directXList = FXCollections.observableArrayList();
    final ObservableList<String> graphicsCardList = FXCollections.observableArrayList();
    final ObservableList<String> popularityList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {


        gameModel = new GameModel();
        gameHolder = GameHolder.getInstance();

        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=p@ssword81";
        try {
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getListFromDatabase("Select * from Category", categoryNameList, selectCategory);
        getListFromDatabase("Select * from Language", languageList, selectLanguage);
        getListFromDatabase("Select * from Developer", developerList, selectDeveloper);
        getListFromDatabase("Select * from Publisher", publisherList, selectPublisher);
        getListFromDatabase("Select * from OS", osList, selectOS);
        getListFromDatabase("Select * from Processor", processorNameList, selectProcessor);
        getListFromDatabase("Select * from Memory", memoryList, selectMemory);
        getListFromDatabase("Select * from Review", popularityList, selectPopularity);
        getListFromDatabase("Select * from Storage", storageList, selectStorage);
        getListFromDatabase("Select * from DirectX", directXList, selectDirectX);
        getListFromDatabase("Select * from Graphics", graphicsCardList, selectGraphics);

        backButton.setOnAction(actionEvent -> loadFxml("fxml/all_game_page.fxml"));
        addGameButton.setOnAction(actionEvent -> addGame());

    }

    private void addGame() {

        if(nameTextField.getText().equals("")) {
            DialogBox.getDialogBox(getStage(), "Please Enter Game Name");
        }
        if(selectCategory.getSelectionModel().isEmpty() && selectCategory.getValue().isEmpty() ) {
            DialogBox.getDialogBox(getStage(), "Please Enter Game Category");
        }
        if(selectDeveloper.getSelectionModel().isEmpty() && selectDeveloper.getValue().isEmpty()) {
            DialogBox.getDialogBox(getStage(), "Please Enter Game Developer");
        }
        if(priceTextField.getText().equals("")) {
            DialogBox.getDialogBox(getStage(), "Please Enter Game Price");
        }
        if(datePicker.getValue() == null) {
            DialogBox.getDialogBox(getStage(), "Please Enter Game Release Date");
        }
        if(descriptionTextArea.getText().equals("")) {
            DialogBox.getDialogBox(getStage(), "Please Enter Game Description");
        }



        String name = nameTextField.getText();
        String category = selectCategory.getValue();
        String categoryId = null;
        String developerId = null;
        String releaseDate = (datePicker.getValue()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String price = priceTextField.getText();
        String description = descriptionTextArea.getText();
        String developer = selectDeveloper.getValue();

        System.out.println(checkGameCategory(category));

        if(checkGameName(name) && checkGameCategory(category) && checkGameDeveloper(developer)) {

            String insertQuery = "insert into GamesBasicInfo(game_name, game_release_date, game_price, game_description)\n" +
                    "Values (?,?,?,?)";
            try {
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, releaseDate);
                preparedStatement.setString(3, price);
                preparedStatement.setString(4,description);
                preparedStatement.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            }

            String findGameId = "select game_id\n" +
                    "from GamesBasicInfo\n" +
                    "where game_name = ?";

            String findCategoryId = "select category_id\n" +
                    "from Category\n" +
                    "where category_name = ?";

            String completeInsertQuery = "insert into GameCategory\n" +
                    "values(?,?)";
            try {
                preparedStatement = connection.prepareStatement(findGameId);
                preparedStatement.setString(1, name);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    gameId = resultSet.getString(1);
                }
                preparedStatement = connection.prepareStatement(findCategoryId);
                preparedStatement.setString(1, category);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    categoryId = resultSet.getString(1);
                }
                preparedStatement = connection.prepareStatement(completeInsertQuery);
                preparedStatement.setString(1, gameId);
                preparedStatement.setString(2, categoryId);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            String findDeveloperId = "select developer_id from Developer\n" +
                    "where developer_name = ?";

            completeInsertQuery = "insert into GameDeveloper\n" +
                    "values(?,?)";
            try {

                preparedStatement = connection.prepareStatement(findDeveloperId);
                preparedStatement.setString(1, developer);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    developerId = resultSet.getString(1);
                }
                preparedStatement = connection.prepareStatement(completeInsertQuery);
                preparedStatement.setString(1, gameId);
                preparedStatement.setString(2, developerId);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkGameDeveloper(String developer) {
        if(developerList.contains(developer)) {
            return true;
        }
        else {
            if(developer.length() > 30) {
                DialogBox.getDialogBox(getStage(),"Developer Name is too Long");
                return false;
            }
            else {
                String query = "insert into Developer values (?)";
                try {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, developer);
                    preparedStatement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    private boolean checkGameCategory(String category) {
        if(categoryNameList.contains(category)) {
            return true;
        }
        else {
            if(category.length() > 10) {
                DialogBox.getDialogBox(getStage(),"Category Name is too Long");
                return false;
            }
            else {
                for (int i = 0; i < category.length(); i++) {
                    char c = category.charAt(i);
                    if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z')) {
                        DialogBox.getDialogBox(getStage(), "Category Contains Invalid Character");
                        return false;
                    }
                }
            }
            String query = "insert into Category values (?)";
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, category);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }



    private boolean checkGameName(String name) {
        String query = "select * from GamesBasicInfo\n" +
                "where game_name = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                DialogBox.getDialogBox(getStage(),"Games with similar name exists!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void getListFromDatabase(String query, ObservableList<String> list, ComboBox<String> comboBox) throws SQLException {
        // fetching category name for category combo box
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            list.add(resultSet.getString(2));
        }

        // setting category combo box value
        comboBox.getItems().addAll(list);

    }

    private void getListFromDatabase(String query, ObservableList<String> list, CheckComboBox<String> comboBox) throws SQLException {
        // fetching category name for category combo box
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            list.add(resultSet.getString(2));
        }

        // setting category combo box value
        comboBox.getItems().addAll(list);

    }

    private void loadFxml(String fxmlLocation) {
        FXMLLoader fxmlLoader = new FXMLLoader(Gametics.class.getResource(fxmlLocation));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) addGameButton.getScene().getWindow();
        stage.setScene(scene);
    }

    private Stage getStage() {
        return (Stage) nameTextField.getScene().getWindow();
    }


}
