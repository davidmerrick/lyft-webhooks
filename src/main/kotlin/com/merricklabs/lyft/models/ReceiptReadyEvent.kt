package com.merricklabs.lyft.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReceiptReadyEvent(
        @JsonProperty("event_id") val eventId: String,
        @JsonProperty("href") val href: String,
        @JsonProperty("event") val event: Ride
)