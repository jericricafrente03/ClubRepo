package com.android.clubserve.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrpcResponse<T>(
    val result: TrpcResult<T>? = null,
    val error: TrpcError? = null
)

@Serializable
data class TrpcResult<T>(
    val data: TrpcData<T>
)

@Serializable
data class TrpcData<T>(
    val json: T
)

@Serializable
data class TrpcError(
    val json: TrpcErrorDetail
)

@Serializable
data class TrpcErrorDetail(
    val message: String,
    val code: Int,
    val data: TrpcErrorData? = null
)

@Serializable
data class TrpcErrorData(
    val code: String,
    val httpStatus: Int,
    val path: String? = null
)
