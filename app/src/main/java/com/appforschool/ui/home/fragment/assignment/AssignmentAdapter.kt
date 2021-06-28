package com.appforschool.ui.home.fragment.assignment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.data.model.AssignmentModel
import com.appforschool.databinding.AdapterAssignmentListBinding
import com.appforschool.listner.HomeListner

class AssignmentAdapter (
    private val context: Context,
    private var dataList: MutableList<AssignmentModel.Data>
) : RecyclerView.Adapter<AssignmentAdapter.BindingViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ViewDataBinding =
            AdapterAssignmentListBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val subjectDetails = dataList[position]
        holder.itemBinding.setVariable(BR.assignmentModel, subjectDetails)
        holder.itemBinding.executePendingBindings()
        //Set Item click listner in Adapte
        holder.itemBinding.setVariable(BR.assignmentListner,holder.itemBinding.root.context as HomeListner)
    }

    fun updateList(list: List<AssignmentModel.Data>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root)
}
