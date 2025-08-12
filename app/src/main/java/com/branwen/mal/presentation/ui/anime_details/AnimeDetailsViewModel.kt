package com.branwen.mal.presentation.ui.anime_details

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branwen.mal.data.remote.dto.anime.AnimeNode
import com.branwen.mal.data.repository.AnimeRepository
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

    private val _uiModel = MutableStateFlow<AnimeNode?>(null)
    val uiModel: StateFlow<AnimeNode?> = _uiModel

    init {
        loadAnimeDetails()
    }

    fun loadAnimeDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repo.getAnimeDetails(animeId)
            }.onSuccess { node ->
                _uiModel.value = node
                Timber.d("Loaded details for id=${node.id}")
            }.onFailure { e ->
                Timber.e(e, "Failed loading anime details for id=$animeId")
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
            Timber.e(it, "Error opening genre link: $url")
        }
    }
}