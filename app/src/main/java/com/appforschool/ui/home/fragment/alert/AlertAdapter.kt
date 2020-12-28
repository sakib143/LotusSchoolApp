//package com.appforschool.ui.home.fragment.alert
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.ViewDataBinding
//import androidx.databinding.library.baseAdapters.BR
//import androidx.recyclerview.widget.RecyclerView
//import com.appforschool.data.model.AlertModel
//import com.appforschool.data.model.AssignmentModel
//import com.appforschool.databinding.AdapterAlertListBinding
//import com.appforschool.databinding.AdapterAssignmentListBinding
//import com.appforschool.listner.HomeListner
//import com.appforschool.ui.home.fragment.assignment.AssignmentAdapter
//
//class AlertAdapter(
//    private val context: Context,
//    private val dataList: List<AlertModel.Data>
//) : RecyclerView.Adapter<AlertAdapter.BindingViewHolder>() {
//
//    override fun getItemCount() = dataList.size
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
//        val rootView: ViewDataBinding =
//            AdapterAlertListBinding.inflate(LayoutInflater.from(context), parent, false)
//        return BindingViewHolder(
//            rootView
//        )
//    }
//
//    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
//        val model = dataList[position]
//
//        holder.itemBinding.setVariable(BR.alertModel, model)
//        holder.itemBinding.executePendingBindings()
//        //Set Item click listner in Adapter
//        holder.itemBinding.setVariable(BR.alertListner,holder.itemBinding.root.context as HomeListner)
//    }
//
//    class BindingViewHolder(val itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root)
//
//}
