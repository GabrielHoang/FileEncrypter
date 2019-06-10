package com.fileencrypter.controller;

import com.fileencrypter.model.MessageType;
import com.fileencrypter.model.TransferData;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    void sendMessage(TransferData data, DataOutputStream out) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput objOut = null;

        //convert data into bytes to allow progress bar updates
        try {
            objOut = new ObjectOutputStream(bos);
            objOut.writeObject(data.getLoad());
            objOut.flush();
            byte[] dataBytes = bos.toByteArray();

            int dataSize = dataBytes.length;

            out.writeInt(data.getHeader().getCode());
            out.writeInt(dataSize);
            out.write(dataBytes);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    TransferData receiveMessage(DataInputStream in) {
        int messageCode;
        int messageSize;


        try {
                messageCode = in.readInt();
                messageSize = in.readInt();
                byte[] message = new byte[messageSize];
                in.read(message);

            try {
                TransferData returnData = new TransferData(MessageType.getName(messageCode),deserialize(message));
                return returnData;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class FileInfo implements Serializable{

    long fileSize;
    String fileExtension;
    String fileName;
    String mode;

    public FileInfo(long fileSize, String fileExtension, String fileName, String mode) {
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
        this.fileName = fileName;
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

