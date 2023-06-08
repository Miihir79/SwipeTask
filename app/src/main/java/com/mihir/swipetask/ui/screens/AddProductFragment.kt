package com.mihir.swipetask.ui.screens

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mihir.swipetask.common.*
import com.mihir.swipetask.databinding.FragmentAddProductBinding
import com.mihir.swipetask.ui.vm.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { ViewModelProvider(this)[ViewModel::class.java] }

    // static list of products
    var productList = arrayOf<String?>("Select Product Type","Product 1", "Product 2", "Product 3", "Product 4", "Product 5")

    private val getImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.imageView.setImageURI(it)
        }
    }
    var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(), android.R.layout.simple_spinner_item, productList
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerType.adapter = spinnerAdapter

        binding.imageView.setOnClickListener {
            getImageContract.launch("image/*")
        }

        binding.btnSubmit.setOnClickListener {

            val prodName = binding.etvProductName.text.toString()
            val prodSellingPrice = binding.etvSellingPrice.text
            val prodTaxRate = binding.etvTaxRate.text

            if (prodName.isBlank().not()) {    // checking if product name is not empty
                if (prodSellingPrice.isNullOrBlank().not()) {   // checking if product selling price is not blank
                    if (prodTaxRate.isNullOrBlank().not()) {    // checking if product tax rate is not blank
                        if (binding.spinnerType.selectedItemPosition != 0) {    // checking if product type is selected
                            val prodType = productList[binding.spinnerType.selectedItemPosition]
                            if (Utils.isNetworkConnected(requireContext())) {   // checking for network connectivity
                                if (selectedImageUri == null) {    // if user has not selected any image, i.e uri is null
                                    viewModel.postProductDataWithoutImage(
                                        prodName, prodType.toString(),
                                        prodSellingPrice.toString(), prodTaxRate.toString())
                                } else {
                                    // converting img uri into MultipartBody
                                    val fileDir = requireActivity().filesDir
                                    val file = File(fileDir, "image.png")
                                    val inputStream = selectedImageUri?.let { it1 ->
                                        requireActivity().contentResolver.openInputStream(
                                            it1
                                        )
                                    }
                                    val outputStream = FileOutputStream(file)
                                    inputStream!!.copyTo(outputStream)

                                    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                                    val image = MultipartBody.Part.createFormData("files[]", file.name, requestBody)
                                    binding.loader.visibility = View.VISIBLE
                                    if (prodType != null) {
                                        viewModel.postProductDataWithImage(prodName.toRequestBody("text/plain".toMediaTypeOrNull()), prodType.toRequestBody("text/plain".toMediaTypeOrNull()),
                                            prodSellingPrice.toString().toDouble(), prodTaxRate.toString().toDouble(), image
                                        )
                                    }
                                }

                            } else {
                                requireContext().showToastMessage("Please Check Your Network Connection")
                            }
                        } else {
                            requireContext().showToastMessage("Please Select Product Type")
                        }
                    } else {
                        requireContext().showToastMessage("Please Enter Tax ")
                    }
                } else {
                    requireContext().showToastMessage("Please Enter Price of Product")
                }
            } else {
                requireContext().showToastMessage("Please Enter Product Name")
            }
        }

        lifecycleScope.launch {
            viewModel.apiStatus.collectLatest { status ->
                when (status) {
                    API_FAILED -> {
                        binding.loader.visibility = View.GONE
                        context?.showToastMessage("Something went wrong")
                    }
                    NO_NETWORK -> {
                        binding.loader.visibility = View.GONE
                        context?.showToastMessage("Please Check Your Internet")
                    }
                    API_SUCCESS -> {
                        binding.loader.visibility = View.GONE
                        // closing the add product screen in case of success response
                        findNavController().popBackStack()
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