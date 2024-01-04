package com.gontory.elqirasygontory.ui.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gontory.elqirasygontory.R
import com.gontory.elqirasygontory.adapter.IkhtibarAdapter
import com.gontory.elqirasygontory.databinding.FragmentIkhtibarBinding
import com.gontory.elqirasygontory.utils.Resource
import com.gontory.elqirasygontory.ui.mutholaah.MutholaahViewModel
import kotlinx.coroutines.flow.collectLatest

class IkhtibarFragment : Fragment() {

    private lateinit var binding: FragmentIkhtibarBinding

    private val viewModel by viewModels<MutholaahViewModel>()
    private lateinit var mutholaahAdapter: IkhtibarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIkhtibarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customToolbar()
        setupRvMutholaah()
        retrieveMutholaah()
    }

    private fun retrieveMutholaah() {
        lifecycleScope.launchWhenStarted {
            viewModel.mutholaahList.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        mutholaahAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e("MutholaahList", it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupRvMutholaah() {
        mutholaahAdapter = IkhtibarAdapter(requireContext())
        binding.rvMutholaah.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = mutholaahAdapter
        }
    }

    private fun customToolbar() {
        binding.toolbar.apply {
            navBack.setOnClickListener {
                findNavController().navigateUp()
            }
            tvToolbarName.text = getString(R.string.ikhtibar)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }
}