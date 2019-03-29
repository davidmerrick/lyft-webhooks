package com.merricklabs.lyft

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.lyft.models.ReceiptReadyEvent
import com.merricklabs.lyft.models.Ride
import org.apache.logging.log4j.LogManager


// Todo: Get a webhook verification token and check for it. https://www.lyft.com/developers
class Handler : RequestHandler<APIGatewayProxyRequestEvent, ApiGatewayResponse> {
    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): ApiGatewayResponse {
        LOG.info("received: ${input.body}")
        val mapper = ObjectMapper()
        val receiptReadyEvent = mapper.readValue(input.body, ReceiptReadyEvent::class.java)

        return ApiGatewayResponse.build {
            statusCode = 200
            objectBody = WebhookResponse("Your ride cost: $${receiptReadyEvent.event.price.amount/100}")
            headers = mapOf("X-Powered-By" to "AWS Lambda & serverless")
        }
    }

    fun saveRideToDb(ride: Ride){
        val endpoint = System.getenv("DYNAMODB_ENDPOINT")
        val region = System.getenv("DYNAMODB_REGION")
        val tableName = System.getenv("DYNAMODB_TABLE")
        val client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build()

        val dynamoDB = DynamoDB(client)
        val table = dynamoDB.getTable(tableName)
    }

    companion object {
        private val LOG = LogManager.getLogger(Handler::class.java)
    }
}
