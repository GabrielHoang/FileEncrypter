package com.fileencrypter.controller;

import com.fileencrypter.model.Recipent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private ServerSocket serverSocket;

    private Socket clientSocket;

    private PrintWriter out;

    private BufferedReader in;
    //1 thread pool to allow running task in background
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private static final int PORT = 8888;

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
     * Start server when file is chosen
     */
    @FXML
    void chooseFile(ActionEvent event) {
        inputFile = chooseFile();
        if (inputFile != null) {
            inputFileLabel.setText(inputFile.getName());
            serverToggle();
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


    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitForClient() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("CLIENT CONNECTION ESTABLISHED");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



//            String test = in.readLine();
//            if("test".equals(test)) {
//                out.println("test ok");
//                System.out.println("GIT MAJONEZ");
//            } else {
//                out.println("test failed");
//                System.out.println("NIE GIT");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopConnection();
        }
    }

    public void serverToggle() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("SERVER STARTED");
            executor.submit(this::waitForClient);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
