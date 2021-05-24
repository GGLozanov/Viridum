package com.lozanov.viridum.repo.impl

import com.lozanov.viridum.model.network.SketchFabModelResponse
import com.lozanov.viridum.model.network.SketchFabOAuthResponse
import com.lozanov.viridum.repo.decl.SketchFabRepository

class SketchFabRepositoryImpl : SketchFabRepository {
    override fun authenticate(): SketchFabOAuthResponse {
        TODO("Not yet implemented")
    }

    override fun fetchModel(): SketchFabModelResponse {
        TODO("Not yet implemented")
    }
}