package com.example.launcher

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appAdapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 4) // 4 columns

        val apps = getInstalledApps()
        appAdapter = AppAdapter(this, apps) { appInfo ->
            launchApp(appInfo)
        }
        recyclerView.adapter = appAdapter
    }

    override fun onResume() {
        super.onResume()
        // Refresh app list when returning to launcher
        val apps = getInstalledApps()
        appAdapter.updateApps(apps)
    }

    /**
     * Queries the package manager for all apps that have a launcher intent.
     */
    private fun getInstalledApps(): List<ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val apps = packageManager.queryIntentActivities(intent, 0)
        return apps.sortedBy { it.loadLabel(packageManager).toString().lowercase() }
    }

    /**
     * Launches the selected app using its package and activity name.
     */
    private fun launchApp(appInfo: ResolveInfo) {
        val packageName = appInfo.activityInfo.packageName
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            Toast.makeText(this, "Unable to launch app", Toast.LENGTH_SHORT).show()
        }
    }
}
