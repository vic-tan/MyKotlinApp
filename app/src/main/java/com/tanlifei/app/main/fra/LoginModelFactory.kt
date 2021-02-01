package com.tanlifei.app.main.fra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tanlifei.app.main.model.LoginViewModel
import com.tanlifei.app.main.network.LoginNetwork

class LoginModelFactory(private val repository: LoginNetwork) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}