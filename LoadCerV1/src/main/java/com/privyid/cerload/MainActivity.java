package com.privyid.cerload;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button btnLoad;
    TextView txtPath,txtResult;
    EditText editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLoad = (Button)findViewById(R.id.btnLoad);
        txtPath = (TextView)findViewById(R.id.txtPath);
        txtResult = (TextView)findViewById(R.id.txtResult);
        editPassword = (EditText)findViewById(R.id.editPassword);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showgallery();
            }
        });
    }
    public void showgallery() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(11)
                //.withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
                //withFilterDirectories(true) // Set directories filterable (false by default)
                //.withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    private  void loadCer(String path){
        try {
            txtResult.setText("");
            File file = new File(path);
            KeyStore p12 = KeyStore.getInstance("pkcs12");
            p12.load(new FileInputStream(file), editPassword.getText().toString().toCharArray());
            Enumeration e = p12.aliases();
            while (e.hasMoreElements()) {
                String alias = (String) e.nextElement();
                X509Certificate c = (X509Certificate) p12.getCertificate(alias);
                Principal subject = c.getSubjectDN();
                String subjectArray[] = subject.toString().split(",");
                for (String s : subjectArray) {
                    String[] str = s.trim().split("=");
                    String key = str[0];
                    String value = str[1];
                    System.out.println(key + " - " + value);
                    System.out.println("key "+key);
                    Log.e("data",value);
                    txtResult.setText(txtResult.getText()+"\n"+key+" "+value);
                }
            }
        } catch (Exception e) {
            System.out.println("error "+e.getMessage());
            Log.e("error",e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==11 && resultCode!= Activity.RESULT_CANCELED){
            String pathdata = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Log.e("data",pathdata);
            txtPath.setText(pathdata);
            loadCer(pathdata);
        }
    }

}
