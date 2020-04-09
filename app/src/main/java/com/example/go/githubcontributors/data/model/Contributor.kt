package com.example.go.githubcontributors.data.model

import com.google.gson.annotations.SerializedName

data class Contributor(
    @SerializedName("login") val name: String,
    val avatarUrl: String,
    val contributions: Int
)