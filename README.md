# lein-eb
Lein AWS Elastic Beanstalk publisher is based on v1.10.77 of the AWS SDK. This is the newest version that is compatible as of lein 2.7.1.

It is likely quite sharp around the edges as it currently addresses the happy-path.

# License
Eclipse Public License - v1.0: <https://eclipse.org/org/documents/epl-v10.html>

# Development
lein install

# Using

Add the following to every project that you want to use EB with:

`[lein-eb 0.0.0-SNAPSHOT]`

From SDK v1.10.77 Javadocs:

> AWS credentials provider chain that looks for credentials in this order:
>
> * Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY (RECOMMENDED since they are recognized by all the AWS SDKs and CLI except for .NET), or AWS_ACCESS_KEY and AWS_SECRET_KEY (only recognized by Java SDK)
> * Java System Properties - aws.accessKeyId and aws.secretKey
> * Credential profiles file at the default location (~/.aws/credentials) shared by all AWS SDKs and the AWS CLI
> * Instance profile credentials delivered through the Amazon EC2 metadata service
>

To target a territory other than "us-east-1" set an environment variable for `AWS_DEFAULT_REGION` to the appropriate region.
