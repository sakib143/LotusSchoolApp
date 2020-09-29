package com.appforschool.ui.home.fragment.schedule

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.data.model.ScheduleModel

@BindingAdapter("bindScheduleList")
fun bindRecyclerView(view: RecyclerView, list: List<ScheduleModel.Data>) {
    if (list.isEmpty())
        return
    val layoutManager = view.layoutManager
    if (layoutManager == null)
        view.layoutManager = LinearLayoutManager(view.context)
    var adapter = view.adapter

    if (adapter == null) {
        adapter =
            ScheduleAdapter(
                view.context,
                list
            )
        view.adapter = adapter
    }
}