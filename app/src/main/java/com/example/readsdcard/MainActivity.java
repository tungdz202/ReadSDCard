package com.example.readsdcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    ListView listViewDir;
    List<String> dir, name;
    ArrayAdapter<String> adapter;
    String ROOT_DIR = Environment.getExternalStorageDirectory().getPath() + "/";
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewDir = findViewById(R.id.dir);
        dir = new ArrayList<>();
        name = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, 1
            );
        } else {
            getDirectories(ROOT_DIR);
        }

        listViewDir.setOnItemClickListener((adapter, view, i,l ) ->  {
            path= dir.get(i);
            getDirectories(path);
        });
    }

    @Override
    public void onBackPressed() {
        int length = path.length() - 1;
        while (path.charAt(length) != '/') {
            length--;
        }
        path = path.substring(0, length);
        getDirectories(path);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDirectories(ROOT_DIR);
        }
    }

    public void getDirectories(String path){
        dir.clear();
        name.clear();
        if (path != null) {
            Log.v("PATH: ", path);
            File mainFile = new File(path);
            if (mainFile.isFile()) {
                try {
                    Intent intent = new Intent(MainActivity.this, ReadFile.class);
                    String content = new Scanner(mainFile).useDelimiter("\\Z").next();
                    intent.putExtra("name", mainFile.getName());
                    intent.putExtra("content", content);
                    startActivity(intent);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {

                File[] files = mainFile.listFiles();
                assert files != null;
                for (File file: files) {
                    name.add(file.getName());
                    dir.add(file.getAbsolutePath());
                }
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name);
        listViewDir.setAdapter(adapter);
    }
}