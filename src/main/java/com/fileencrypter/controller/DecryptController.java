package com.fileencrypter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class DecryptController extends BaseController implements Initializable {

    @FXML private ProgressBar receiveProgressBar;

    @FXML private Label statusLabel;

    @FXML private TextField receivedFileLabel;

    @FXML private TextField outputFileLabel;

    @FXML private TextField privateKeyLabel;

    @FXML private PasswordField passwordLabel;

    @FXML private ProgressBar decryptProgressBar;

    @FXML private Label decryptStatusLabel;

    private String outputFileName;

    private File privateKey;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Choose private key and set its' label with name
     */
    @FXML
    void chooseFile(ActionEvent event) {
        privateKey = chooseFile();
        if (privateKey != null) {
            privateKeyLabel.setText(privateKey.getName());
        }
    }

    @FXML
    void chooseName(ActionEvent event) {
        outputFileName = chooseOutputFileName();
        if (outputFileName != null) {
            outputFileLabel.setText(outputFileName);
        }
    }

    //TODO: implement method
    @FXML
    void decryptFile(ActionEvent event) {

    }


}
