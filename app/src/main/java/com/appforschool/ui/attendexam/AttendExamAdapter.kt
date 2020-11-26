package com.appforschool.ui.attendexam

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.appforschool.R
import com.appforschool.data.model.AttendExamModel
import com.appforschool.utils.LogM
import com.appforschool.utils.hide
import com.appforschool.utils.show
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import java.util.*

class AttendExamAdapter(
    private val context: Context,
    private val list: List<AttendExamModel.Data>
) : RecyclerView.Adapter<AttendExamAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_attend_exam, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        setText(holder, position)
        setCheckboxchecked(position, holder)
        setViewVisibility(position, holder)
        setListner(position, holder)
    }

    private fun setText(holder: MyViewHolder, position: Int) {
        holder.tvNumber.text = list.get(position).questionOrder.toString()
        holder.tvQuestionDescription.text =
            list.get(position).questionDesc + "  (Mark(s): " + list.get(position).marks.toString() + ")"
        holder.cbFour.text = list.get(position).optionD
        holder.cbThree.text = list.get(position).optionC
        holder.cbTwo.text = list.get(position).optionB
        holder.cbOne.text = list.get(position).optionA

        if( ! list.get(position).subjectiveanswer.isNullOrEmpty()) {
            holder.edtAnswer.setText(list.get(position).subjectiveanswer)
        }
    }

    private fun setCheckboxchecked(position: Int, holder: MyViewHolder) {
        holder.cbOne.isChecked = list.get(position).isACorrect
        holder.cbTwo.isChecked = list.get(position).isBCorrect
        holder.cbThree.isChecked = list.get(position).isCCorrect
        holder.cbFour.isChecked = list.get(position).isDCorrect
    }

    private fun setListner(position: Int, holder: MyViewHolder) {
        holder.cbOne.setOnClickListener() {
            (context as AttendExamActivity).optionAClicked(position,list.get(position).srNo.toString())
        }

        holder.cbTwo.setOnClickListener() {
            (context as AttendExamActivity).optionBClicked(position,list.get(position).srNo.toString())
        }

        holder.cbThree.setOnClickListener() {
            (context as AttendExamActivity).optionCClicked(position,list.get(position).srNo.toString())
        }

        holder.cbFour.setOnClickListener() {
            (context as AttendExamActivity).optionDClicked(position,list.get(position).srNo.toString())
        }

        var timer = Timer()
        val DELAY: Long = 1200L

        holder.edtAnswer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        (context as AttendExamActivity).updateEditeTextAnswer(position,list.get(position).srNo.toString(), s.toString())
                    }
                }, DELAY)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setViewVisibility(position: Int, holder: MyViewHolder) {
        if (list.get(position).qType.equals("S", ignoreCase = true)) {
            holder.llCheckbox.hide()
            holder.edtAnswer.show()
        } else {
            holder.llCheckbox.show()
            holder.edtAnswer.hide()
        }

        if (list.get(position).questionimagefullpath.isNullOrEmpty()) {
            holder.ivImage.hide()
        } else {
            holder.ivImage.show()
            Glide.with(context).load(list.get(position).questionimagefullpath)
                .apply(
                    RequestOptions()
                        .priority(Priority.HIGH)
                        .error(R.mipmap.ic_launcher)
                )
                .into(holder.ivImage)
        }
    }

    override fun getItemCount() = list.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber = itemView.findViewById(R.id.tvNumber) as TextView
        val tvQuestionDescription = itemView.findViewById(R.id.tvQuestionDescription) as TextView
        val cbFour = itemView.findViewById(R.id.cbFour) as CheckBox
        val cbThree = itemView.findViewById(R.id.cbThree) as CheckBox
        val cbTwo = itemView.findViewById(R.id.cbTwo) as CheckBox
        val cbOne = itemView.findViewById(R.id.cbOne) as CheckBox
        val llCheckbox = itemView.findViewById(R.id.llCheckbox) as LinearLayout
        val edtAnswer = itemView.findViewById(R.id.edtAnswer) as EditText
        val ivImage = itemView.findViewById(R.id.ivImage) as ImageView
    }

}