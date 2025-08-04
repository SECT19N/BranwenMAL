package com.branwen.mal.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

fun ViewModel.launchCatching(
    block: suspend () -> Unit,
    post: (suspend () -> Unit)? = null
) {
    viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            block()
        }.onFailure {
            Timber.e(it, "Error in launchCatching")
        }.also {
            post?.let { execute ->
                runCatching {
                    execute()
                }.onFailure {
                    Timber.e(it, "Error in post-block")
                }
            }
        }
    }
}