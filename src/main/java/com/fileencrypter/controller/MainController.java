package com.fileencrypter.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TabPane mainTabPane;

    @FXML private Tab decryptTab;

    @FXML private Tab encryptTab;

    @FXML private Tab keyGeneratorTab;

    @FXML private DecryptController decryptController;

    @FXML private EncryptController encryptController;

    @FXML private KeyGeneratorController keyGeneratorController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        decryptTab.setOnSelectionChanged(event -> {
            if(decryptTab.isSelected()) {
                decryptController.clientToggle();
            }
        });
    }
}
