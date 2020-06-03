# Cloud Computing Lab 11

## Create a HelloWorld Lambda function with Eclipse IDE

### 0 Update AWS Credentials

Update AWS credentials

### 1 Install Java

Install Java JDK and JRE

- `sudo apt install default-jdk default-jre`

### 2 Create an AWS Lambda project

On Eclipse IDE

1. Open the AWS icon, choose *New AWS Lambda Java Project*
2. Enter the following details:
   1. Name: `HelloLambda`
   2. Group: `com.amazonaws.lambda`
   3. Artifact: `demo`
   4. Class: `Hello`
   5. Input: *Custom*
3. Click *Finish*

### 3 Implement Handler Method

In Eclipse IDE

1. In Hello.java, enter the following code

```java
package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Hello implements RequestHandler<Object, String>
{
  @Override
  public String handleRequest(Object input, Context context)
  {
    context.getLogger().log("Input: " + input);
    String output = "Hello, " + input + "!";
    return output;
  }
}
```

### 4 Allow Lambda to Assume IAM Role

In AWS Console

1. Go on *Services* and *IAM*
2. Click *Roles* and *Create role*
3. Use the following settings:
   1. Trusted Entity: *AWS Service*
   2. Use case: *Lambda*
   3. *Next Permissions*
   4. Search and tick "AWSLambdaBasicExecutionRole"
   5. *Next* and *Review*
   6. Name: *hello-lambda-role*
   7. *Create role*

### 5 Create an S3 bucket

In AWS Console

1. Go on *Services* and *S3*
2. *Create Bucket*
   1. Name: `s3668468-lab11-bucket`
   2. Region: "N. Virginia"
   3. *Next*, *Next*
   4. Untick *Block all public access* and confirm

### 6 Upload code

In Eclipse IDE

1. Right click code window, select *AWS Lambda* and choose *Upload function to AWS Lambda*
2. On the **Select Target** menu, use the following details:
   1. Handler: `*.demo.Hello`
   2. Region: "N. Virginia"
   3. Create: `HelloFunction`
3. On the **Function Config** menu, use the following details:
   1. Desc: `Says "Hello" to someone`
   2. IAM: `hello-lambda-role`
   3. S2: `s3668468-lab11-bucket`
4. *Finish*
