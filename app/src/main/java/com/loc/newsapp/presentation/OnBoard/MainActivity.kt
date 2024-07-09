package com.loc.newsapp.presentation.OnBoard

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.loc.newsapp.BaseActivity
import com.loc.newsapp.data.entity.ConnectivityClass
import com.loc.newsapp.presentation.home.HomeActivity
import com.loc.newsapp.presentation.loading.LoadingScreen
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalFoundationApi::class)
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>() {
    override val mViewModel: MainViewModel by viewModels()
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
            Box(modifier = Modifier.fillMaxSize())
            {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

    }

    private fun getCurrentDeviceLocation() {

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mViewModel.location()
        } else locationPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)

    }


    private fun observeAppReadyState() {
        mViewModel.apply {
            observeNetworkStatus()
            getOnBoardData()

            isAppReady.observe(this@MainActivity) { appReady ->
                when (appReady) {
                    true -> {
                        keepSplashCondition = false
                        networkStatus.observe(this@MainActivity) { status ->
                            when (status) {
                                is ConnectivityClass.Connected -> {
                                    openHomeState.observe(this@MainActivity) {
                                        if (it) {
                                            setLocation.observe(this@MainActivity)
                                            { location ->
                                                if (location) {
                                                    startActivity(
                                                        Intent(
                                                            this@MainActivity,
                                                            HomeActivity::class.java
                                                        )
                                                    )

                                                }
                                            }
                                        } else {
                                            setContent {
                                                OnBoardScreen(mViewModel)
                                            }
                                        }
                                    }
                                }

                                else -> setContent { LoadingScreen() }
                            }
                        }


                    }

                    false -> {
                        keepSplashCondition = true
                    }
                }
            }
        }
    }
}


