package com.sbz.extractpackagenames;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
            getUserVisibleApps();
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

    private void getUserVisibleApps() {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ModelClass> apps = new ArrayList<>();

        for (ApplicationInfo applicationInfo : applicationInfoList) {
            // Check if the application has a launcher activity
            launcherIntent.setPackage(applicationInfo.packageName);
            if (packageManager.queryIntentActivities(launcherIntent, 0).size() > 0) {
                String appName = packageManager.getApplicationLabel(applicationInfo).toString();
                String packageName = applicationInfo.packageName;
                apps.add(new ModelClass(appName, packageName));
            }
        }

        AdapterClass adapterClass = new AdapterClass(MainActivity.this, apps);
        listView.setAdapter(adapterClass);

        text.setText(apps.size() + " User-Installed Apps");
    }

}
