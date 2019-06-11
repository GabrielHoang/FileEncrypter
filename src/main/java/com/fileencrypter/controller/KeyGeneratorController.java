package com.fileencrypter.controller;

import com.fileencrypter.model.Context;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.ResourceBundle;

public class KeyGeneratorController extends BaseController implements Initializable {

    @FXML private Label statusLabel;

    @FXML private PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void generateKeys(ActionEvent event) {
        statusLabel.setText("Keys generated!");
        Context.getInstance().setPassword(passwordField.getText());
    }


}
