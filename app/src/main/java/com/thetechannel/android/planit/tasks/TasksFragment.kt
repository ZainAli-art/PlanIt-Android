package com.thetechannel.android.planit.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.thetechannel.android.planit.MyApplication
import com.thetechannel.android.planit.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {

    private val viewModel by viewModels<TasksViewModel> {
        TasksViewModelFactory((requireContext().applicationContext as MyApplication).repository)
    }

    private val args: TasksFragmentArgs by navArgs()

    private lateinit var viewDataBinding: FragmentTasksBinding

    private lateinit var listAdapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentTasksBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        setUpFiltering()
        setUpListAdapter()
    }

    private fun setUpFiltering() {
        viewModel.setFiltering(args.filterType)
    }

    private fun setUpListAdapter() {
        viewDataBinding.tasksList.adapter
        listAdapter = TasksAdapter(viewModel)
        viewDataBinding.tasksList.adapter = this.listAdapter
        viewModel.observableTaskDetails.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
        })
    }
}