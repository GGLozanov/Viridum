package com.lozanov.viridum.repo.decl

import com.lozanov.viridum.model.network.SketchFabModelResponse
import com.lozanov.viridum.model.network.SketchFabOAuthResponse

interface SketchFabRepository {
    // TODO: Fill params
    fun authenticate(): SketchFabOAuthResponse

    // TODO: Random model? Based on user?
    fun fetchModel(): SketchFabModelResponse
}