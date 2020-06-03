# Cloud Computing Lab 6

`mkdir lab6 && cd lab6`

<https://github.com/AccelNA/aws-coe/wiki/Building-And-Deploying-Web-Apps-Into-AWS-Cloud-Using-AWS-Elastic-Beanstalk>

<https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3-install.html>

<https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/create-deploy-python-flask.html>

## 1 Create Elastic Beanstalk PHP Application with AWS COnsole

1. Head into the AWS Console and change the region to US East, N. Virginia.
2. Click on *Services* and *Elastic Beanstalk* under Compute.
3. Click *Create Application*
4. Enter the following details
   1. Name: `"<username>-beanstalk-php"`
   2. Platform: `PHP`
   3. Platform Branch: `PHP 7.2 64-bit Linux 2`
   4. Application Code: `Sample`
5. Click *Create Application*
6. Once created, a link will be provided to test the application. Click on the link to see the application (*refer to firefox_beanstalk_working.png*)
7. Click *Upload and Deploy* and upload the provided PHP sample
8. Once uploaded, click *Deploy*
9. Once deployed, click on the provided link to test that the application works (*refer to firefox_sample_beanstalk_working*)

## 2 Create Elastic Beanstalk PHP Application with EB CLI

0. Install EB CLI
   1. Ensure Python 2.7 or later is installed
      1. `python --version`
   2. Install pip
      1. `sudo apt install python-pip`
   3. Install EB CLI with pip
      1. `pip install awsebcli --upgrade --user`
   4. Verify EB CLI installed correctly
      1. `eb --version`
1. Update AWS credentials with details from AWS Classroom
   1. `nano ~/.aws/credentials`
2. Initialize EB CLI in the `lab6/php` directory
   1. `eb init`
   2. Select the correct region
   3. Select the previous project
3. Create the application
   1. `eb create`
   2. *enter* x4
   3. Wait for the application to deploy
4. View the application
   1. `eb open`
   2. (*refer to firefox_sample_beanstalk_working_eb_cli*)

## 3 Create Elastic Beanstalk Python Application with EB CLI

### Set up Python virtual environment with Flask

1. Create project directory
   1. `mkdir eb-flask && cd eb-flask`
2. Create and activate a virtual environment named `virt`
   1. `virtualenv virt`
   2. `source virt/bin/activate`
3. Install Flask with pip
   1. `pip install flask==1.0.2`
4. View installed libraries
   1. `pip freeze`
5. Save output to `requirements.txt`
   1. `pip freeze > requirements.txt`

### Create a Flask application

1. Create a new file called `application.py` or use the provided files
2. Run `application.py` with Python
   1. `python application.py`
3. The application should run in a local environment
   1. (*refer to firefox_local_python_working.png*)

### Deploying the application with EB CLI

0. Create a `.ebignore` file to ignore the virt folder from being deployed
   1. `echo virt > .ebignore`
1. Initialize EB CLI in `eb-flask` directory
   1. `eb init`
   2. Select the correct region
   3. Select *Create new application*
   4. Enter `"<username>-beanstalk-python"`
   5. Select *Python*
   6. Select *Python 2.7*
   7. Select no (*n*)
2. Create an environment and deploy the application
   1. `eb create`
   2. `flask-env`
   3. *enter* x3
   4. This will take a while, grab a coffee
3. Once created, open the application
   1. `eb open`

### Cleanup

To cleanup the flask environment, run `eb terminate flask-env`
