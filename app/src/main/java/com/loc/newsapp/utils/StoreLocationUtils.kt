package com.loc.newsapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class StoreLocationUtils(
    private val appContext: Context,
    private val sharedPreference: SharedPreferences
) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission", "CommitPrefEdits")
    fun execute(): Flow<Boolean> {
        return callbackFlow {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                        return CancellationTokenSource().token
                    }

                    override fun isCancellationRequested(): Boolean {
                        return false
                    }

                }).addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    val geocoder = Geocoder(appContext)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(it.result.latitude,
                            it.result.longitude,
                            1, object : Geocoder.GeocodeListener {
                                override fun onGeocode(addresses: MutableList<Address>) {
                                    launch {
                                        send(
                                            sharedPreference.edit()
                                                .putString(
                                                    "DEVICE_LOCATION_COUNTRY",
                                                    addresses[0].countryName
                                                )
                                                .putString(
                                                    "DEVICE_LOCATION_COUNTRY_CODE",
                                                    addresses[0].countryCode
                                                )
                                                .commit()
                                        )
                                    }
                                }

                                override fun onError(errorMessage: String?) {
                                    super.onError(errorMessage)
                                    launch {
                                        send(true)
                                    }
                                }


                            })

                    } else {

                        val geoLocation =
                            geocoder.getFromLocation(it.result.latitude, it.result.longitude, 1)
                        launch {
                            send(
                                sharedPreference.edit()
                                    .putString(
                                        "DEVICE_LOCATION_COUNTRY",
                                        geoLocation!![0].countryName
                                    )
                                    .putString(
                                        "DEVICE_LOCATION_COUNTRY_CODE",
                                        geoLocation[0].countryCode
                                    )
                                    .commit()
                            )
                        }
                    }
                }

            }
                .addOnFailureListener {
                    launch {
                        send(true)
                    }
                }
            awaitClose()
        }
    }


}