package com.merricklabs.lyft

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.lyft.models.ReceiptReadyEvent
import com.merricklabs.lyft.models.Ride
import org.apache.logging.log4j.LogManager
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.merricklabs.lyft.models.LyftEvent

class Handler : RequestHandler<APIGatewayProxyRequestEvent, ApiGatewayResponse> {
    private val mapper = ObjectMapper()

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): ApiGatewayResponse {
        if(!validateSignature(input)){
            return ApiGatewayResponse.build {
                statusCode = 400
                objectBody = WebhookResponse("Signature mismatch")
            }
        }

        if(!validateEventType(input)){
            return ApiGatewayResponse.build {
                statusCode = 400
                objectBody = WebhookResponse("That event type is not supported by this endpoint")
            }
        }

        val receiptReadyEvent = mapper.readValue(input.body, ReceiptReadyEvent::class.java)
        saveRideToDb(receiptReadyEvent.event)

        return ApiGatewayResponse.build {
            statusCode = 204
        }
    }

    private fun validateEventType(input: APIGatewayProxyRequestEvent): Boolean {
        val lyftEvent = mapper.readValue(input.body, LyftEvent::class.java)
        return lyftEvent.eventType != "ride.receipt.ready"
    }

    private fun validateSignature(input: APIGatewayProxyRequestEvent): Boolean {
        // Todo: Verify that the signature matches the body: https://developer.lyft.com/v1/reference#section-webhook-verification
        // Check headers
        val signatureHeader = "X-Lyft-Signature"
        val signature = input.headers[signatureHeader]
        return signature != null || signature!!.trim() != ""
    }

    private fun saveRideToDb(ride: Ride){
        val endpoint = System.getenv("DYNAMODB_ENDPOINT")
        val region = System.getenv("DYNAMODB_REGION") ?: "us-west-2"
        val client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build()
        val mapper = DynamoDBMapper(client)
        mapper.save<Any>(ride)
    }

    companion object {
        private val LOG = LogManager.getLogger(Handler::class.java)
    }
}
