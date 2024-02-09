package com.mir.gametics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogBox {


    public static void getDialogBox(Stage parentStage, String dialogText) {

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(parentStage);
        VBox dialogBox = new VBox();
        dialogBox.setPrefWidth(400);
        dialogBox.setPadding(new Insets(50, 20, 20, 20));
        dialogBox.setSpacing(20);
        Label label = new Label(dialogText);
        label.setPrefWidth(360);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-size: 20px");
        Button button = new Button("OK");
        button.setPadding(new Insets(0, 10, 0, 10));
        button.setOnAction(event -> {
            dialogStage.close();
        });
        VBox.setMargin(button, new Insets(0, 0, 0, 160));
        dialogBox.getChildren().addAll(label, button);
        Scene dialogScene = new Scene(dialogBox, 400, 150);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

}
