package com.example.chekhebar.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chekhebar.data.Result
import com.example.chekhebar.data.source.MapRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(private val mapRepository: MapRepository) : ViewModel() {

    private val _places: MutableLiveData<Result<List<PlaceView>>?> = MutableLiveData()
    val places: LiveData<Result<List<PlaceView>>?>
        get() = _places

    private val _placeDetail: MutableLiveData<Result<PlaceDetailView?>?> = MutableLiveData()
    val placeDetail: LiveData<Result<PlaceDetailView?>?>
    get() = _placeDetail

    fun getNearbyPlaces(
        lat: Double,
        long: Double,
        limit: Int,
        offset: Int,
        isInitialLoad: Boolean
    ) = viewModelScope.launch {
        _places.value = mapRepository.getNearbyPlaces(lat, long, limit, offset, isInitialLoad)
    }

    fun getPlaceDetail(placeId: String) = viewModelScope.launch {
        _placeDetail.value = mapRepository.getPlaceDetail(placeId)
    }

}