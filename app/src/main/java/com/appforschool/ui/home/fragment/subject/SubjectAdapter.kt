package com.appforschool.ui.home.fragment.subject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.R
import com.appforschool.data.model.SubjectModel


class SubjectAdapter(
    private val list: List<SubjectModel.Data>,
    private val context: Context,
    private val listner: SubjectFragment.SubjectFragmentListner
) : RecyclerView.Adapter<SubjectAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_subjects, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvSubjectName.text = list.get(position).coursename
        holder.tvFile.setOnClickListener() {
            listner.openSubjectDetails(list.get(position).courseid.toString(),list.get(position).coursename)
        }
    }

    override fun getItemCount() = list.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rlRoom = itemView.findViewById(R.id.rlRoom) as RelativeLayout
        val tvSubjectName = itemView.findViewById(R.id.tvSubjectName) as TextView
        val llRoom = itemView.findViewById(R.id.llRoom) as LinearLayout
        val tvFile = itemView.findViewById(R.id.tvFile) as TextView
    }
}