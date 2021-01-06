package com.appforschool.ui.attendexam

import android.annotation.SuppressLint
import android.content.Context
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
    private val list: List<AttendExamModel.Data>,
    private val isFromViewAnswer: Boolean
) : RecyclerView.Adapter<AttendExamAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_attend_exam, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        setText(holder, position)
        setCheckboxchecked(position, holder)
        setViewVisibility(position, holder)
        // isFromViewAnswer == true then we will disable checkbox and all listners.
        if (!isFromViewAnswer) {
            setListner(position, holder)
        } else {
            holder.cbOne.isClickable = false
            holder.cbTwo.isClickable = false
            holder.cbThree.isClickable = false
            holder.cbFour.isClickable = false
            holder.edtAnswer.isEnabled = false
            holder.llUploadFile.hide()
            setRightWrongAnswers(position, holder)
        }
    }

    private fun setText(holder: MyViewHolder, position: Int) {
        holder.tvNumber.text = list.get(position).questionOrder.toString()
        if (isFromViewAnswer) {
            holder.tvQuestionDescription.text =
                list.get(position).questionDesc + "  (Mark: " + list.get(position).ObtainedMarks + "/" + list.get(
                    position
                ).marks.toString() + ")"
        } else {
            holder.tvQuestionDescription.text =
                list.get(position).questionDesc + "  (Mark(s): " + list.get(position).marks.toString() + ")"
        }
        holder.cbFour.text = list.get(position).optionD
        holder.cbThree.text = list.get(position).optionC
        holder.cbTwo.text = list.get(position).optionB
        holder.cbOne.text = list.get(position).optionA

        if (!list.get(position).subjectiveanswer.isNullOrEmpty()) {
            holder.edtAnswer.setText(list.get(position).subjectiveanswer)
        }
    }

    private fun setCheckboxchecked(position: Int, holder: MyViewHolder) {
        holder.cbOne.isChecked = list.get(position).isACorrect
        holder.cbTwo.isChecked = list.get(position).isBCorrect
        holder.cbThree.isChecked = list.get(position).isCCorrect
        holder.cbFour.isChecked = list.get(position).isDCorrect
    }

    private fun setRightWrongAnswers(
        position: Int,
        holder: MyViewHolder
    ) {
        if (isFromViewAnswer) {
            if (list.get(position).isAcutalAnsA) {
                //Set correct Answers
                holder.tvACorrect.setBackgroundResource(R.drawable.ic_correct_ans)
                //Set wrong Answers
                if (list.get(position).isBCorrect && !list.get(position).isAcutalAnsB) {
                    holder.tvBInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                } else if (list.get(position).isCCorrect && !list.get(position).isAcutalAnsC) {
                    holder.tvCInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                } else if (list.get(position).isDCorrect && !list.get(position).isAcutalAnsD) {
                    holder.tvDInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                }
            } else if ((list.get(position).isAcutalAnsB)) {
                //Set correct Answers
                holder.tvBCorrect.setBackgroundResource(R.drawable.ic_correct_ans)
                //Set wrong Answers
                if (list.get(position).isACorrect && !list.get(position).isAcutalAnsA) {
                    holder.tvAInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                } else if (list.get(position).isCCorrect && !list.get(position).isAcutalAnsC) {
                    holder.tvCInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                } else if (list.get(position).isDCorrect && !list.get(position).isAcutalAnsD) {
                    holder.tvDInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                }
            } else if ((list.get(position).isAcutalAnsC)) {
                //Set correct Answers
                holder.tvCCorrect.setBackgroundResource(R.drawable.ic_correct_ans)
                //Set wrong Answers
                if (list.get(position).isACorrect && !list.get(position).isAcutalAnsA) {
                    holder.tvAInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                } else if (list.get(position).isBCorrect && !list.get(position).isAcutalAnsB) {
                    holder.tvBInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                } else if (list.get(position).isDCorrect && !list.get(position).isAcutalAnsD) {
                    holder.tvDInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                }
            } else if ((list.get(position).isAcutalAnsD)) {
                //Set correct Answers
                holder.tvDCorrect.setBackgroundResource(R.drawable.ic_correct_ans)
                //Set wrong Answers
                if (list.get(position).isACorrect && !list.get(position).isAcutalAnsA) {
                    holder.tvAInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                } else if (list.get(position).isBCorrect && !list.get(position).isAcutalAnsB) {
                    holder.tvBInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                } else if (list.get(position).isCCorrect && !list.get(position).isAcutalAnsC) {
                    holder.tvCInCorrect.setBackgroundResource(R.drawable.ic_wrong_ans)
                }
            }
        }
    }

    private fun setListner(position: Int, holder: MyViewHolder) {
        holder.llUploadFile.setOnClickListener() {
            (context as AttendExamActivity).openFile(list.get(position).srNo.toString(), position)
        }

        holder.ivImage.setOnClickListener() {
            (context as AttendExamActivity).zoomImage(
                list.get(position).questionimagefullpath,
                holder.ivImage
            )
        }

        holder.cbOne.setOnClickListener() {
            (context as AttendExamActivity).optionAClicked(
                position,
                list.get(position).srNo.toString()
            )
        }

        holder.cbTwo.setOnClickListener() {
            (context as AttendExamActivity).optionBClicked(
                position,
                list.get(position).srNo.toString()
            )
        }

        holder.cbThree.setOnClickListener() {
            (context as AttendExamActivity).optionCClicked(
                position,
                list.get(position).srNo.toString()
            )
        }

        holder.cbFour.setOnClickListener() {
            (context as AttendExamActivity).optionDClicked(
                position,
                list.get(position).srNo.toString()
            )
        }

        var timer = Timer()
        val DELAY: Long = 500L

        holder.edtAnswer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().equals("", ignoreCase = true)) {
                    (context as AttendExamActivity).updateEditeTextAnswer(
                        position,
                        list.get(position).srNo.toString(),
                        s.toString()
                    )
                } else {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            (context as AttendExamActivity).updateEditeTextAnswer(
                                position,
                                list.get(position).srNo.toString(),
                                s.toString()
                            )
                        }
                    }, DELAY)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setViewVisibility(position: Int, holder: MyViewHolder) {
        //If answer is subjective then hide checkbox and show edittext
        if (list.get(position).qType.equals("S", ignoreCase = true)) {
            holder.llCheckbox.hide()
            holder.edtAnswer.show()
            holder.llUploadFile.show()
        } else {
            holder.llCheckbox.show()
            holder.edtAnswer.hide()
            holder.llUploadFile.hide()
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

    //getItemId and getItemId used to fix data repeating related issue
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

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
        val llUploadFile = itemView.findViewById(R.id.llUploadFile) as LinearLayout
        val tvACorrect = itemView.findViewById(R.id.tvACorrect) as TextView
        val tvAInCorrect = itemView.findViewById(R.id.tvAInCorrect) as TextView
        val tvBCorrect = itemView.findViewById(R.id.tvBCorrect) as TextView
        val tvBInCorrect = itemView.findViewById(R.id.tvBInCorrect) as TextView
        val tvCCorrect = itemView.findViewById(R.id.tvCCorrect) as TextView
        val tvCInCorrect = itemView.findViewById(R.id.tvCInCorrect) as TextView
        val tvDCorrect = itemView.findViewById(R.id.tvDCorrect) as TextView
        val tvDInCorrect = itemView.findViewById(R.id.tvDInCorrect) as TextView
    }

}