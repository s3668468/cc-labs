# Cloud Computing Lab 5

`mkdir lab5 && cd lab5`

## 1 Setting up

### Requirements

<!-- Create an SSH key:

- `ssh-keygen -m PEM -t rsa -f lab5`  
- Do not enter a passphrase -->

<!-- Log into AWS Educate and get the accounts details:

- Copy AWS CLI details
- `nano ~/.aws/credentials`
- Replace all content with new details -->

## 2 Create an EC2 Instance

1. Head into the AWS Console and change region to US East, N. Virginia.
2. Click on *Services*, *EC2* and *Launch Service*.

## 3 Creating Virtual Machine

1. Select *Ubuntu Server 18.04 LTS 64-bit*
2. Select *t2.micro* (this should be defaulted)
3. Click *Configure Instance Details*
4. Click *Add Storage*
5. Click *Add Tags*
6. Add a tag and enter your username as the key, and "My Linux Server" for the value
   1. For example: [(Key, s3668468)(Value, My Linux Server)]
7. Click *Configure Security Group*
8. Add the following rules and set all sources to *anywhere*: (the rest should auto fill)
   1. SSH
   2. All TCP
   3. All ICMP IPv4
   4. All ICMP IPv6
   5. HTTP
   6. HTTPS
   7. MYSQL/Aurora
9. Click *Review and Launch*
10. Click *Launch*
11. Create a new key pair and name it *lab5*
12. Click *Download Key Pair*. This will download a .pem file
13. Move the .pem file from the downloads to your lab folder and update the permissions:
    1. `mv ~/Downloads/lab5.pem lab5.pem`
    2. `chmod 400 lab5.pem`
14. Click *Launch Instances* and *View Instances*
15. Once the instance is running, select it and copy the Public DNS. Save this as we will be using it later.
    <!-- 1.  `ec2-52-86-36-187.compute-1.amazonaws.com` -->

## 4 Connecting to the Virtual Machine

SSH into the instance:

1. `ssh -i lab5.pem ubuntu@<instance_public_dns>`
2. Enter *yes*

Update sources and Upgrade the system

1. `sudo apt update -y && sudo apt upgrade -y`

## 5 Customizing the Virtual Machine

### Install Java JDK

We will be install the official Oracle JDK

1. Add the repository
   1. `sudo add-apt-repository ppa:linuxuprising/java -y`
2. Update the sources
   1. `sudo apt update -y`
3. Install JDK 14
   1. `sudo apt install -y oracle-java14-installer`
4. During installation, click *ok* and *yes*
5. Check the version installed
   1. `java --version`
6. Export the environment variables
   1. `export JAVA_HOME=/usr/bin/java`
   2. `export PATH=$JAVA_HOME/bin:$PATH`

### Upload files from your machine

0. Download FileZilla
   1. `sudo apt install filezilla -y`
1. Open Filezilla
2. Click *Edit* and *Settings*
3. In Settings, open *SFTP* and add the key file
4. Click *Ok*
5. Click *File*, *Site Manager* and add a new site
6. Add the following details
   1. Host: <instance_public_dns>
   2. Port: 22
   3. Protocol: SFTP
   4. Logon Type: Normal
   5. User: ubuntu
   6. Password
7. Let FileZilla save passwords
8. On the top left, open the sites and select you server
9. Upload `Server.java` from Lab 5 Module (*Refer to filezilla_upload_server.png*)

### Duplicate the instance

1. Open AWS Console
2. Select the server instance and click *Launch more like this*
3. Select *Edit Tags* and change the Value to "My Linux Client"
4. Click *Launch* and select the existing lab5 key pair
5. Once the instance is running, select it and copy the Public DNS. Save this as we will be using it later.
   <!-- 1. `ec2-100-26-231-234.compute-1.amazonaws.com` -->
6. Follow **Connecting to the Virtual Machine** to connect to the duplicated instance
7. Follow **Install Java JDK** to install java on the duplicated instance
8. Follow **Upload files from the client machine** and instead of upload `Server.java` upload `Client.java`

### Create Client-Server communication

1. On the client instance, edit `Client.java` to contain the Public IP of the server instance.
   1. `nano Client.java`
   2. *Edit file*
2. Compile the file on both instances
   1. `javac Server.java`
   2. `javac Client.java`
3. Run the file on both instances
   1. `java Server`
   2. `java Client`
4. You should see the two instances communicating

While were here, reboot the instances.

### Prepare the apache server

On the server instance

The steps in the lab5.pdf do not work. Adding the PHP beta repository is a work around for installing the correct version of php for this lab.  
Resources:  
<https://www.digitalocean.com/community/tutorials/how-to-upgrade-to-php-7-on-ubuntu-14-04>

0. Add PHP Beta Repository
   1. `sudo add-apt-repository ppa:ondrej/php && sudo apt update`
1. Install apache
   1. `sudo apt install -y apache2`
2. Install php
   1. `sudo apt install -y php7.0 libapache2-mod-php7.0`
   2. `sudo a2enmod mpm_prefork && sudo a2enmod php7.0`
3. Start the apache web server
   1. `sudo service apache2 restart`  
Now we edit the `index.php` file
4. `sudo nano /var/www/html/index.php`
   1. Enter `<?php echo "Hello World! This is Lab 5!!; ?>`

On your machine

To test that the apache web server is running, open the AWS Console then copy and paste the Public DNS of the server instance into your web browser.

### Create AWS S3 Bucket

On the AWS Console

1. Click *Services* and under Storage click *S3*
2. Click *Create Bucket*
3. Name the bucket "s3668468-lab5-bucket" (this name must be unique) and click *Next*
4. Click *Next*, *Next* and *Create Bucket*
5. Ensure that the permissions for the S3 bucket do NOT block public access
6. Click on the newly created bucket and upload an image. In this case we will upload a previous screenshot.
7. Click *Upload*, *Add files*, add the screenshot, *Next*, *Next*, *Next*, and *Upload*
8. Once uploaded, tick the image, go to *Actions*, and select *Make Public*

## 5 Amazon DynamoDB

1. Open Eclipse IDE
2. Dropdown the Amazon Icon and select *New AWS Java Project*
3. Enter "lab5-db" for the project name and click *Finish*

We now need to set up the AWS credentials for Eclipse. These credentials change every 3 hours so if there is an authentication error, check your credentials.

1. Dropdown the Amazon Icon and select *Preferences*
2. Replace the details credentials with the credentials given in the AWS Educate Account Details screen.
3. Click *Apply and Close* and *Always Update*

Now we can create and configure the database

1. Click on the project and open the *src* directory
2. Create a new package called `com.amazonaws.codesamples`
3. Right click the package and create a new class called `CreateTablesLoadData`
4. Copy and Paste the content from `CreateTableLoadData.java` in the Lab 5 module into the new class.
  This code will not work with your user because it is set to 'adminuser'. This needs to change. On line 31, remove "adminuser" so that the program uses the default user.
5. Save the file, right click it and select *Run As* and *Java Application*
