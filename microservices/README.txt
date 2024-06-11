################
Run Instructions
################
Build the Microservice Images
=============================
For each video-microservice, trending-microservice and subscription-microservice, run the following in a CMD terminal in their respective directory:
gradlew dockerBuild

Bring up the Containers
=======================
In order, run these scripts:
1. compose-kafka.sh
2. compose-topics.sh
3. compose-services.sh

This starts kafka, creates the topics, and finally starts the microservices.
Make sure to wait a bit after running the third script before passing commands through the CLI - it takes a little time for the services to come online, and the Client connection will time out if it's used immediately after step 3.

##################
CLI (client) Usage
##################
How to Use the CLI
==================
Open a CMD terminal in the Client/ directory and write:
gradlew run --args=""

Write the desired command (list below) in between the quotes.

Command list
============

Video microservice
------------------
create-user <username>                     Creates a user with the given username
list-users                                 Lists all users currently saved to the database
post-video <title> <username> <tags..>     Posts a video with a given title, username, and 0 or more tags
get-video <videoId>                        Gets information about a video with the given numerical ID (should be numerical order starting from 1, but check video-microservice logs in Docker to be sure)
watch-video <username> <videoId>           Add a view to a video from a certain user
like-video <username> <videoId>            Add a like to a video from a certain user. Removes a dislike if the user has disliked the video
dislike-video <username> <videoId>         Add a dislike to a video from a certain user. Removes a like if the user has liked the video.


Trending microservice
---------------------
get-trending-tags                          Gets the top 10 tags as of the last hour based on likes


Subscription microservice
-------------------------
subscribe-to-tag <username> <tag>          Adds a subscription to a user for a tag
unsubscribe-from-tag <username> <tag>      Removes a subscription to a user for a tag
get-subscriptions <username>               Gets all tag subscriptions for a user

                                           Code for recommendations based on subscriptions exists, but method is not exposed to the CLI client as it does not work due to an error with the Micronaut database @Query tag