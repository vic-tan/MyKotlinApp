package com.tanlifei.app.main.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tanlifei.app.main.bean.AdsBean
import com.tanlifei.app.main.viewmodel.SplashViewModel
import com.tanlifei.app.main.network.SplashNetwork
import com.tanlifei.app.main.viewmodel.AdsViewModel

class AdsModelFactory(private val adsBean: AdsBean) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AdsViewModel(adsBean) as T
    }
}