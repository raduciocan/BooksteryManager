package com.clientmodule.controllers;

import com.clientmodule.EntityMaster;
import com.clientmodule.StagesMaster;
import com.clientmodule.models.*;
import com.clientmodule.views.BookDetailsView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrimaryController implements Initializable {
    private EntityMaster entity;
    private BooksSortModes currentSortMode;
    private User currentUser;
    private Book currentSelectedBook;
    private ObservableList books;

    @FXML
    private Label userLabel;
    @FXML
    private Button logOutButton;
    @FXML
    private Button addBookButton;
    @FXML
    private Button selectButton;
    @FXML
    private Button deleteBookButton;
    @FXML
    private Button rentButton;
    @FXML
    private TableView<Book> allBooksTable;
    @FXML
    private TableColumn<Book, Integer> idColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private Label selectedBookLabel;
    @FXML
    private ComboBox<BooksSortModes> filterComboBox;

    @FXML
    protected void onFilterComboBoxAction() {
        currentSortMode = filterComboBox.getValue();
        updateBooksList(currentSortMode);
    }

    @FXML
    protected void onAddBookButtonClick() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add book wizard");
        dialog.setHeaderText("Create a new book entity and add it to the database");

        ImageView imageView = new ImageView(this.getClass().getResource("/icons/book.png").toString());
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        dialog.setGraphic(imageView);

        ButtonType addBookButtonType = new ButtonType("Add Book", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBookButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Name of the Book");
        TextField authorField = new TextField();
        authorField.setPromptText("Name of the Book's Author");

        grid.add(new Label("Book Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author Name:"), 0, 1);
        grid.add(authorField, 1, 1);

        Node addBookButton = dialog.getDialogPane().lookupButton(addBookButtonType);
        addBookButton.setDisable(true);

        //validation for add button disable
        AtomicBoolean isTitleEmpty = new AtomicBoolean(true);
        AtomicBoolean isAuthorEmpty = new AtomicBoolean(true);
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            isTitleEmpty.set(newValue.trim().isEmpty());
            addBookButton.setDisable(isTitleEmpty.get() || isAuthorEmpty.get());
        });
        authorField.textProperty().addListener((observable, oldValue, newValue) -> {
            isAuthorEmpty.set(newValue.trim().isEmpty());
            addBookButton.setDisable(isTitleEmpty.get() || isAuthorEmpty.get());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> titleField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addBookButtonType) {
                return new Pair<>(titleField.getText(), authorField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(titleAuthorPair -> {
            System.out.println("Book title = " + titleAuthorPair.getKey() + ", Book author = " + titleAuthorPair.getValue());
            entity.postBook(1, titleAuthorPair.getKey(), titleAuthorPair.getValue());
        });

        updateBooksList(currentSortMode);
    }

    @FXML
    protected void onDeleteButtonClick() {
        if(currentSelectedBook == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select the book that you want to delete!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm book deletion");
        alert.setHeaderText("Are you sure you want to remove this book?");
        alert.setContentText("Book \""+ currentSelectedBook.getBookTitle()+ "\" written by " + currentSelectedBook.getBookAuthor() +"\nwill be permanently deleted from database!");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            entity.deleteBook(currentSelectedBook);
            currentSelectedBook = null;
            selectedBookLabel.setText(" ");
            updateBooksList(currentSortMode);
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION, "Book entry has been deleted!", ButtonType.OK);
            alertInfo.showAndWait();
        } else {
            return;
        }
    }

    @FXML
    protected void onRefreshAllButtonClick() {
        updateBooksList(currentSortMode);
        initiateLibraryTable();
    }

    @FXML
    protected void onViewBookButtonClick() {
        openBookDetailsWindow();
    }

    @FXML
    protected void onRentButtonClick() {
        if(currentSelectedBook == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select the book that you wish to acquire!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm book rental");
        alert.setHeaderText("Are you sure you want to rent this book?\nRent period: 2 weeks");
        alert.setContentText("Book \""+ currentSelectedBook.getBookTitle()+ "\" written by " + currentSelectedBook.getBookAuthor() +"\nwill be rented for a duration of 14 days!");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            Rent newRent = new Rent();
            newRent.setUserId(currentUser.getId());
            newRent.setBookId(currentSelectedBook.getId());
            //setting review post date as current date ( java.time.LocalDate.now() )
            newRent.setRentDate(Date.valueOf(LocalDate.now()));
            newRent.setRentDue(Date.valueOf(LocalDate.now().plusDays(14)));
            entity.postRent(newRent);
            updateBooksList(currentSortMode);
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION, "You have rented " + currentSelectedBook.getBookTitle()+" for 14 days!", ButtonType.OK);
            alertInfo.showAndWait();
        } else {
            return;
        }
    }


    @FXML
    protected void onLogOutButtonClick() {
        //implement
        logOutButton.getScene().getWindow().hide();
    }


    private void initiateUser() {
        userLabel.setText(currentUser.getUserName());
    }

    private void updateBooksList(BooksSortModes mode) {
        List result;
        switch (mode) {
            case MINE:
                result = entity.getBooksRentedByUser(currentUser);
                break;
            case AVAILABLE:
                result = entity.getBooksNotRented();
                break;
            default:
            case ALL:
                result = entity.getAllBooks();
                break;

        }
        if (books == null)
            books = FXCollections.observableArrayList(result);
        else {
            books.clear();
            books.addAll(result);
        }
    }

    private void initiateLibraryTable() {
        allBooksTable.setOnMouseClicked(event -> {
            if (allBooksTable.getSelectionModel().getSelectedItem() == null) {
                selectedBookLabel.setText(" ");
                return;
            }
            if (allBooksTable.getSelectionModel().getSelectedItem().equals(currentSelectedBook)) {
                currentSelectedBook = null;
                allBooksTable.getSelectionModel().clearSelection();
                selectedBookLabel.setText(" ");
                return;
            }
            currentSelectedBook = allBooksTable.getSelectionModel().getSelectedItem();
            selectedBookLabel.setText(currentSelectedBook.getBookTitle());
        });
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn = new TableColumn<>();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        titleColumn = new TableColumn<>();
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("BookAuthor"));
        authorColumn = new TableColumn<>();
        allBooksTable.setItems(books);
    }

    private void openBookDetailsWindow() {
        BookDetailsView bookDetailsView = new BookDetailsView(currentUser, currentSelectedBook);
        bookDetailsView.startWithResources();
    }

    public void loadData(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //we get the persistence entity manager from the StagesMaster singleton
        entity = StagesMaster.getInstance().getEntity();

        filterComboBox.getItems().clear();
        filterComboBox.getItems().addAll(BooksSortModes.values());
        filterComboBox.getSelectionModel().selectFirst();

        initiateUser();
        currentSortMode = filterComboBox.getValue();
        updateBooksList(currentSortMode);
        initiateLibraryTable();
    }

}
