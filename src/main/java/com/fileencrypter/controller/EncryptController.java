package com.fileencrypter.controller;

import com.fileencrypter.model.Recipent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class EncryptController extends BaseController implements Initializable {

    @FXML
    private TextField inputFileLabel;

    @FXML
    private TextField outputFileLabel;

    @FXML
    private ChoiceBox<String> encryptionModeChoiceBox;

    @FXML
    private ChoiceBox<Integer> subblockLengthChoiceBox;

    @FXML
    private TableView<Recipent> recipentTable;

    @FXML
    private TableColumn<Recipent, String> recipentColumn;

    @FXML
    private ProgressBar sendProgressBar;

    @FXML
    private Label sendStatusLabel;

    private File inputFile;

    private String outputFileName;

    List<String> encryptionModes = Arrays.asList(
            "ECB","CBC", "CFB", "OFB"
    );
    List<Integer> subblockLengths = Arrays.asList(
            2,4,8,16,32,64,128,256,512,1024
    );
    ObservableList<Recipent> recipents = FXCollections.observableArrayList(
    //DEMO PURPOSE ONLY
    new Recipent("test1"),
    new Recipent("test2"),
    new Recipent("test3"),
    new Recipent("test4")
    );

    /**
     * Initialilze fields with values and settings.
     */
    public void initialize(URL location, ResourceBundle resources) {

        encryptionModeChoiceBox.getItems().addAll(encryptionModes);
        encryptionModeChoiceBox.setValue(encryptionModes.get(0));

        //disable subblock choice box if encryption is not in CFB or OFB mode
        encryptionModeChoiceBox.setOnAction(event -> {
            if(encryptionModeChoiceBox.getSelectionModel().getSelectedItem().equals("CFB")
            || encryptionModeChoiceBox.getSelectionModel().getSelectedItem().equals("OFB")) {
                subblockLengthChoiceBox.setDisable(true);
            } else {
                subblockLengthChoiceBox.setDisable(false);
            }
        });


        subblockLengthChoiceBox.getItems().addAll(subblockLengths);
        subblockLengthChoiceBox.setValue(subblockLengths.get(0));

        recipentTable.setItems(recipents);
        recipentColumn.setCellValueFactory(recipent -> recipent.getValue().getName());


    }

    //TODO: implement method
    @FXML
    void addRecipent(ActionEvent event) {

    }

    /**
     * Show file chooser dialog, capture the file and fill input file label with file name
     */
    @FXML
    void chooseFile(ActionEvent event) {
        inputFile = chooseFile();
        if (inputFile != null) {
            inputFileLabel.setText(inputFile.getName());
        }
    }

    /**
     * Show file name choosing dialog, capture the name and set it in proper fields
     */
    @FXML
    void chooseName(ActionEvent event) {
        outputFileName = chooseOutputFileName();
        if (outputFileName != null) {
            outputFileLabel.setText(outputFileName);
        }
    }

    /**
     * Get selected recipent and delete it from the list
     */
    @FXML
    void deleteRecipent(ActionEvent event) {
        Recipent selectedRecipent = recipentTable.getSelectionModel().getSelectedItem();
        recipents.remove(selectedRecipent);
    }

    //TODO: implement methdod
    @FXML
    void encryptAndSend(ActionEvent event) {

    }


}
