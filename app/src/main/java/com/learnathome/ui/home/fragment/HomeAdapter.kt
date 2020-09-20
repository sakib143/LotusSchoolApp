package com.learnathome.ui.home.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.learnathome.data.model.ScheduleModel
import com.learnathome.databinding.AdapterHomeBinding
import com.learnathome.listner.HomeListner

class HomeAdapter (
    private val context: Context,
    private val dataList: List<ScheduleModel.Data>
) : RecyclerView.Adapter<HomeAdapter.BindingViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ViewDataBinding =
            AdapterHomeBinding.inflate(LayoutInflater.from(context), parent, false)
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
