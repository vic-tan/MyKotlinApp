package com.common.http


class PageList<T> {
    val curPage = 0 //当前页数
    val pageCount = 0 //总页数
    val total = 0 //总条数
    val datas: MutableList<T>? = null
}