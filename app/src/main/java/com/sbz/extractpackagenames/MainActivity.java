package com.sbz.extractpackagenames;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listView;
    private TextView text;
    private static final int PERMISSION_REQUEST_CODE = 1;


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
        if (hasUsageStatsPermission()) {
            getAppInfo();
        } else {
            requestUsageStatsPermission();
        }
    }

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void requestUsageStatsPermission() {
        Toast.makeText(this, "Please grant usage access permission for your app.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    public void getAppInfo() {
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