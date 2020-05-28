# Cloud Computing Lab 4

`mkdir lab4 && cd lab4`

## 1 Setting up

### Install requirements

`sudo apt update -y && sudo apt upgrade -y`  
`sudo apt install google-cloud-sdk google-cloud-sdk-app-engine-java maven`

### Git clone

Clone the Google Cloud examples to your machine

`git clone https://github.com/GoogleCloudPlatform/java-docs-samples`

Optional:

`touch .gitignore && echo "java-docs-samples" >> .gitignore`

### Copy and Change directory

Copy directory and change into it

`cp -r java-docs-samples/appengine-java8/endpoints-v2-backend/ endpoints-v2-backend/`  
`cd endpoints-v2-backend`

Optional:  
Put the following code inside the .gitignore for the backends directory  
`*`  
`!pom.xml`

### Remove plugin

Remove lines 84 to 97

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-war-plugin</artifactId>
  <version>3.2.3</version>
  <configuration>
    <webResources>
      <resources>
        <directory>${basedir}/src/main/webapp/WEB-INF</directory>
        <filtering>true</filtering>
        <targetPath>WEB-INF</targetPath>
      </resources>
    </webResources>
  </configuration>
</plugin>
```

### Add in variable names

Replace `YOUR_PROJECT_ID` and add API project name to line 43

`cc-lab4-api-v4`

Replace lines 91 and 92 with the following code:

```xml
<deploy.projectId>YOUR_PROJECT_ID</deploy.projectId>
<deploy.version>GCLOUD_CONFIG</deploy.version>
```

Replace `YOUR_PROJECT_ID` and add Client project name to line 92

`cc-lab4-client-v4`

### Create Google Cloud Projects

Create the google cloud projects with the same id's used in the `pom.xml` file

**Create API with** `gcloud projects create cc-lab4-api-v4`

**Create Client with** `gcloud projects create cc-lab4-client-v4`

## 2 Configure API

### Set project

`gcloud config set project cc-lab4-api-v4`

### Set Authentication

`gcloud auth login`  
`gcloud auth application-default login`

### Setup Maven

`mvn clean package`  
`mvn -U dependency:resolve`  
`mvn endpoints-framework:openApiDocs`

OR

`mvn clean package && mvn -U dependency:resolve && mvn endpoints-framework:openApiDocs`

### Start GCloud Services

`gcloud endpoints services deploy target/openapi-docs/openapi.json`

`gcloud services enable servicemanagement.googleapis.com`  
`gcloud services enable servicecontrol.googleapis.com`  
`gcloud services enable endpoints.googleapis.com`

`gcloud services enable cc-lab4-api-v4`

If enabling the project ID provides an error, use `gcloud services list` too see if the required services are running

## 3 Configure Client

### Set Client project

`gcloud config set project cc-lab4-client-v4`

### Set Client Authentication

`gcloud auth login`  
`gcloud auth application-default login`

### Create an App Engine

`gcloud app create --region=australia-southeast1`

### Deploy App Engine with Maven

`mvn appengine:deploy`

## 4 Send Requests

### Test API

Send a curl request to the API backend

```bash
curl --header "Content-Type: application/json" --request POST --data '{"message":"hello world"}' https://cc-lab4-api-v4.appspot.com/_ah/api/echo/v1/echo
```

### Test Client

Send a curl request to the API client

```bash
curl --header "Content-Type: application/json" --request POST --data '{"message":"hello world"}' https://cc-lab4-client-v4.appspot.com/_ah/api/echo/v1/echo
```

## 5 Debugging

### Fixing 500 Server Error

For some reason the client does not read the variable name at all. To fix this the variable is replace with the api project id and the following commands were entered:

`gcloud config set project cc-lab4-api-v4`

`mvn clean package && mvn -U dependency:resolve && mvn endpoints-framework:openApiDocs`

`gcloud endpoints services deploy target/openapi-docs/openapi.json`

`gcloud config set project cc-lab4-client-v4`

`mvn appengine:deploy`

### Fixing 500 Server Error (No versions exist!)

A file named `web.xml` in `./endpoints-v2-backend/src/main/webapp/WEB-INF/` for an unknown reason creates this error.

To fix this, remove the filter and filter mapping for the endpoint

```xml
<!-- lines 41-44 -->
<filter>
    <filter-name>endpoints-api-configuration</filter-name>
    <filter-class>com.google.api.control.ServiceManagementConfigFilter</filter-class>
</filter>
<!-- lines 60-63 -->
<filter-mapping>
    <filter-name>endpoints-api-configuration</filter-name>
    <servlet-name>EndpointsServlet</servlet-name>
</filter-mapping>
```
