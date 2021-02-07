package com.tanlifei.app.main.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tanlifei.app.persenal.network.SettingNetwork
import com.tanlifei.app.persenal.viewmodel.SettingViewModel

class SettingModelFactory(private val repository: SettingNetwork) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingViewModel(repository) as T
    }
}