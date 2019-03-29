Writing this to aggregate my Lyft ride history in a DynamoDB table.

This project will consist of an API Gateway endpoint that forwards to a Lambda function written in Kotlin.

Uses the Serverless framework to deploy resources.  

## Usage 

First and foremost, install the serverless framework: `npm install -g serverless`.
Initialize it with your AWS creds with `serverless config credentials --provider aws --key EXAMPLE --secret EXAMPLEKEY`.

To deploy, run `./gradlew deploy`

## Reference

* [Using Kotlin in a Serverless Architecture with AWS Lambda](https://medium.com/tech-travelstart/using-kotlin-in-a-serverless-architecture-with-aws-lambda-part-1-setting-up-the-project-87033790e2f4)