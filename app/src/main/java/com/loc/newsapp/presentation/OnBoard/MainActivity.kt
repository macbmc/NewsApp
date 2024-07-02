package com.loc.newsapp.presentation.OnBoard

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.loc.newsapp.BaseActivity
import com.loc.newsapp.presentation.home.HomeActivity
import com.loc.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalFoundationApi::class)
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>() {
    override val mViewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var keepSplashCondition = true

    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted)
                mViewModel.location()
            else {
                Toast.makeText(this, "NO LOCATION ACCESS", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCurrentDeviceLocation()
        observeAppReadyState()
        installSplashScreen().setKeepOnScreenCondition { keepSplashCondition }
        setContent {
                OnBoardScreen(mViewModel)
        }
    }

    private fun getCurrentDeviceLocation() {

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mViewModel.location()
        } else locationPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)

    }


    private fun observeAppReadyState() {
        mViewModel.getOnBoardData()
        mViewModel.isAppReady.observe(this) { appReady ->
            when (appReady) {
                true -> {
                    Log.d("OpenHome-APPREADY",appReady.toString())
                    mViewModel.openHomeState.observe(this) {
                        Log.d("OpenHome-OPENHOMESTATE",it.toString())
                        if (it) {
                            mViewModel.setLocation.observe(this)
                            { location ->
                                Log.d("LOCATIONSTATUSACT",location.toString())
                                if (location)
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            HomeActivity::class.java
                                        )
                                    )


                            }
                        }
                    }
                    keepSplashCondition = false
                }

                false -> {
                    keepSplashCondition = true
                }
            }
        }
    }
}
