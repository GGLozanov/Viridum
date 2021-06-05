package com.lozanov.viridum.model.network

data class SketchFabModelResponse(
    val gltf: Gltf, // archive
    val usdz: Usdz // file
)

data class Gltf(
    val expires: Int,
    val size: Int,
    val url: String
)

data class Usdz(
    val expires: Int,
    val size: Int,
    val url: String
)
