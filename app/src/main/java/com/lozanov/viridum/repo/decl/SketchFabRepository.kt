package com.lozanov.viridum.repo.decl

import com.lozanov.viridum.model.network.SketchFabModelResponse

interface SketchFabRepository {
    // TODO: Random model? Based on user?
    fun fetchModel(): SketchFabModelResponse
}