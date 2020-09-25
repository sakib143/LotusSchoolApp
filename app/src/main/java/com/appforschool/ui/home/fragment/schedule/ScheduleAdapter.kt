package com.appforschool.ui.home.fragment.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.data.model.ScheduleModel
import com.appforschool.databinding.AdapterScheduleBinding
import com.appforschool.listner.HomeListner

class ScheduleAdapter (
    private val context: Context,
    private val dataList: List<ScheduleModel.Data>
) : RecyclerView.Adapter<ScheduleAdapter.BindingViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ViewDataBinding =
            AdapterScheduleBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(
            rootView
        )
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val product = dataList[position]

        holder.itemBinding.setVariable(BR.scheduleModel, product)
        holder.itemBinding.executePendingBindings()
        //Set Item click listner in Adapte
        holder.itemBinding.setVariable(BR.scheduleClickListner,holder.itemBinding.root.context as HomeListner)
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root)

}
