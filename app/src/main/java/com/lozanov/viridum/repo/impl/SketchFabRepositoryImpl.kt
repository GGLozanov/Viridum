package com.lozanov.viridum.repo.impl

import com.lozanov.viridum.api.ViridumAPI
import com.lozanov.viridum.model.network.SketchFabModelResponse
import com.lozanov.viridum.repo.decl.Repository
import com.lozanov.viridum.repo.decl.SketchFabRepository

class SketchFabRepositoryImpl(
    viridumAPI: ViridumAPI
) : Repository(viridumAPI), SketchFabRepository {
    override fun fetchModel(): SketchFabModelResponse {
        TODO("Not yet implemented")
    }
}