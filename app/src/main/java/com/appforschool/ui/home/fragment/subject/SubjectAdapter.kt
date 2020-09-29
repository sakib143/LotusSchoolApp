package com.appforschool.ui.home.fragment.subject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
//        if (position % 2 == 1) {
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT)
//            params.setMargins(0, 20, 0, 40)
//            holder.llSubject.setLayoutParams(params)
//        } else {
//
//        }
    }

    override fun getItemCount() = list.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llSubject = itemView.findViewById(R.id.llSubject) as LinearLayout
//        val rvScheduleChild  = itemView.findViewById(R.id.rvScheduleChild) as RecyclerView
    }
}