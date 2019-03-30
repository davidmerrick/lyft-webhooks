I originally wrote this to aggregate all my Lyft rides in a DynamoDB table, so I could query for totals based on the month. 

But then, partway through, I came across this in the docs:
> You will only receive webhook events for the rides requested via the API.

So it's not really suitable for that purpose, unless I wanted to write my own Lyft client 🙂. 

However, it was a good exercise in using the [Serverless framework](https://serverless.com/) to provision AWS resources, 
storing nested data in DynamoDB, and in creating a webhook handler in Kotlin. So I'm making it public on GitHub on the hope
that it's useful for others as a reference.

## Usage 

First and foremost, install the serverless framework: `npm install -g serverless`.
Initialize it with your AWS creds with `serverless config credentials --provider aws --key EXAMPLE --secret EXAMPLEKEY`.

To deploy, run `./gradlew deploy`.

## Reference

* [Using Kotlin in a Serverless Architecture with AWS Lambda](https://medium.com/tech-travelstart/using-kotlin-in-a-serverless-architecture-with-aws-lambda-part-1-setting-up-the-project-87033790e2f4)