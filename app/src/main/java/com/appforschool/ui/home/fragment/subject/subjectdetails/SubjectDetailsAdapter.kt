package com.appforschool.ui.home.fragment.subject.subjectdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.R
import com.appforschool.data.model.ScheduleModel
import com.appforschool.data.model.SubjectDetailsModel
import com.appforschool.data.model.SubjectModel
import com.appforschool.databinding.AdapterScheduleBinding
import com.appforschool.databinding.AdapterSubjectdetailsBinding
import com.appforschool.listner.HomeListner
import com.appforschool.ui.home.fragment.schedule.ScheduleAdapter
import com.appforschool.ui.home.fragment.subject.SubjectAdapter
import com.appforschool.ui.home.fragment.subject.SubjectFragment

class SubjectDetailsAdapter(
    private val context: Context,
    private val dataList: List<SubjectDetailsModel.Data>
) : RecyclerView.Adapter<SubjectDetailsAdapter.BindingViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ViewDataBinding =
            AdapterSubjectdetailsBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(
            rootView
        )
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val subjectDetails = dataList[position]

        holder.itemBinding.setVariable(BR.subjectDetailsModel, subjectDetails)
        holder.itemBinding.executePendingBindings()
        //Set Item click listner in Adapte
        holder.itemBinding.setVariable(BR.subjectDetailsListner,holder.itemBinding.root.context as HomeListner)
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root)

}
