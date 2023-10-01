package com.sbz.extractpackagenames;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listView;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        text = findViewById(R.id.totalapp);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.hasFixedSize();
    }

    public void getallapps(View view) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0);

        List<ModelClass> list = new ArrayList<>();
        for (ResolveInfo ri : ril) {
            if (ri.activityInfo != null) {
                String appName = ri.activityInfo.loadLabel(getPackageManager()).toString();
                String packageName = ri.activityInfo.packageName;
                list.add(new ModelClass(appName, packageName));
            }
        }
        AdapterClass adapterClass = new AdapterClass(MainActivity.this, list);
        listView.setAdapter(adapterClass);

        text.setText(ril.size() + " Apps are installed");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}