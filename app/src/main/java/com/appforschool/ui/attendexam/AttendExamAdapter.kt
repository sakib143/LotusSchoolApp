package com.appforschool.ui.attendexam

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.data.model.AttendExamModel
import com.appforschool.data.model.ExamModel
import com.appforschool.databinding.AdapterAttendExamBinding
import com.appforschool.databinding.AdapterExamlistBinding
import com.appforschool.listner.AttendExamListner
import com.appforschool.listner.HomeListner
import com.appforschool.ui.home.fragment.exam.ExamAdapter

class AttendExamAdapter(
    private val context: Context,
    private val dataList: List<AttendExamModel.Data>
) : RecyclerView.Adapter<AttendExamAdapter.BindingViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ViewDataBinding =
            AdapterAttendExamBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(
            rootView
        )
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val model = dataList[position]

        holder.itemBinding.setVariable(BR.attendExamModel, model)
        holder.itemBinding.executePendingBindings()
//        Set Item click listner in Adapter
        holder.itemBinding.setVariable(BR.attendExamListner,holder.itemBinding.root.context as AttendExamListner)
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root)

}
