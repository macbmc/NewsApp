package com.loc.newsapp.data.entity

data class SearchDataClass(
    val query:String,
    val onQueryChange:(String)->Unit,
    val onSearch:(String)->Unit ,
    val active:Boolean ,
    val onActiveChange:(Boolean)->Unit,
    val resultList:List<Any>

)
