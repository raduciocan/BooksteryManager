package com.clientmodule.controllers;

import com.clientmodule.EntityMaster;
import com.clientmodule.StagesMaster;
import com.clientmodule.models.userRoles;
import com.clientmodule.models.User;
import com.clientmodule.views.PrimaryView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController implements Initializable {
    private EntityMaster entity;
    private User currentUser;
    private PrimaryView primaryView;

    @FXML
    private TextField emailLoginField;
    @FXML
    private TextField passwordLoginField;
    @FXML
    private ComboBox<userRoles> roleSignupSelector;
    @FXML
    private TextField emailSignupField;
    @FXML
    private TextField usernameSignupField;
    @FXML
    private TextField passwordSignupField1;
    @FXML
    private TextField passwordSignupField2;

    @FXML
    protected void onLoginButtonClick() {
        String email = emailLoginField.getText().trim();
        String password = passwordLoginField.getText().trim();

        if(validateLoginFields(email, password)) {
            currentUser = entity.logInUser(email, password);
            System.out.println(currentUser.getUserName() + " " + currentUser.getUserEmail());
            openPrimaryViewWindow();
        }
    }

    private boolean validateLoginFields(String email, String password) {
        Alert alert;

        if (!entity.doesUserExist(email)) {
            alert = new Alert(Alert.AlertType.WARNING, "Specified email does not exist!\nTry writing it again, or create a new account if you don't have one.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        if (!entity.checkUserPassword(email, password)) {
            alert = new Alert(Alert.AlertType.WARNING, "Wrong password! Try again!", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    protected void onSignupButtonClick() {
        String email = emailSignupField.getText();
        String name = usernameSignupField.getText();
        String role = roleSignupSelector.getSelectionModel().getSelectedItem().toString();
        String password = passwordSignupField1.getText();

        if(!validateSignupFields(email, name, role, password))
            return;
        if(entity.doesUserExist(email)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is already an account associated with this email!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        currentUser = new User();
        currentUser.setUserEmail(email);
        currentUser.setUserName(name);
        currentUser.setUserRole(role);
        currentUser.setUserPassword(password);

        entity.registerNewUser(currentUser);

        openPrimaryViewWindow();
    }

    private boolean validateSignupFields(String email, String name, String role, String password) {
        Alert alert;
        if(email.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR, "Email can't be empty!", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        //regex for email check
        Matcher matcher = Pattern.compile("^(.+)@(.+)$").matcher(email);
        if(!matcher.find()) {
            alert = new Alert(Alert.AlertType.ERROR, "Specified email is not valid! \nMust be of type \"example.adress@example.com\" ", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        if(name.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR, "Name field can't be empty!", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        if(role.equals("LIBRARIAN")) {
            //alert = new Alert(Alert.AlertType.ERROR, "You can't register a librarian account yourself!\nYou need to be logged in as a librarian for that!", ButtonType.OK);
            //alert.showAndWait();
            //return false;
        }
        if(password.length() < 6) {
            alert = new Alert(Alert.AlertType.ERROR, "Password must be at least 6 characters long!", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        if(!passwordSignupField2.getText().equals(password)) {
            alert = new Alert(Alert.AlertType.ERROR, "Passwords do not mach!", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void openPrimaryViewWindow() {
        primaryView = new PrimaryView(currentUser);
        roleSignupSelector.getScene().getWindow().hide();
        primaryView.startWithResources();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //we get the persistence entity master from the static stages master singleton
        entity = StagesMaster.getInstance().getEntity();

        roleSignupSelector.getItems().clear();
        roleSignupSelector.getItems().addAll(userRoles.values());
        roleSignupSelector.getSelectionModel().selectFirst();
    }
}
