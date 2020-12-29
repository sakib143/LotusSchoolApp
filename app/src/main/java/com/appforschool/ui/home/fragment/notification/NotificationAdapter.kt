package com.appforschool.ui.home.fragment.notification

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.R
import com.appforschool.data.model.AlertModel
import com.appforschool.utils.hide
import com.appforschool.utils.show
import com.appforschool.utils.toast

class NotificationAdapter(
    private val list: List<AlertModel.Data>
) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_alert_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var totalLineCount = 0
        holder.tvDate.setText(list.get(position).notidate)
        holder.tvTime.setText(list.get(position).notitime)
        holder.tvDescription.setText(list.get(position).notificationDesc)

        //It will open web browser when text will have any URLs
        Linkify.addLinks(holder.tvDescription, Linkify.WEB_URLS);

        //Getting textview length
        holder.tvDescription.post(Runnable {
            totalLineCount = holder.tvDescription.lineCount
        })

        if( totalLineCount == 1) {
            holder.tvMore.hide()
        } else {
            holder.tvMore.show()
        }

        holder.tvMore.setOnClickListener() {
            holder.tvDescription.setText(list.get(position).notificationDesc)
            //It will open web browser when text will have any URLs
            Linkify.addLinks(holder.tvDescription, Linkify.WEB_URLS);

            if (holder.tvMore.text.toString().equals(holder.tvMore.context.resources.getString(R.string.less), ignoreCase = true)) {
                holder.tvDescription.maxLines = 1
                holder.tvMore.setText(holder.tvMore.context.resources.getString(R.string.view_more))
            } else {
                holder.tvDescription.maxLines = 8
                holder.tvMore.setText(holder.tvMore.context.resources.getString(R.string.less))
            }
        }
        
        holder.llNotificationRoot.setOnLongClickListener() {
            holder.llNotificationRoot.context.toast(holder.llNotificationRoot.context.getString(R.string.copied_to_clipboard))
            val clipBoard = holder.llNotificationRoot.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("label", list.get(position).notificationDesc)
            clipBoard.setPrimaryClip(clipData)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount() = list.size

    //getItemId and getItemId used to fix data repeating related issue
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate = itemView.findViewById(R.id.tvDate) as TextView
        val tvTime = itemView.findViewById(R.id.tvTime) as TextView
        val tvDescription = itemView.findViewById(R.id.tvDescription) as TextView
        val tvMore = itemView.findViewById(R.id.tvMore) as TextView
        val llNotificationRoot = itemView.findViewById(R.id.llNotificationRoot) as LinearLayout
    }

}