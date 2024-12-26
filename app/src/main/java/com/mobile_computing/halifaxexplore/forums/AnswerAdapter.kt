package com.mobile_computing.halifaxexplore.forums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile_computing.halifaxexplore.R

class AnswerAdapter(private val answers: List<Answer>) :
    RecyclerView.Adapter<AnswerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerTextView: TextView = itemView.findViewById(R.id.answerTextView)
        val signatureTextView: TextView = itemView.findViewById(R.id.signatureTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_answer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answer = answers[position]
        holder.answerTextView.text = answer.answer
        holder.signatureTextView.text = "by ${answer.user} on ${answer.date}"
    }

    override fun getItemCount(): Int {
        return answers.size
    }
}