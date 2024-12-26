package com.mobile_computing.halifaxexplore.forums

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobile_computing.halifaxexplore.R

class QuestionAdapter(private val questions: List<Question>) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val question = questions[position]
                Toast.makeText(itemView.context, "Clicked: ${question.id}", Toast.LENGTH_SHORT).show()
                val intent = Intent(itemView.context, AnswersActivity::class.java)
                intent.putExtra("questionExtra", question)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questions[position]
        holder.questionTextView.text = question.question
        // Bind other data or views here
    }

    override fun getItemCount(): Int {
        return questions.size
    }
}