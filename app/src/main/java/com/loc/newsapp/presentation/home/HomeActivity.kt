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
        openLinkUrl()
        setContent {
           HomeScreen(mViewModel = mViewModel)

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