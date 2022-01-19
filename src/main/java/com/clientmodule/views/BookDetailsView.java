package com.clientmodule.views;

import com.clientmodule.controllers.BookDetailsController;
import com.clientmodule.models.Book;
import com.clientmodule.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class BookDetailsView {
    private User currentUser;
    private Book currentBook;

    public BookDetailsView(User currentUser, Book currentBook) {
        this.currentUser = currentUser;
        this.currentBook = currentBook;
    }

    public void startWithResources() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml-files/book-details_view.fxml"));

            BookDetailsController controller = new BookDetailsController();
            controller.loadData(currentUser, currentBook);
            fxmlLoader.setController(controller);

            Scene scene = new Scene(fxmlLoader.load(), 750, 700);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setTitle("Hammer Library | Book Information");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
