package com.appforschool.ui.home.fragment.drive

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.data.model.AlertModel
import com.appforschool.data.model.DriveModel
import com.appforschool.databinding.AdapterAlertListBinding
import com.appforschool.databinding.AdapterDriveBinding
import com.appforschool.listner.HomeListner
import com.appforschool.ui.home.fragment.alert.AlertAdapter

class DriveAdapter (
    private val context: Context,
    private val dataList: List<DriveModel.Data>
) : RecyclerView.Adapter<DriveAdapter.BindingViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView: ViewDataBinding =
            AdapterDriveBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(
            rootView
        )
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val model = dataList[position]

        holder.itemBinding.setVariable(BR.driveModel, model)
        holder.itemBinding.executePendingBindings()
        //Set Item click listner in Adapter
        holder.itemBinding.setVariable(BR.driveListner,holder.itemBinding.root.context as HomeListner)
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root)

}
