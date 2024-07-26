package eu.tutorials.locationapp

import android.location.Location
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.tutorials.locationapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel: ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }

    fun sendLocationData(location: Location) {
        val locationEntry = LocationData(
            id = System.currentTimeMillis(),
            latitude = location.latitude,
            longitude = location.longitude
        )

        viewModelScope.launch(Dispatchers.IO) {
            val call = RetrofitInstance.api.createLocationEntry(locationEntry)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("LocationViewModel", "Location data sent successfully")
                    } else {
                        Log.e("LocationViewModel", "Failed to send location data")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("LocationViewModel", "Error: ${t.message}")
                }
            })
        }
    }

}