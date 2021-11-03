package com.example.websiteobserver.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class WebsiteViewModelFactory(private val repository: WebsitesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WebsiteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WebsiteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
