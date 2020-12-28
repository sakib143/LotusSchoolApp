package com.appforschool.ui.home.fragment.exam

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.data.model.ExamModel
import com.appforschool.databinding.AdapterExamlistBinding
import com.appforschool.listner.HomeListner

class ExamAdapter(
    private val context: Context,
    private val dataList: List<ExamModel.Data>
) : RecyclerView.Adapter<ExamAdapter.BindingViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ViewDataBinding =
            AdapterExamlistBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(
            rootView
        )
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val model = dataList[position]

        holder.itemBinding.setVariable(BR.examListModel, model)
        holder.itemBinding.executePendingBindings()
        //Set Item click listner in Adapter
        holder.itemBinding.setVariable(BR.examListListner,holder.itemBinding.root.context as HomeListner)
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root)

}
