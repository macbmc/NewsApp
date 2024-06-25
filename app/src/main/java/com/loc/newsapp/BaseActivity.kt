package com.loc.newsapp

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel

abstract class BaseActivity<VM:ViewModel>: ComponentActivity() {


     protected abstract val mViewModel: VM
}