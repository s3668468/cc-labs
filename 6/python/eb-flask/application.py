from flask import Flask, request

application = Flask(__name__)

@application.route("/")
def index():
    return "<img src='https://s3668468-lab5-bucket.s3.us-east-1.amazonaws.com/firefox_apache_working.png' />"

if __name__ == '__main__':
    application.run()