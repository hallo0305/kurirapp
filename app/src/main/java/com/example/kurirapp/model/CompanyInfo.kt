package com.example.kurirapp.model

import com.google.gson.annotations.SerializedName

data class CompanyInfo(
    @SerializedName("name")
    val companyName: String
)
