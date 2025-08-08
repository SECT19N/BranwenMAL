package com.branwen.mal.data.repo

import com.branwen.mal.data.local.MangaLocalDataSource
import com.branwen.mal.data.remote.MangaRemoteDataSource

class MangaRepository(
    private val remote: MangaRemoteDataSource,
    private val local: MangaLocalDataSource
) {

}