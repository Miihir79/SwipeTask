package com.mihir.swipetask.ui.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mihir.swipetask.common.*
import com.mihir.swipetask.data.ProductsListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class ViewModel(private val application: Application): AndroidViewModel(application) {

    var resp = MutableLiveData<ProductsListResponse>()
    var apiStatus = MutableSharedFlow<String?>(replay = 1)

    fun getDashBoardData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (Utils.isNetworkConnected(getApplication())) {
                    val response = AppObjectController.service.getProductData()
                    if (response.isSuccessful) {
                        MainScope().launch {
                            response.body()?.let { resp.value = it }
                        }
                    } else {
                        apiStatus.emit(API_FAILED)
                    }
                } else {
                    apiStatus.emit(NO_NETWORK)
                }

            } catch (e:Exception) {
                e.printStackTrace()
                apiStatus.emit(API_FAILED)
            }

        }
    }

    fun postProductDataWithoutImage(prodName: String, prodType: String, prodSellingPrice: String, prodTaxRate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = AppObjectController.service.postProductData(prodName, prodType, prodSellingPrice,prodTaxRate
                )
                if (response.isSuccessful) {
                    val json = JSONObject(Gson().toJson(response.body()))
                    val message = json.getString("message")
                    MainScope().launch {
                        application.showToastMessage(message)
                    }

                    apiStatus.emit(API_SUCCESS)
                } else {
                    apiStatus.emit(API_FAILED)
                }

            } catch (e:Exception) {
                e.printStackTrace()
                apiStatus.emit(API_FAILED)
            }
        }
    }

    fun postProductDataWithImage(prodName: RequestBody, prodType: RequestBody, prodSellingPrice: Double, prodTaxRate: Double, image:MultipartBody.Part) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = AppObjectController.service.postProductDataWithImage(prodName, prodType, prodSellingPrice,prodTaxRate,image
                )
                if (response.isSuccessful) {
                    val json = JSONObject(Gson().toJson(response.body()))
                    val message = json.getString("message")
                    MainScope().launch {
                        application.showToastMessage(message)
                    }

                    apiStatus.emit(API_SUCCESS)
                } else {
                    apiStatus.emit(API_FAILED)
                }

            } catch (e:Exception) {
                e.printStackTrace()
                apiStatus.emit(API_FAILED)
            }
        }
    }
}