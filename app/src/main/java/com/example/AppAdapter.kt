package com.example.launcher

import android.content.Context
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppAdapter(
    private val context: Context,
    private var apps: List<ResolveInfo>,
    private val onAppClick: (ResolveInfo) -> Unit
) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        val appName: TextView = itemView.findViewById(R.id.appName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val appInfo = apps[position]
        holder.appName.text = appInfo.loadLabel(context.packageManager).toString()
        holder.appIcon.setImageDrawable(appInfo.loadIcon(context.packageManager))
        holder.itemView.setOnClickListener { onAppClick(appInfo) }
    }

    override fun getItemCount(): Int = apps.size

    fun updateApps(newApps: List<ResolveInfo>) {
        apps = newApps
        notifyDataSetChanged()
    }
}
