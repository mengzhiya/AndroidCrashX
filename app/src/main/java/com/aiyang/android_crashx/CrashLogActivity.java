package com.aiyang.android_crashx;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aiyang.android_crashx.CrashLog.Log;
import com.aiyang.android_crashx.CrashLog.LogAdapter;
import com.aiyang.crashx.util.LogFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CrashLogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LogAdapter adapter;
    Handler fileReadHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crash_log);
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter = new LogAdapter(this,fileReadHandler));

        readFileList();
    }


    private void readFileList() {
        fileReadHandler.post(new Runnable() {
            @Override
            public void run() {
                String dir = LogFile.crashLogDir(getBaseContext());
                if (dir == null) {
                    return;
                }
                File file = new File(dir);
                List<File> fs = Arrays.asList(file.listFiles());

                Collections.sort(fs, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return (int) (o2.lastModified() - o1.lastModified());
                    }
                });

                final List<Log> logs = new ArrayList<>();
                for (File f : fs) {
                    logs.add(new Log(f, f.getName(), null));
                }
                adapter.setFileList(logs);
            }
        });
    }

}