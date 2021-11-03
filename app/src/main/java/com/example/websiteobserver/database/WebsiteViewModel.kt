package com.example.websiteobserver.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.websiteobserver.database.entities.Website
import kotlinx.coroutines.launch

class WebsiteViewModel(private val repository: WebsitesRepository) : ViewModel() {
    val allWords: LiveData<List<Website>> = repository.entries.asLiveData()

    fun insert(entry: Website) = viewModelScope.launch {
        repository.insert(entry)
    }
}
