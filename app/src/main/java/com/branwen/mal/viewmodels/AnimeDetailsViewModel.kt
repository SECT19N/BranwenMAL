package com.branwen.mal.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branwen.mal.data.repo.AnimeRepository
import com.branwen.mal.models.AnimeNode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(
    private val repo: AnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int = checkNotNull(savedStateHandle["animeId"]) {
        "Missing animeId argument"
    }

    private val _animeDetails = MutableStateFlow<AnimeNode?>(null)
    val animeDetails: StateFlow<AnimeNode?> = _animeDetails

    init {
        loadAnimeDetails()
    }

    fun loadAnimeDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repo.getAnimeDetails(animeId)
            }.onSuccess { details ->
                _animeDetails.value = details
                Timber.d("Loaded anime details for id=$animeId")
            }.onFailure { error ->
                Timber.e(error, "Failed to load anime details for id=$animeId")
            }
        }
    }

    fun shareAnime(context: Context) {
        val url = "https://myanimelist.net/anime/$animeId"
        Timber.i("Sharing anime URL: $url")
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }.also {
            context.startActivity(Intent.createChooser(it, null))
        }
    }

    fun openGenreLink(context: Context, genreId: Int) {
        val url = "https://myanimelist.net/anime/genre/$genreId"
        runCatching {
            Timber.i("Opening genre link: $url")
            CustomTabsIntent.Builder().build()
                .launchUrl(context, url.toUri())
        }.onFailure {
            Timber.e(it,"Error opening genre link: $url")
        }
    }
}