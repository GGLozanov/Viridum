package com.lozanov.viridum.state

import com.lozanov.viridum.model.Model

sealed interface NetworkState {
    data class Error(val error: Exception) : NetworkState

    object Loading : NetworkState

    data class Result<T : Model>(val result: T) : NetworkState
}