package com.gontory.elqirasygontory.ui.mutholaah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gontory.elqirasygontory.data.Mutholaah
import com.gontory.elqirasygontory.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MutholaahViewModel : ViewModel() {

    private val _mutholaahList = MutableStateFlow<Resource<List<Mutholaah>>>(Resource.Unspecified())
    val mutholaahList: StateFlow<Resource<List<Mutholaah>>> = _mutholaahList

    private val firestore = FirebaseFirestore.getInstance()
    private val mutholaahCollection = firestore.collection("mutholaah")

    init {
        fetchMutholaah()
    }

    private fun fetchMutholaah() {
        viewModelScope.launch {
            _mutholaahList.emit(Resource.Loading())
        }


        mutholaahCollection.orderBy("urutan").get()
            .addOnSuccessListener { result ->
                val mutholaahList = result.toObjects(Mutholaah::class.java)
                viewModelScope.launch {
                    _mutholaahList.emit(Resource.Success(mutholaahList))
                }
            }
            .addOnFailureListener { e ->
                viewModelScope.launch {
                    _mutholaahList.emit(Resource.Error(e.message.toString()))
                }
            }
    }
}