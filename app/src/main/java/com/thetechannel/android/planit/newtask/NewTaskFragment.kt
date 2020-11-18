package com.thetechannel.android.planit.newtask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.thetechannel.android.planit.MyApplication
import com.thetechannel.android.planit.databinding.FragmentNewTaskBinding
import kotlinx.android.synthetic.main.fragment_home.view.*

class NewTaskFragment : Fragment() {

    private val viewModel by viewModels<NewTaskViewModel> {
        NewTaskViewModelFactory((requireActivity().applicationContext as MyApplication).repository)
    }

    private lateinit var viewDataBinding: FragmentNewTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentNewTaskBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpCategorySpinner()
        setUpTaskMethodSpinner()
    }

    private fun setUpCategorySpinner() {
        viewModel.categoryNames.observe(viewLifecycleOwner, Observer { names ->
            val spinnerAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, names)
            viewDataBinding.categorySpinner.apply {
                this.adapter = spinnerAdapter
            }
        })
    }

    private fun setUpTaskMethodSpinner() {
        viewModel.methodNames.observe(viewLifecycleOwner, Observer { names ->
            val adapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            viewDataBinding.methodSpinner.adapter = adapter
        })
    }
}