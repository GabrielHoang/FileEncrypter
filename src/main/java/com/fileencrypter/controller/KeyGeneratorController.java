package com.fileencrypter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class KeyGeneratorController extends BaseController implements Initializable {

    @FXML
    private TextField publicKeyLabel;

    @FXML
    private TextField privateKeyLabel;

    @FXML
    private TextField userTextField;

    @FXML
    private PasswordField passwordField;

    private File publicKey;

    private File privateKey;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void choosePublicKey(ActionEvent event) {
        publicKey = chooseFile();
        if (publicKey != null) {
            publicKeyLabel.setText(publicKey.getName());
        }
    }

    @FXML
    void choosePrivateKey(ActionEvent event) {
        privateKey = chooseFile();
        if (privateKey != null) {
            privateKeyLabel.setText(privateKey.getName());
        }
    }

    @FXML
    void generateKeys(ActionEvent event) {

    }


}
