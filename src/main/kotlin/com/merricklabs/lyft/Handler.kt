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


// Todo: Get a webhook verification token and check for it. https://www.lyft.com/developers
class Handler : RequestHandler<APIGatewayProxyRequestEvent, ApiGatewayResponse> {
    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): ApiGatewayResponse {
        if(!validateInput(input)){
            return ApiGatewayResponse.build {
                statusCode = 400
            }
        }

        val mapper = ObjectMapper()
        // Check if event_type is ride.receipt.ready
        val lyftEvent = mapper.readValue(input.body, LyftEvent::class.java)
        if(lyftEvent.eventType != "ride.receipt.ready"){
            LOG.info("Incorrect event type: ${lyftEvent.eventType}")
            return ApiGatewayResponse.build {
                statusCode = 400
                objectBody = WebhookResponse("event_type ${lyftEvent.eventType} is not supported by this endpoint")
            }
        }

        val receiptReadyEvent = mapper.readValue(input.body, ReceiptReadyEvent::class.java)

        LOG.info("Saving ride to db")
        saveRideToDb(receiptReadyEvent.event)

        return ApiGatewayResponse.build {
            statusCode = 200
            objectBody = WebhookResponse("Your ride cost: $${receiptReadyEvent.event.price.amount/100}")
            headers = mapOf("X-Powered-By" to "AWS Lambda & serverless")
        }
    }

    private fun validateInput(input: APIGatewayProxyRequestEvent): Boolean {
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
