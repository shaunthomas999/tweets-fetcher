# Tweets Fetcher

## Description

Tweets Fetcher is a Spring-boot application that fetches upto 200 tweets of a twitter user 
(e.g., ElsevierNews) configured in `app.tweetsFetcher.twitterUser` property of application.properties file.
  
## Configuration
* `app.tweetsFetcher.twitterUser` - Specify the name of twitter user whose tweets needs to be fetched
* `spring.social.twitter.appId` - Consumer Key or API Key received after registering the app in twitter.
Please refer to [Registering an Application with Twitter](https://spring.io/guides/gs/register-twitter-app/).
* `spring.social.twitter.appSecret` - Consumer Secret or API Secret received after registering the app in twitter.

## Run the application

Execute the command `./mvnw spring-boot:run` in ubuntu based systems

## Screenshot

![Screenshot](https://github.com/shaunthomas999/tweets-fetcher/blob/master/tweets-fetcher-app-screenshot.png)