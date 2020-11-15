package com.thetechannel.android.planit.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.databinding.TaskItemBinding

class TasksAdapter(private val viewModel: TasksViewModel) :
    ListAdapter<TaskDetail, TasksAdapter.ViewHolder>(TaskDiffCallback()) {

    class ViewHolder private constructor(val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: TasksViewModel, item: TaskDetail) {
            binding.viewmodel = viewModel
            binding.taskDetail = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<TaskDetail>() {
    override fun areItemsTheSame(oldItem: TaskDetail, newItem: TaskDetail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaskDetail, newItem: TaskDetail): Boolean {
        return oldItem == newItem
    }
}