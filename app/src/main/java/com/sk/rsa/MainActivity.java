package com.sk.rsa;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonEncrypt,buttonDecrypt,buttonChangeText,buttonCheckSignature;
    EditText editTextPublicKey,editTextPrivateKey,editTextDigitalSignature,editTextN,editBeginerText,editText2Signature ;

    TextView textViewEncryptedText;
    String keyFilename="key.txt";
    String SignatureFilename="Signature.txt";
    String textFilename="text.txt";
    String EncryptedFilename="textEncrypted.txt";

    RSA rsa;
    HashFunction hashFunction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textViewEncryptedText=(TextView)findViewById(R.id.textVievEncryptedText);

        editBeginerText=(EditText)findViewById(R.id.editBeginerText);
        editTextPublicKey=(EditText)findViewById(R.id.publicKeyEditText);
        editTextPrivateKey=(EditText)findViewById(R.id.privateKeyEditText);

        editTextN=(EditText)findViewById(R.id.editNText);
        editTextDigitalSignature=(EditText)findViewById(R.id.editTextDigitalSignature);

        editText2Signature=(EditText)findViewById(R.id.editText2Signature);




        buttonChangeText=(Button)findViewById(R.id.ChangeText);
        buttonChangeText.setText(">><<");
        buttonEncrypt=(Button)findViewById(R.id.buttonEncrypt);
        buttonDecrypt=(Button)findViewById(R.id.buttonDecrypt);
        buttonCheckSignature=(Button)findViewById(R.id.buttonCheckSignature);

        buttonChangeText.setOnClickListener(this);
        buttonEncrypt.setOnClickListener(this);
        buttonDecrypt.setOnClickListener(this);
        buttonCheckSignature.setOnClickListener(this);

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }


           String begintext=getStringFromFile(textFilename);
        rsa=new RSA(8);
        String encryptedText=rsa.encrypt(begintext,rsa.getN().intValue(),rsa.getE().intValue());//////////////
        hashFunction=new HashFunction();
        int digitalSignature=hashFunction.compute(encryptedText);
        String encryptedSignature=rsa.encrypt(String.valueOf(digitalSignature),rsa.getN().intValue(),rsa.getE().intValue());//////////////




        editBeginerText.setText(begintext);
        textViewEncryptedText.setText(encryptedText);
        editTextN.setText(rsa.getN().toString());
        editTextDigitalSignature.setText(String.valueOf(digitalSignature));
        editTextPublicKey.setText(rsa.getE().toString());
        editTextPrivateKey.setText(rsa.getD().toString());

        writeToFile(EncryptedFilename,encryptedText);
        writeToFile(keyFilename,String.valueOf(rsa.getD())+"\n"+String.valueOf(rsa.getN()));
        writeToFile(SignatureFilename,String.valueOf(digitalSignature));



        //  text=rsa.decrypt(text);                         ////////
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                } else
                {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private static final int REQUEST_WRITE_STORAGE = 112;


    public void writeToFile(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.write(sBody);//////////////////////////////////////////////////////////
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved to "+ sFileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getStringFromFile(String fileName) {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,"/Notes/" + fileName);



        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
        }
        return text.toString();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonDecrypt:
                String encryptedText;
                if (editBeginerText.getText().equals("")){
                encryptedText=getStringFromFile(EncryptedFilename);
                }
                else encryptedText=editBeginerText.getText().toString();
               String text= rsa.decrypt(encryptedText,Integer.parseInt(editTextN.getText().toString()),Integer.parseInt(editTextPrivateKey.getText().toString()));
                textViewEncryptedText.setText(text);
                editBeginerText.setText(encryptedText);
                String encryptedSignatureq=rsa.encrypt(String.valueOf(editTextDigitalSignature.getText()),rsa.getN().intValue(),rsa.getE().intValue());//////////////

                editText2Signature.setText("");
                editTextDigitalSignature.setText(getStringFromFile(SignatureFilename));
      //          editTextPublicKey.setText("");

                break;
            case R.id.buttonEncrypt:

                String begintext=editBeginerText.getText().toString();

                encryptedText = rsa.encrypt(editBeginerText.getText().toString(), rsa.getN().intValue(), rsa.getE().intValue());
                hashFunction=new HashFunction();
                int digitalSignature=hashFunction.compute(encryptedText);
                String encryptedSignature=rsa.encrypt(String.valueOf(digitalSignature),rsa.getN().intValue(),rsa.getE().intValue());//////////////


                textViewEncryptedText.setText(encryptedText);
                editTextN.setText(rsa.getN().toString());
                editTextDigitalSignature.setText(String.valueOf(digitalSignature));
                editTextPublicKey.setText(rsa.getE().toString());
                editTextPrivateKey.setText(rsa.getD().toString());

                writeToFile(EncryptedFilename,encryptedText);
                writeToFile(keyFilename,String.valueOf(rsa.getD())+"\n"+String.valueOf(rsa.getN()));
                writeToFile(SignatureFilename,String.valueOf(digitalSignature));






                break;
            case R.id.ChangeText:
                editBeginerText.setText(textViewEncryptedText.getText());
                textViewEncryptedText.setText("");
                break;
            case R.id.buttonCheckSignature:
                if(editText2Signature.getText().toString()=="")
               editText2Signature.setText(getStringFromFile(SignatureFilename));

                int Signature2=Integer.parseInt(String.valueOf(editText2Signature.getText()));

                int Signature1=Integer.parseInt(String.valueOf(editTextDigitalSignature.getText()));

                if(Signature1==Signature2)
                    Toast.makeText(this, "identical signatures", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "differ signatures", Toast.LENGTH_SHORT).show();


                break;




        }
    }










}
