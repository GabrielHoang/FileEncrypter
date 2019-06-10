package com.fileencrypter.controller;

import com.fileencrypter.model.MessageType;
import com.fileencrypter.model.Recipent;
import com.fileencrypter.model.TransferData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
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

    private SecretKey secretKey;

    // Data streams serve for sending keys
    private DataOutputStream out;

    private DataInputStream in;

    // Buffered streams serve for sending files

    private BufferedOutputStream fileOut;

    private PublicKey clientsPublicKey;
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
        SecretKeySpec secretKey = null;
        try {
            secretKey = new SecretKeySpec(generateKey(16), "AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.secretKey = secretKey;

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

    public String createSessionKey(PublicKey publicKey) throws Exception {
        RSA RSAEncrypter = new RSA();
        String encryptedSessionKey = RSAEncrypter.encrypt(Base64.getEncoder().encodeToString(secretKey.getEncoded()),publicKey);
        return encryptedSessionKey;

        //TODO: send encrypted session key to client
        //OutputStream outputStream = socket.getOutputStream();
        //PrintWriter writer = new PrintWriter(outputStream, true);
        //writer.println(encryptedSessionKey);
        //outputStream.flush();
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
    void encryptAndSend(ActionEvent event) throws Exception {
        System.out.println("sessionKey SERVER: "+Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        Cipher cipher = null;
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        try {
            if(encryptionModeChoiceBox.getValue().equals("ECB")){
                cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            }
            else if(encryptionModeChoiceBox.getValue().equals("CBC")) {
                cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            }
            else if(encryptionModeChoiceBox.getValue().equals("CFB")) {
                cipher = Cipher.getInstance("AES/CFB/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            }
            else if(encryptionModeChoiceBox.getValue().equals("OFB")) {
                cipher = Cipher.getInstance("AES/OFB/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            }
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        String fileExtension = inputFile.getName();
        int i = fileExtension.lastIndexOf('.');
        if (i > 0) {
            fileExtension = fileExtension.substring(i+1);
        }

        FileInputStream in = new FileInputStream(inputFile);
        byte[] input = new byte[(int) inputFile.length()];
        in.read(input);
        in.close();
        byte[] cipheredOutput;
        try {
             cipheredOutput = cipher.doFinal(input);
             FileInfo fileInfo = new FileInfo(cipheredOutput.length,fileExtension,outputFileName,encryptionModeChoiceBox.getSelectionModel().getSelectedItem());
             TransferData fileInfoData = new TransferData(MessageType.FILE,fileInfo);
             sendMessage(fileInfoData,out);
             sendStatusLabel.setText("sendind file metadata");

            //open file transfer stream
            BufferedInputStream fileIn = new BufferedInputStream(new ByteArrayInputStream(cipheredOutput));
//            fileOut = new BufferedOutputStream(clientSocket.getOutputStream());

            byte [] buffer = new byte[4096];
            int readBytes;
            int totalSent = 0;

            while((readBytes = fileIn.read(buffer)) != -1) {
                out.write(buffer,0,readBytes);
                totalSent += readBytes;

                double progress = (double) totalSent / fileInfo.fileSize;
                sendStatusLabel.setText(String.valueOf(progress) + "%");
                Platform.runLater(() -> sendProgressBar.setProgress(progress));
            }
            System.out.println(fileInfo.fileSize);
            sendStatusLabel.setText("sending complete");
        }catch (Exception ext){
            System.out.println(ext.getMessage());
        }



    }

    static byte[] generateKey(int lenght) throws UnsupportedEncodingException {
        byte[] keyBytes = new byte[lenght];
        try {
            long time = System.currentTimeMillis();
            //final ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
            //bb.order(ByteOrder.LITTLE_ENDIAN);
            //bb.putInt(Math.toIntExact(time));
            String timeString = String.valueOf(time);
            byte[] key = timeString.getBytes();
            int len = key.length;

            if (len > keyBytes.length) {
                len = keyBytes.length;
            }

            System.arraycopy(key, 0, keyBytes, 0, len);
        }catch (Exception ext){
            System.out.println(ext.getMessage());
        }
        return keyBytes;
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

            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

            BufferedOutputStream fileOut;

            //read and save client public key
            TransferData clientPublicKeyData = receiveMessage(in);

            if(clientPublicKeyData.getHeader().equals(MessageType.USER_PUBLIC_KEY)) {
                clientsPublicKey = (PublicKey)clientPublicKeyData.getLoad();
                System.out.println("CLIENT PUBLIC KEY RECEIVED");
            }
            //send session key
            try {
                TransferData encryptedSessionKeyData = new TransferData(MessageType.SESSION_KEY,createSessionKey(clientsPublicKey));
                sendMessage(encryptedSessionKeyData,out);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //stopConnection();
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
