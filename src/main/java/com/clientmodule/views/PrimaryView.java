package com.clientmodule.views;

import com.clientmodule.controllers.PrimaryController;
import com.clientmodule.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PrimaryView {
    private User currentUser;

    public PrimaryView(User currentUser) {
        this.currentUser = currentUser;
    }


    public void startWithResources() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml-files/main_view.fxml"));

            PrimaryController controller = new PrimaryController();
            controller.loadData(currentUser);
            fxmlLoader.setController(controller);

            Scene scene = new Scene(fxmlLoader.load(), 800, 550);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setTitle("Hammer Library | Books hub");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
        }
        catch (IOException e) {
           e.printStackTrace();
        }

    }


}
