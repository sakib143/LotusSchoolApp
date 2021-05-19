package com.appforschool.ui.home.fragment.bindrecyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.data.model.*
import com.appforschool.ui.home.fragment.assignment.AssignmentAdapter
import com.appforschool.ui.home.fragment.drive.DriveAdapter
import com.appforschool.ui.home.fragment.exam.ExamAdapter
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

//@BindingAdapter("bindAlert")
//fun bindAlert(view: RecyclerView, list: List<AlertModel.Data>) {
//    if (list.isEmpty())
//        return
//    val layoutManager = view.layoutManager
//    if (layoutManager == null)
//        view.layoutManager = LinearLayoutManager(view.context)
//    var adapter = view.adapter
//
//    if (adapter == null) {
//        adapter =
//            AlertAdapter(
//                view.context,
//                list
//            )
//        view.adapter = adapter
//    }
//}

@BindingAdapter("bindAExamList")
fun bindAExamList(view: RecyclerView, list: List<ExamModel.Data>) {
    if (list.isEmpty())
        return
    val layoutManager = view.layoutManager
    if (layoutManager == null)
        view.layoutManager = LinearLayoutManager(view.context)
    var adapter = view.adapter
    adapter =
        ExamAdapter(
            view.context,
            list
        )
    view.adapter = adapter
}

@BindingAdapter("bindDriveList")
fun bindDriveList(view: RecyclerView, list: List<DriveModel.Data>) {
    if (list.isEmpty())
        return
    val layoutManager = view.layoutManager
    if (layoutManager == null)
        view.layoutManager = LinearLayoutManager(view.context)
    var adapter = view.adapter
        adapter = DriveAdapter(view.context, list)
        view.adapter = adapter
}

//@BindingAdapter("bindAttendExam")
//fun bindAttendExam(view: RecyclerView, list: List<AttendExamModel.Data>) {
//    if (list.isEmpty())
//        return
//    val layoutManager = view.layoutManager
//    if (layoutManager == null)
//        view.layoutManager = LinearLayoutManager(view.context)
//    var adapter = view.adapter
//
//    if (adapter == null) {
//        adapter =
//            AttendExamAdapter(
//                view.context,
//                list
//            )
//        view.adapter = adapter
//    }
//}
