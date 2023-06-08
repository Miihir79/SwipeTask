package com.mihir.swipetask.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mihir.swipetask.R
import com.mihir.swipetask.common.*
import com.mihir.swipetask.databinding.FragmentProductListBinding
import com.mihir.swipetask.ui.adapter.AdapterProduct
import com.mihir.swipetask.ui.vm.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { AdapterProduct() }

    private val viewModel by lazy { ViewModelProvider(this)[ViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshProduct.setOnRefreshListener {
            viewModel.getDashBoardData()
        }

        // adding a greeting for the user
        binding.txtVGreeting.text = Utils.greetUser()

        binding.rvProduct.adapter = adapter
        binding.rvProduct.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        binding.srchVProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String): Boolean {
                adapter.filter = p0
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }
        })

        binding.srchVProduct.setOnClickListener {
            // making the whole searchbar touchable instead of just the search icon
            binding.srchVProduct.isIconified = false
        }

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_productListFragment_to_addProductFragment)
        }
        observe()
    }

    override fun onResume() {
        super.onResume()
        // refreshing data everytime user comes back to the screen
        viewModel.getDashBoardData()
        binding.swipeRefreshProduct.isRefreshing = true
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.resp.observe(viewLifecycleOwner) {
                adapter.accessoryItemData = it
                binding.swipeRefreshProduct.isRefreshing = false
            }
        }
        lifecycleScope.launch {
            viewModel.apiStatus.collectLatest { status ->
                when (status) {
                    API_FAILED -> {
                        binding.swipeRefreshProduct.isRefreshing = false
                        context?.showToastMessage("Something went wrong")
                    }
                    NO_NETWORK -> {
                        binding.swipeRefreshProduct.isRefreshing = false
                        context?.showToastMessage("Please Check Your Internet")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}