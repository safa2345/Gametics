package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class DetailedPageController {
    @FXML
    private ImageView posterImageView, gameplayImageView;
    @FXML
    private Label titleLabel, dateLabel, developerLabel, categoryLabel, priceLabel, osLabel,
            processorLabel, memoryLabel, graphicsLabel, directXLabel, storageLabel, languageLabel,
            reviewLabel, publisherLabel;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button backButton;

    private String id;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @FXML
    public void initialize() {
        GaussianBlur blur = new GaussianBlur(30);
        gameplayImageView.setEffect(blur);
        descriptionTextArea.setWrapText(true);

        backButton.setOnAction(actionEvent -> loadFxml("fxml/all_game_page.fxml"));

        GameHolder gameHolder = GameHolder.getInstance();
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=p@ssword81";

        try {
            connection = DriverManager.getConnection(connectionUrl);
            System.out.println("successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        titleLabel.setText(gameHolder.getName());

        String sqlQuery = "select GamesBasicInfo.game_id, GamesBasicInfo.game_name, Developer.developer_name, Category.category_name, GamesBasicInfo.game_release_date, GamesBasicInfo.game_price, GamesBasicInfo.game_poster, GamesBasicInfo.gameplay_picture, GamesBasicInfo.game_description\n" +
                "       from GamesBasicInfo\n" +
                "       INNER JOIN GameCategory on GamesBasicInfo.game_id = GameCategory.game_id\n" +
                "       INNER JOIN Category on Category.category_id = GameCategory.category_id\n" +
                "       INNER JOIN GameDeveloper on GamesBasicInfo.game_id = GameDeveloper.game_id\n" +
                "       INNER JOIN Developer  on GameDeveloper.developer_id = Developer.developer_id\n" +
                "       where game_name = ?";

        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, gameHolder.getName());
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                id = resultSet.getString(1);
                InputStream inputStream = resultSet.getBinaryStream("game_poster");
                if(inputStream!=null) posterImageView.setImage(new Image(inputStream));
                InputStream inputStream2 = resultSet.getBinaryStream("gameplay_picture");
                if(inputStream2!=null) gameplayImageView.setImage(new Image(inputStream2));
                dateLabel.setText(resultSet.getString(5));
                developerLabel.setText(resultSet.getString(3));
                categoryLabel.setText(resultSet.getString(4));
                priceLabel.setText("$" + resultSet.getString(6));
                descriptionTextArea.setText(resultSet.getString(9));

            }

            sqlQuery = "select STRING_AGG(Language.language_name, ' , ')\n" +
                    "from GamesBasicInfo\n" +
                    "         JOIN GameLanguage\n" +
                    "              on GamesBasicInfo.game_id = GameLanguage.game_id\n" +
                    "         join Language\n" +
                    "              on Language.language_id = GameLanguage.language_id and GamesBasicInfo.game_id = GameLanguage.game_id\n" +
                    "where GamesBasicInfo.game_id = ?\n" +
                    "group by GamesBasicInfo.game_id\n";

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                languageLabel.setText(resultSet.getString(1));
            }

            sqlQuery = "select Publisher.publisher_name\n" +
                    "from GamesBasicInfo\n" +
                    "         JOIN GamePublisher\n" +
                    "              on GamesBasicInfo.game_id = GamePublisher.game_id\n" +
                    "         join Publisher\n" +
                    "              on Publisher.publisher_id = GamePublisher.publisher_id and GamesBasicInfo.game_id = GamePublisher.game_id\n" +
                    "where GamesBasicInfo.game_id = ?\n" +
                    "group by Publisher.publisher_name";

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                publisherLabel.setText(resultSet.getString(1));
            }

            sqlQuery = "select Review.review_value\n" +
                    "from GamesBasicInfo\n" +
                    "         JOIN GameReview\n" +
                    "              on GamesBasicInfo.game_id = GameReview.game_id\n" +
                    "         join Review\n" +
                    "              on Review.review_id = GameReview.review_id and GamesBasicInfo.game_id = GameReview.game_id\n" +
                    "where GamesBasicInfo.game_id = ?\n" +
                    "group by Review.review_value";

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                reviewLabel.setText(resultSet.getString(1));
            }


            sqlQuery = "select STRING_AGG(OS.os_name, ' , ')\n" +
                    "from GamesBasicInfo\n" +
                    "JOIN GameOS\n" +
                    "on GamesBasicInfo.game_id = GameOS.game_id\n" +
                    "join OS\n" +
                    "on OS.os_id = GameOS.os_id and GamesBasicInfo.game_id = GameOS.game_id\n" +
                    "where GamesBasicInfo.game_id = ?\n" +
                    "group by GamesBasicInfo.game_id";

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                osLabel.setText(resultSet.getString(1));
            }

            sqlQuery = "select STRING_AGG(Processor.processor_name, ' , ')\n" +
                    "from GamesBasicInfo\n" +
                    "         JOIN GameProcessor\n" +
                    "              on GamesBasicInfo.game_id = GameProcessor.game_id\n" +
                    "         join Processor\n" +
                    "              on Processor.processor_id = GameProcessor.processor_id and GamesBasicInfo.game_id = GameProcessor.game_id\n" +
                    "where GamesBasicInfo.game_id = ?\n" +
                    "group by GamesBasicInfo.game_id";

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                processorLabel.setText(resultSet.getString(1));
            }

            sqlQuery = "select STRING_AGG(Memory.memory_value, ' , ')\n" +
                    "from GamesBasicInfo\n" +
                    "         JOIN GameMinMemory\n" +
                    "              on GamesBasicInfo.game_id = GameMinMemory.game_id\n" +
                    "         join Memory\n" +
                    "              on Memory.memory_id = GameMinMemory.memory_id and GamesBasicInfo.game_id = GameMinMemory.game_id\n" +
                    "where GamesBasicInfo.game_id = ?\n" +
                    "group by GamesBasicInfo.game_id";

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                memoryLabel.setText(resultSet.getString(1));
            }

            sqlQuery = "select STRING_AGG(Graphics.graphics_name, ' , ')\n" +
                    "from GamesBasicInfo\n" +
                    "         JOIN GameMinGraphics\n" +
                    "              on GamesBasicInfo.game_id = GameMinGraphics.game_id\n" +
                    "         join Graphics\n" +
                    "              on Graphics.graphics_id = GameMinGraphics.graphics_id and GamesBasicInfo.game_id = GameMinGraphics.game_id\n" +
                    "where GamesBasicInfo.game_id = ?\n" +
                    "group by GamesBasicInfo.game_id";

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                graphicsLabel.setText(resultSet.getString(1));
            }

            sqlQuery = "select STRING_AGG(DirectX.directx_name, ' , ')\n" +
                    "from GamesBasicInfo\n" +
                    "         JOIN GameMinDirectX\n" +
                    "              on GamesBasicInfo.game_id = GameMinDirectX.game_id\n" +
                    "         join DirectX\n" +
                    "              on DirectX.directx_id = GameMinDirectX.directx_id and GamesBasicInfo.game_id = GameMinDirectX.game_id\n" +
                    "where GamesBasicInfo.game_id = ?\n" +
                    "group by GamesBasicInfo.game_id";

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                directXLabel.setText(resultSet.getString(1));
            }

            sqlQuery = "select Storage.storage_value\n" +
                    "from GamesBasicInfo\n" +
                    "         JOIN GameStorage\n" +
                    "              on GamesBasicInfo.game_id = GameStorage.game_id\n" +
                    "         join Storage\n" +
                    "              on Storage.storage_id = GameStorage.storage_id and GamesBasicInfo.game_id = GameStorage.game_id\n" +
                    "where GamesBasicInfo.game_id = ?\n" +
                    "group by Storage.storage_value";


            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                storageLabel.setText(resultSet.getString(1) + " GB HD space");
            }


        } catch (SQLException e) {
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
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
    }
}
