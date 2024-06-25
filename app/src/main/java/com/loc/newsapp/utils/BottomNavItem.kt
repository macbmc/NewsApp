package com.loc.newsapp.utils

import com.loc.newsapp.R

sealed class BottomNavItem(val label:String,val route:String,val icon:Int) {
    object HomeNav:BottomNavItem("Home","Home", R.drawable.ic_home)
    object BookMarks:BottomNavItem("Marked","BookMarks",R.drawable.ic_bookmark)
}