package com.fileencrypter.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController {

    @FXML private TabPane mainTabPane;

    @FXML private Tab decryptTab;

    @FXML private Tab encryptTab;

    @FXML private Tab keyGeneratorTab;

    @FXML private DecryptController decryptController;

    @FXML private EncryptController encryptController;

    @FXML private KeyGeneratorController keyGeneratorController;

}
