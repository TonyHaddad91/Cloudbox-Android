package com.duriana.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.duriana.cloudbox.CloudBox;
import com.duriana.cloudbox.OnSyncFinish;

import java.sql.SQLOutput;

import csds.sds.app.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Using the detfault file path
        final CloudBox cloudBox = CloudBox.getInstance(getApplicationContext(), "http://192.168.0.99:3000/");
        cloudBox.getFileFromServer(getApplicationContext(), "currencies", ".json", new OnSyncFinish() {
            @Override
            public void finish(boolean status) {
                if (status)
                    System.out.println("File Download success");
                else
                    System.out.println("File Download failed ");

            }
        });


        //Read file from the storage
        System.out.println("File Content"+cloudBox.readFile(getApplicationContext(), "currencies"));
        /*
        in order to change the file path you can use
        CloudBox cloudBox= CloudBox.getInstance(getApplicationContext(),domain,filePath);
        */

    }
}
