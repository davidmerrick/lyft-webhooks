package com.merricklabs.lyft.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LyftEvent(
        @JsonProperty("event_id") val eventId: String,
        @JsonProperty("event_type") val eventType: String
)