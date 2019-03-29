package com.merricklabs.lyft.models

import com.fasterxml.jackson.annotation.JsonProperty

data class RidePrice(
        @JsonProperty("amount") val amount: Int,
        @JsonProperty("currency") val currency: String,
        @JsonProperty("description") val description: String
)