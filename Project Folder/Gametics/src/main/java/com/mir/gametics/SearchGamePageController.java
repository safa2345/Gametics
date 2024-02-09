package com.mir.gametics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class SearchGamePageController {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private GameHolder gameHolder;

    @FXML
    private ComboBox<String> categoryComboBox, storageComboBox, processorComboBox, memoryComboBox, graphicsCardComboBox, graphicsCardMemoryComboBox, popularityComboBox;

    @FXML
    private TableView<Game> tableView;

    @FXML
    private TextField titleField;

    @FXML
    private CheckBox exactMatchingCheckBox;

    @FXML
    private Button searchButton, backButton;

    Scene scene;

    final ObservableList<String> categoryNameList = FXCollections.observableArrayList();
    final ObservableList<String> processorNameList = FXCollections.observableArrayList();
    final ObservableList<String> memoryList = FXCollections.observableArrayList();
    final ObservableList<String> storageList = FXCollections.observableArrayList();
    final ObservableList<String> graphicsCardNameList = FXCollections.observableArrayList();
    final ObservableList<String> graphicsCardMemoryList = FXCollections.observableArrayList();
    final ObservableList<String> popularityList = FXCollections.observableArrayList();

    private Boolean isCategorySelected, isProcessorSelected, isMemorySelected, isStorageSelected, isGraphicsSelected, isGraphicsMemorySelected, isReviewSelected;

    private String selectedCategoryName;

    private TableColumn<Game, String> firstColumn;
    private GameModel gameModel;


    @FXML
    public void initialize() throws SQLException {

        gameModel = new GameModel();
        gameHolder = GameHolder.getInstance();

        initializeTable();

        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=p@ssword81";

        try {
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "Select * from Category";
        getListFromDatabase(query, categoryNameList, categoryComboBox);
        query = "select * from Memory";
        getListFromDatabase(query, memoryList, memoryComboBox);
        query = "select * from Storage";
        getListFromDatabase(query, storageList, storageComboBox);
        backButton.setOnAction(actionEvent -> loadFxml("fxml/home_page.fxml"));

        tableView.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {

                gameHolder.setName(tableView.getSelectionModel().getSelectedItem().getGameName());
                FXMLLoader fxmlLoader = new FXMLLoader(AllGamePageController.class.getResource("fxml/detailed-page.fxml"));

                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = (Stage) searchButton.getScene().getWindow();
                stage.setScene(scene);
            }
        });

        searchButton.setOnAction(actionEvent -> {

            System.out.println(titleField.getText().isEmpty());

            tableView.getItems().clear();
            String searchedGame = titleField.getText();
            if (!titleField.getText().isEmpty()) {
                System.out.println("Entered");
                if (exactMatchingCheckBox.isSelected()) {
                    String matchQuery = "select GamesBasicInfo.game_name\n" +
                            "from GamesBasicInfo\n" +
                            "where game_name = ?";
                    try {
                        preparedStatement = connection.prepareStatement(matchQuery);
                        preparedStatement.setString(1, searchedGame);
                        resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()) {
                            gameModel.addGame(new Game(resultSet.getString(1)));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    String matchSimilarQuery = "select GamesBasicInfo.game_name\n" +
                            "from GamesBasicInfo\n" +
                            "where game_name like ?";
                    try {
                        preparedStatement = connection.prepareStatement(matchSimilarQuery);
                        preparedStatement.setString(1, "%" + searchedGame + "%");
                        resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            gameModel.addGame(new Game(resultSet.getString(1)));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }


//            isCategorySelected = !categoryComboBox.getSelectionModel().isEmpty();
//            isMemorySelected = !memoryComboBox.getSelectionModel().isEmpty();

            if (memoryComboBox.getValue() != null) {
                System.out.println("not entered");
                String selectQuery = "select GamesBasicInfo.game_name, Memory.memory_value, GamesBasicInfo.game_release_date, GamesBasicInfo.game_price\n" +
                        "from GamesBasicInfo\n" +
                        "         INNER JOIN GameMinMemory on GamesBasicInfo.game_id = GameMinMemory.game_id\n" +
                        "         INNER JOIN Memory on Memory.memory_id = GameMinMemory.memory_id\n" +
                        "where memory_value = ?";
                try {
                    preparedStatement = connection.prepareStatement(selectQuery);
                    preparedStatement.setString(1,memoryComboBox.getValue());
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        gameModel.addGame(new Game(resultSet.getString(1)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

//            } else if (isCategorySelected) {
//                String selectQuery = "select GamesBasicInfo.game_name, Category.category_name, GamesBasicInfo.game_release_date, GamesBasicInfo.game_price, Memory.memory_value\n" +
//                        "from GamesBasicInfo\n" +
//                        "         INNER JOIN GameCategory on GamesBasicInfo.game_id = GameCategory.game_id\n" +
//                        "         INNER JOIN Category on Category.category_id = GameCategory.category_id\n" +
//                        "         INNER JOIN GameMinMemory on GamesBasicInfo.game_id = GameMinMemory.game_id\n" +
//                        "         INNER JOIN Memory  on Memory.memory_id = GameMinMemory.memory_id\n" +
//                        "where category_name = ?";
//                search(selectQuery, categoryComboBox);
//            } else if (isCategorySelected && isMemorySelected) {
//                String selectQuery = "select GamesBasicInfo.game_name, Category.category_name, GamesBasicInfo.game_release_date, GamesBasicInfo.game_price, Memory.memory_value\n" +
//                        "from GamesBasicInfo\n" +
//                        "         INNER JOIN GameCategory on GamesBasicInfo.game_id = GameCategory.game_id\n" +
//                        "         INNER JOIN Category on Category.category_id = GameCategory.category_id\n" +
//                        "         INNER JOIN GameMinMemory on GamesBasicInfo.game_id = GameMinMemory.game_id\n" +
//                        "         INNER JOIN Memory  on Memory.memory_id = GameMinMemory.memory_id\n" +
//                        "where Memory.memory_value = ? and Category.category_name = ?";
//                try {
//                    preparedStatement = connection.prepareStatement(selectQuery);
//                    preparedStatement.setString(1, memoryComboBox.getValue());
//                    preparedStatement.setString(2, categoryComboBox.getValue());
//                    resultSet = preparedStatement.executeQuery();
//                    while (resultSet.next()) {
//                        gameModel.addGame(new Game(resultSet.getString(1)));
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }


        });
    }

    private void search(String selectQuery, String compare) {
        System.out.println(compare);
        try {
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, compare);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                gameModel.addGame(new Game(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void getListFromDatabase(String query, ObservableList<String> list, ComboBox<String> comboBox) throws
            SQLException {
        // fetching category name for category combo box
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            list.add(resultSet.getString(2));
        }

        // setting category combo box value
        comboBox.getItems().addAll(list);

    }

    public void initializeTable() {

        tableView.setEditable(true);
        // Table Columns
        firstColumn = new TableColumn<>("Game Name");

        // Setting value type for column
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        // setting item on table
        tableView.setItems(gameModel.getGameList());
        // adding column to table
        tableView.getColumns().add(firstColumn);
    }

    private void loadFxml(String fxmlLocation) {
        FXMLLoader fxmlLoader = new FXMLLoader(Gametics.class.getResource(fxmlLocation));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.setScene(scene);
    }
}
