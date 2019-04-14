package com.fileencrypter.controller;

import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public abstract class BaseController {

    /**
     * Show dialog with text input for desired output file name.
     */
    static String chooseOutputFileName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Output file name");
        dialog.setHeaderText("How would you like to name output file?");
        dialog.setContentText("Enter output file desired name:");

        Optional<String> result = dialog.showAndWait();

        if (!result.isPresent()) return null;
        return result.get();
    }

    /**
     * Show file chooser dialog
     * @return chosen file
     */
    static File chooseFile() {
        Stage fileChooserStage = new Stage();
        fileChooserStage.centerOnScreen();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file to encrypt");
        return fileChooser.showOpenDialog(fileChooserStage);
    }


}
