package com.appforschool.ui.home.fragment.bindrecyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.ScheduleModel
import com.appforschool.data.model.SubjectDetailsModel
import com.appforschool.ui.home.fragment.assignment.AssignmentAdapter
import com.appforschool.ui.home.fragment.schedule.ScheduleAdapter
import com.appforschool.ui.home.fragment.subject.subjectdetails.SubjectDetailsAdapter

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

@BindingAdapter("bindSubjectDetial")
fun bindSubjectDetial(view: RecyclerView, list: List<SubjectDetailsModel.Data>) {
    if (list.isEmpty())
        return
    val layoutManager = view.layoutManager
    if (layoutManager == null)
        view.layoutManager = LinearLayoutManager(view.context)
    var adapter = view.adapter

    if (adapter == null) {
        adapter =
            SubjectDetailsAdapter(
                view.context,
                list
            )
        view.adapter = adapter
    }
}

@BindingAdapter("bindAssignment")
fun bindAssignment(view: RecyclerView, list: List<AssignmentModel.Data>) {
    if (list.isEmpty())
        return
    val layoutManager = view.layoutManager
    if (layoutManager == null)
        view.layoutManager = LinearLayoutManager(view.context)
    var adapter = view.adapter

    if (adapter == null) {
        adapter =
            AssignmentAdapter(
                view.context,
                list
            )
        view.adapter = adapter
    }
}