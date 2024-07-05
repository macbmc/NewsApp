package com.loc.newsapp.presentation.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.loc.newsapp.BaseActivity
import com.loc.newsapp.data.entity.ConnectivityClass
import com.loc.newsapp.presentation.loading.LoadingScreen
import com.loc.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeViewModel>() {

    override val mViewModel: HomeViewModel by viewModels()

    private fun openLinkUrl() {
        mViewModel.urLink.observe(this) {
            if (it != null) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.toString()))
                startActivity(browserIntent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.urLink.postValue(null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.initHome()
        mViewModel.getNetworkStatus()
        openLinkUrl()
        observeNetwork()
    }

    private fun observeNetwork() {
        mViewModel.status.observe(this@HomeActivity) {
            when (it) {
                is ConnectivityClass.Connected -> {
                    setContent {
                        HomeScreen(mViewModel = mViewModel)
                    }
                }

                else -> {
                    setContent {
                        LoadingScreen()
                    }
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsAppTheme {
        Greeting("Android")
    }
}