package com.tanlifei.app.main.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tanlifei.app.main.network.SplashNetwork
import com.tanlifei.app.main.viewmodel.SplashViewModel

class SplashModelFactory(private val repository: SplashNetwork) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(repository) as T
    }
}