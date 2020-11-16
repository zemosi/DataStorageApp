package com.example.datastorageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String FILENAME = "filename.txt";
    public static final String PREFNAME = "com.example.datastorageapp.PREF";
    private TextView textRead;
    private EditText editText;

    private static final String[] PERMISSIONS={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textRead=findViewById(R.id.text_read);
        editText=findViewById(R.id.edit_text);
    }

    private static boolean hasPermission(Context context, String... permissions){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && permissions!=null){
            for (String permission:permissions){
                if (ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }

        return true;
    }

    public void save(View view) {
        saveFileES();
    }

    public void read(View view) {
        readFileES();
    }

    public void delete(View view) {
        deleteFileES();
    }

    //External Storage
    public void saveFileES(){
        if (hasPermission(this, PERMISSIONS)){
            String textFile=editText.getText().toString();
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path.toString(),FILENAME);
            FileOutputStream outputStream=null;

            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file, false);
                outputStream.write(textFile.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS,REQUEST_CODE);
        }
    }

    public void readFileES(){
        File path = Environment.getExternalStorageDirectory();
        File file = new File(path.toString(),FILENAME);
        if (file.exists()){
            StringBuilder text=new StringBuilder();

            try {
                BufferedReader br =new BufferedReader(new FileReader(file));
                String line=br.readLine();
                while (line!=null){
                    text.append(line);
                    line=br.readLine();
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            textRead.setText(text.toString());
        } else {
            textRead.setText("");
        }
    }

    public void deleteFileES() {
        File path = Environment.getExternalStorageDirectory();
        File file = new File(path.toString(),FILENAME);
        if (file.exists()){
            file.delete();
        }
    }

    //Internal Storage
    public void saveFileIS(){
        String textFile=editText.getText().toString();
        File path=getDir("NEWFOLDER",MODE_PRIVATE);
        File file = new File(path.toString(),FILENAME);
        FileOutputStream outputStream=null;

        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, false);
            outputStream.write(textFile.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readFileIS(){
        File path=getDir("NEWFOLDER",MODE_PRIVATE);
        File file = new File(path.toString(),FILENAME);
        if (file.exists()){
            StringBuilder text=new StringBuilder();

            try {
                BufferedReader br =new BufferedReader(new FileReader(file));
                String line=br.readLine();
                while (line!=null){
                    text.append(line);
                    line=br.readLine();
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            textRead.setText(text.toString());
        } else {
            textRead.setText("");
        }
    }

    public void deleteFileIS() {
        File path=getDir("NEWFOLDER",MODE_PRIVATE);
        File file = new File(path.toString(),FILENAME);
        if (file.exists()){
            file.delete();
        }
    }

    //SharedPreference
    public void saveFileSP(){
        String textFile=editText.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FILENAME,textFile);
        editor.commit();
    }

    public void readFileSP(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        if (sharedPreferences.contains(FILENAME)){
            String mytext=sharedPreferences.getString(FILENAME,"");
            textRead.setText(mytext);
        } else {
            textRead.setText("");
        }
    }

    public void deleteFileSP(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}