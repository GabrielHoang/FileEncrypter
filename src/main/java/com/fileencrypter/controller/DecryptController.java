package com.fileencrypter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
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

    private File privateKeyFile;

    private Socket clientSocket;

    private PrintWriter out;

    private BufferedReader in;

    private String mode;

    private SecretKey secretKey;

    private PrivateKey privateKey;

    private File inputFile;

    private static final int PORT = 8888;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Choose private key and set its' label with name
     */
    @FXML
    void chooseFile(ActionEvent event) {
        privateKeyFile = chooseFile();
        if (privateKeyFile != null) {
            privateKeyLabel.setText(privateKeyFile.getName());
        }
    }

    @FXML
    void chooseName(ActionEvent event) {
        outputFileName = chooseOutputFileName();
        if (outputFileName != null) {
            outputFileLabel.setText(outputFileName);
        }
    }

    void createAndSaveKeyPairRSA() throws Exception {
        RSA RSAEncrypter = new RSA();
        KeyPair keyPair = RSAEncrypter.generateKeyPair(512);
        byte[] encodedPublicKey = keyPair.getPublic().getEncoded();
        String b64PublicKey = Base64.getEncoder().encodeToString(encodedPublicKey);
        //TODO: save public key to file
        //File file = new File("C:\\Users\\Win10\\IdeaProjects\\AES\\publicKeys.txt");
        //FileOutputStream fop = new FileOutputStream(file);
        //fop.write( b64PublicKey.getBytes() );
        //fop.close();


    }

    public void decryptSessionKey(PrivateKey privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        //TODO: Receiving encrypted session key from server
        //encryptedSessionKey

        //TODO: to uncomment after encrypted session key is implemented
        //RSA decrypter = new RSA();
        //String keySession = decrypter.decrypt(encryptedSessionKey.toString(),privateKey);
        //byte[] decodedSessionKey = Base64.getDecoder().decode(keySession.trim());
        //this.secretKey = new SecretKeySpec(decodedSessionKey, 0,
        //       decodedSessionKey.length, "AES");
    }

    public void encryptPrivateKey(PrivateKey privateKey, String password) throws Exception {
        String keyString = getHashSHA_256(password); //password of user

        byte[] keyBytes = new byte[16];
        try {
            byte[] key = keyString.getBytes();
            int len = key.length;

            if (len > keyBytes.length) {
                len = keyBytes.length;
            }

            System.arraycopy(key, 0, keyBytes, 0, len);
        }catch (Exception ext){
            System.out.println(ext.getMessage());
        }

        SecretKeySpec key = new SecretKeySpec(keyBytes,"AES");
        this.encryptCBC(privateKey,key,"new vector".getBytes());
    }

    public void decryptPrivateKey(String password) throws Exception {
        String keyString = getHashSHA_256(password); //password of user

        byte[] keyBytes = new byte[16];
        try {
            byte[] key = keyString.getBytes();
            int len = key.length;

            if (len > keyBytes.length) {
                len = keyBytes.length;
            }

            System.arraycopy(key, 0, keyBytes, 0, len);
        }catch (Exception ext){
            System.out.println(ext.getMessage());
        }

        SecretKeySpec key = new SecretKeySpec(keyBytes,"AES");
        this.decryptCBC("new vector".getBytes(),privateKeyFile);
    }

    public void encryptCBC(PrivateKey privateKey,SecretKeySpec key,byte[] vector) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytesIv = generateVector(vector, 16);
        IvParameterSpec ivspec = new IvParameterSpec(keyBytesIv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
        byte[] encodedPublicKey = privateKey.getEncoded();
        String b64PublicKey = Base64.getEncoder().encodeToString(encodedPublicKey);
        //Perform Encryption
        byte[] cipherText = cipher.doFinal(b64PublicKey.getBytes("UTF-8"));
        String cipher_text = Base64.getEncoder().encodeToString(cipherText);

        //TODO: save encrypted
        //File targetFile = new File("C:\\Users\\Win10\\IdeaProjects\\AES\\privateKeySHA256.txt");
        //FileOutputStream outStream = new FileOutputStream(targetFile);
        //outStream.write(cipher_text.getBytes());
        //outStream.flush();
        //outStream.close();
    }

    public void decryptCBC(byte[] vector, File privateKey) throws Exception{
        byte[] keyBytesIv = generateVector(vector, 16);
        IvParameterSpec ivspec = new IvParameterSpec(keyBytesIv);

        InputStream is2 = new FileInputStream(privateKeyFile.getPath());
        BufferedReader buf2 = new BufferedReader(new InputStreamReader(is2,"UTF-8"));
        String line2 = buf2.readLine();
        StringBuilder sb2 = new StringBuilder();
        while(line2 != null){
            sb2.append(line2).append("\n");
            line2 = buf2.readLine();
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, this.secretKey, ivspec);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int r = is2.read(buffer);
            if (r == -1) break;
            out.write(buffer, 0, r);
        }

        byte[] ret = out.toByteArray();

        byte[] output = null;
        try {
            byte[] b64PrivateKey = Base64.getDecoder().decode(sb2.toString().trim());
            output = cipher.doFinal(b64PrivateKey); //decrypted private key
        }catch (Exception exc){
            System.out.println(exc.getMessage());
        }

        String privateKey_text = new String(output);
        byte[] byteKey = Base64.getDecoder().decode(privateKey_text.trim());
        PKCS8EncodedKeySpec PKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        PrivateKey privateKeyRSA = kf.generatePrivate(PKCS8EncodedKeySpec);
        this.privateKey = privateKeyRSA; //decoded private key
    }

    static byte[] generateVector(byte[] vector, int lenght) throws UnsupportedEncodingException {
        byte[] keyBytesIv = new byte[lenght];
        int len = vector.length;

        if (len > keyBytesIv.length) {
            len = keyBytesIv.length;
        }

        System.arraycopy(vector, 0, keyBytesIv, 0, len);
        return keyBytesIv;
    }

    //TODO: implement method
    @FXML
    void decryptFile(ActionEvent event) throws java.lang.Exception {
        Cipher cipher = null;
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        try {
            if(mode.equals("ECB")){
                cipher = Cipher.getInstance("AES/ECB/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
            }
            else if(mode.equals("CBC")) {
                cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, ivspec);
            }
            else if(mode.equals("CFB")) {
                cipher = Cipher.getInstance("AES/CFB/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, ivspec);
            }
            else if(mode.equals("OFB")) {
                cipher = Cipher.getInstance("AES/OFB/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, ivspec);
            }
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        FileInputStream in = new FileInputStream(inputFile);
        byte[] input = new byte[(int) inputFile.length()];
        in.read(input);

        FileOutputStream out = new FileOutputStream(inputFile);
        byte[] output = cipher.doFinal(input);
    }

    String sendMessage(String msg) {
        String response = null;
        try {
            out.println(msg);
            response = in.readLine();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }

    void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clientToggle() {
        try {
            System.out.println("CLIENT TOGGLE");
            clientSocket = new Socket("127.0.0.1", PORT);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



            sendMessage("test");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopConnection();
        }
    }

    public String getHashSHA_256(String password) throws Exception{
        String hash = null;
        byte[] passwordBytes = password.getBytes();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte sha256[] = md.digest(passwordBytes);
        sha256 = Arrays.copyOf(sha256, 16);
        String hashString = bytesToHex(sha256);
        return  hashString;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
