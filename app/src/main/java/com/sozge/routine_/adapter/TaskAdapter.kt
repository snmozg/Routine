package com.sozge.routine_.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.sozge.routine_.R
import com.sozge.routine_.databinding.FragmentTaskBinding
import com.sozge.routine_.databinding.RecyclerRowBinding
import com.sozge.routine_.model.Task
import com.sozge.routine_.view.ListFragment
import com.sozge.routine_.view.ListFragmentDirections

class TaskAdapter(val taskList : List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    class TaskHolder (val binding: RecyclerRowBinding ): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskHolder(recyclerRowBinding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.binding.titleText.text = taskList[position].task
        holder.binding.descText.text = taskList[position].desc

        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToTaskFragment(information = "used",id = taskList[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleText : TextView = itemView.findViewById(R.id.titleText)
        val descText : TextView = itemView.findViewById(R.id.descText)
    }
}