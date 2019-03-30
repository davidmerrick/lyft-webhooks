package com.merricklabs.lyft.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@DynamoDBTable(tableName = "Rides")
data class Ride(
        @JsonProperty("ride_id") @DynamoDBHashKey(attributeName = "ride") val rideId: String,
        @JsonProperty("price") @DynamoDBAttribute(attributeName = "price") val price: RidePrice
)