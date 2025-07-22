package com.branwen.mal.viewmodels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.branwen.mal.models.AnimeNode
import com.branwen.mal.utils.MalServiceBuilder.provideMalApiService
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(
    application: Application,
    private val animeId: Int
) : AndroidViewModel(application) {
    private val _animeDetails = MutableLiveData<AnimeNode>()

    val animeDetails: LiveData<AnimeNode> = _animeDetails

    init {
        loadAnimeDetails(animeId)
    }

    fun loadAnimeDetails(animeId: Int) {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val prefs = context.getSharedPreferences("bran_mal_prefs", MODE_PRIVATE)
            val accessToken = prefs.getString("access_token", null)

            if (accessToken != null) {
                try {
                    val apiService = provideMalApiService(accessToken)
                    val response = apiService.getAnimeDetails(animeId)
                    _animeDetails.value = response
                } catch (e: Exception) {
                    Log.e("AnimeDetailsViewModel", "Error loading anime details ${e.message}", e)
                }
            }
        }
    }
}