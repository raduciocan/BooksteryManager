package com.clientmodule.controllers;

import com.clientmodule.EntityMaster;
import com.clientmodule.StagesMaster;
import com.clientmodule.models.Book;
import com.clientmodule.models.Review;
import com.clientmodule.models.ReviewsSortModes;
import com.clientmodule.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookDetailsController implements Initializable {
    private EntityMaster entity;
    private Book currentBook;
    private User currentUser;
    private Review currentSelectedReview;
    private ObservableList reviews;

    @FXML
    private Button logOutButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<Review> reviewsTable;
    @FXML
    private TableColumn<Review, Double> reviewRatingColumn;
    @FXML
    private TableColumn<Review, String> reviewReaderColumn;
    @FXML
    private TableColumn<Review, String> reviewDateColumn;
    @FXML
    private ComboBox<ReviewsSortModes> reviewsSortSelector;
    @FXML
    private Slider ratingSlider;
    @FXML
    private Label ratingLabel;
    @FXML
    private TextArea reviewTextArea;

    @FXML
    protected void onLogOutButtonClick() {
        //implement log out
        logOutButton.getScene().getWindow().hide();
    }

    @FXML
    protected void onAddReviewButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm new review submission");
        alert.setHeaderText("Are you sure you want to post the following review?");
        alert.setContentText("For \"" + currentBook.getBookTitle() + "\" " +
                        "by" + " \"" + currentBook.getBookAuthor() + "\"\n" +
                "Rating: " + ratingSlider.getValue() + "\n" +
                "Review message:\n" + reviewTextArea.getText()
        );

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Review newReview = new Review();

            newReview.setBookId(currentBook.getId());
            newReview.setUserId(currentUser.getId());
            //setting review post date as current date ( java.time.LocalDate.now() )
            newReview.setReviewDate(Date.valueOf(LocalDate.now()));
            newReview.setReviewRating(ratingSlider.getValue());
            newReview.setReviewText(reviewTextArea.getText());

            entity.postReview(newReview);
            updateReviewsList();
        } else {
            return;
        }
    }

    @FXML
    protected void onDeleteReviewButtonClick() {
        Alert alert;
        if(currentSelectedReview == null) {
            alert = new Alert(Alert.AlertType.ERROR, "Please select the review that you want to delete!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm review deletion");
        alert.setHeaderText("Are you sure you want to delete this review?");
        alert.setContentText("Review by " + currentSelectedReview.getUsersByUserId().getUserName() + " for book \"" + currentSelectedReview.getBooksByBookId().getBookTitle() +"\"\nwill be permanently deleted! Commit?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            entity.deleteReview(currentSelectedReview);
            currentSelectedReview = null;
            //update review text area here
            updateReviewsList();
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION, "Book review has been deleted!", ButtonType.OK);
            alertInfo.showAndWait();
        } else {
            return;
        }

    }
    public void loadData(User user, Book book) {
        currentBook = book;
        currentUser = user;
    }

    private void updateReviewsList() {
        List result = entity.getReviewByQuery("SELECT r FROM Review r WHERE r.bookId=" + currentBook.getId());
        if (reviews == null)
            reviews = FXCollections.observableArrayList(result);
        else {
            reviews.clear();
            reviews.addAll(result);
        }
    }

    private void initiateReviewsTable() {
        reviewsTable.setOnMouseClicked(event -> {
            if (reviewsTable.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            if (reviewsTable.getSelectionModel().getSelectedItem().equals(currentSelectedReview)) {
                currentSelectedReview = null;
                reviewsTable.getSelectionModel().clearSelection();
                return;
            }
            currentSelectedReview = reviewsTable.getSelectionModel().getSelectedItem();
            //update review details pane here
        });
        reviewRatingColumn.setCellValueFactory(new PropertyValueFactory("reviewRating"));
        reviewRatingColumn = new TableColumn<>();
        reviewReaderColumn.setCellValueFactory(new PropertyValueFactory("usersByUserId"));
        reviewReaderColumn = new TableColumn<>();
        reviewDateColumn.setCellValueFactory(new PropertyValueFactory("reviewDate"));
        reviewDateColumn = new TableColumn<>();
        reviewsTable.setItems(reviews);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //we get the persistence entity manager from the StagesMaster singleton
        entity = StagesMaster.getInstance().getEntity();

        userNameLabel.setText(currentUser.getUserName());
        userRoleLabel.setText(currentUser.getUserRole());
        titleLabel.setText(currentBook.getBookTitle());
        authorLabel.setText(currentBook.getBookAuthor());
        updateReviewsList();
        initiateReviewsTable();

        ratingSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                ratingLabel.setText(Double.toString(BigDecimal.valueOf(newValue.doubleValue()).setScale(2, RoundingMode.HALF_UP).doubleValue()))
        );

        reviewsSortSelector.getItems().clear();
        reviewsSortSelector.getItems().addAll(ReviewsSortModes.values());
        reviewsSortSelector.getSelectionModel().selectFirst();
    }
}
