package com.merricklabs.lyft.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Ride(
        @JsonProperty("ride_id") val rideId: String,
        @JsonProperty("price") val price: RidePrice
)