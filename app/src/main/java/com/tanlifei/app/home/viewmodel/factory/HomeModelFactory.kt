package com.tanlifei.app.home.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tanlifei.app.home.network.HomeNetwork
import com.tanlifei.app.home.viewmodel.HomeViewModel
import com.tanlifei.app.main.viewmodel.SplashViewModel
import com.tanlifei.app.main.network.SplashNetwork

class HomeModelFactory(private val repository: HomeNetwork) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}