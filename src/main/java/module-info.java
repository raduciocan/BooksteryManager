module com.clientmodule {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens com.clientmodule.view;
    opens com.clientmodule.model;
    opens com.clientmodule.controller;
    opens com.clientmodule;
    opens com.clientmodule.queryutils;
}