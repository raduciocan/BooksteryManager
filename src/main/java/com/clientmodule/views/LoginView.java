package com.clientmodule.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginView extends Application {
    @Override
    public void init() {
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml-files/login_view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 300, 420);
            stage.setScene(scene);
            stage.setTitle("Library Log-in / Sign-up");
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
