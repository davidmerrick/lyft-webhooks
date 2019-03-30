package com.merricklabs.lyft.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument
import com.fasterxml.jackson.annotation.JsonProperty

@DynamoDBDocument
data class RidePrice(
        @JsonProperty("amount") @DynamoDBAttribute(attributeName = "amount") val amount: Int,
        @JsonProperty("currency") @DynamoDBAttribute(attributeName = "currency") val currency: String,
        @JsonProperty("description") @DynamoDBAttribute(attributeName = "description") val description: String
)