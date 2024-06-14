package com.example.pukul6.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pukul6.data.api.APIConfig
import com.example.pukul6.data.database.ClassificationDao
import com.example.pukul6.data.database.ClassificationDatabase
import com.example.pukul6.data.database.ClassificationResult
import com.example.pukul6.data.response.Response
import com.example.pukul6.helper.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject

class FraudViewModel(application: Application) : AndroidViewModel(application) {
    private val _fraudData = MutableLiveData<Response>()
    val fraudData: LiveData<Response> = _fraudData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _classificationResult = SingleLiveEvent<ClassificationResult>()
    val classificationResult: LiveData<ClassificationResult> = _classificationResult

    private val classificationDao: ClassificationDao by lazy {
        ClassificationDatabase.getInstance(application).classificationDao()
    }

    fun getProb(requestBody: RequestBody, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiService = APIConfig.getApiService2()
                val response = apiService.classify(requestBody, "Bearer $token")
                Log.d("FraudViewModel", "API Response: $response")

                val probability = response.result[0]
                val entities = response.entities
                val bias = response.bias

                Log.d("FraudViewModel", "Extracted Probability: $probability")
                Log.d("FraudViewModel", "Extracted Entities: $entities")
                Log.d("FraudViewModel", "Extracted Bias: $bias")

                _fraudData.value = response
                _isLoading.value = false

                val jsonString = bodyToString(requestBody)
                val jsonObject = JSONObject(jsonString)
                val inputText = jsonObject.getString("text")
                Log.d("FraudViewModel", "Input text: $inputText")

                saveDataToDatabase(inputText, probability, entities, bias)

                val classificationResult = ClassificationResult(
                    inputText = inputText,
                    probability = probability,
                    entities = entities,
                    bias = bias
                )
                _classificationResult.value = classificationResult
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }


    private fun bodyToString(requestBody: RequestBody): String {
        return requestBody.let { body ->
            val buffer = okio.Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        }
    }

    private fun saveDataToDatabase(
        inputText: String,
        probability: Float,
        entities: List<String>,
        bias: List<String>
        ) {
        viewModelScope.launch {
            try {
                val classificationResult = ClassificationResult(
                    inputText = inputText,
                    probability = probability,
                    entities = entities,
                    bias = bias
                )
                classificationDao.insert(classificationResult)
                Log.d("FraudViewModel", "Data saved to database: $classificationResult")
            } catch (e: Exception) {
                Log.e("FraudViewModel", "Failed to save data to database", e)
            }
        }
    }

    fun getAllClassificationResults(): LiveData<List<ClassificationResult>> {
        return classificationDao.getAllClassificationResults()
    }

    fun delete(classificationResult: ClassificationResult) {
        viewModelScope.launch(Dispatchers.IO) {
            classificationDao.delete(classificationResult)
        }
    }
}
