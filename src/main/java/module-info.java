module com.clientmodule {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens com.clientmodule.views;
    opens com.clientmodule.models;
    opens com.clientmodule.controllers;
    opens com.clientmodule;
}