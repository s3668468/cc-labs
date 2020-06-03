# Cloud Computing Lab 9

`mkdir lab9 && cd lab9`

<https://aws.amazon.com/blogs/machine-learning/get-started-with-deep-learning-using-the-aws-deep-learning-ami/>

## 1 Create an EC2 Instance

In AWS Console

### Launching Instance

1. Go to *Services* and *EC2*
2. Click *Launch Instance*

### Installing AMI

1. Search for `deep learning ami`
2. Select "Deep Learning AMI Ubuntu 18.04"
3. Select "m5.2xlarge"
4. Click *Next*, *Next*, *Next* *Next*
5. Add the following security groups
   1. SSH: (TCP, 22, Anywhere)
   2. HTTPS: (TCP, 443, Anywhere)
   3. Custom TCP: (TCP, 8888, Anywhere)
6. Click *Review and Launch* and *Launch*
7. Choose the existing lab5 key and click *View Instances*

### Connect to the Instance

1. Similar to previous labs, use ssh to connect to the instance
   1. `ssh -i lab5.pem ubuntu@<public-dns>`
2. Update the instance
   1. `sudo apt update -y && sudo apt upgrade -y`
3. Reboot the instance
   1. `sudo reboot now`

## 2 Data Analytics in AWS

1. Activate Tensorflow backend
   1. `source activate tensorflow_p36`
2. Use iPython to get an encrypted password. Remember to save the password
   1. `ipython`
   2. [1] `from IPython.lib import passwd`
   3. [2] `passwd()`
      1. Enter a desired password (rmit1234)
      2. Copy the output
   4. [3] `exit`
3. Generate Jupyter Notebook server
   1. `jupyter notebook --generate-config -y`
4. Create HTTPS certificates
   1. `mkdir certs`
   2. `cd certs`
   3. `sudo openssl req -x509 -nodes -days 365 -newkey rsa:1024 -keyout mycert.key -out mycert.pem`
   4. Enter the following:
      1. `au`
      2. `victoria`
      3. `melbourne`
      4. `rmit`
      5. `it`
      6. `s3668468`
      7. `s3668468@student.rmit.edu.au`
5. Change the configuration settings of the server
   1. `cd ~/.jupyter`
   2. `vi jupyter_notebook_config.py`
   3. Enter in the following code

   ```python
    c = get_config()
    c.IPKernelApp.pylab = 'inline'
    c.NotebookApp.certfile = u'/home/ubuntu/certs/mycert.pem'
    c.NotebookApp.keyfile = u'/home/ubuntu/certs/mycert.key'
    c.NotebookApp.ip = '*'
    c.NotebookApp.open_browser = False
    c.NotebookApp.password = u'sha1:93f8bd5dd086:04299b9ca2d2b4dff9e5931efaab839e25fb5358'
    c.NotebookApp.port = 8888
   ```

6. Change the ownership of the home folder
   1. `cd ../..`
   2. `sudo chown -R $USER /home/`
   3. `sudo chown -R $USER ~/.local/share/jupyter`
7. Create and change to the notebook folder
   1. `cd ~`
   2. `mkdir Notebooks`
   3. `cd Notebooks`
8. Start notebook server
   1. `jupyter notebook`
9. Connect to the notebook via public dns with port 8888
   2. <https://ec2-3-235-223-231.compute-1.amazonaws.com:8888>
